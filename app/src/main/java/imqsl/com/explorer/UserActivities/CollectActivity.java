package imqsl.com.explorer.UserActivities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imqsl.com.explorer.DiscoveryActivities.DiscoveryActivity;
import imqsl.com.explorer.OpenHelper;
import imqsl.com.explorer.R;

public class CollectActivity extends AppCompatActivity {

    private SimpleAdapter adapter=null;
    private ListView listView=null;

    private ArrayList<String> list_name=null;
    private ArrayList<String> list_url=null;
    List<Map<String,String>> data=null;
    private OpenHelper helper=null;
    private SQLiteDatabase database=null;
    private Cursor cursor=null;
    TextView textback=null;
    LinearLayout linearLayout=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        data=new ArrayList<Map<String,String>>();
        list_name=new ArrayList<String>();
        list_url=new ArrayList<String>();
        helper=new OpenHelper(this,"mylist.db",null,1);
        database=helper.getWritableDatabase();
        cursor=database.rawQuery("select * from collect",null);
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount();i++){
            list_url.add(cursor.getString(0));
            list_name.add(cursor.getString(1));
            cursor.moveToNext();
        }
        for (int i=0;i<list_url.size();i++){
            Map<String,String> map=new HashMap<String,String>();
            map.put("url", list_url.get(i));
            map.put("name",list_name.get(i));
            data.add(map);
        }

        textback= (TextView) findViewById(R.id.collect_back);
        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectActivity.this.finish();
            }
        });
        listView=(ListView) findViewById(R.id.listview_collect);
        adapter=new SimpleAdapter(this,data,R.layout.item_web,new String[]{"url","name"},new int[]{R.id.text_url,R.id.text_title});
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(CollectActivity.this, DiscoveryActivity.class);
                intent.putExtra("url",list_url.get(i));
                    startActivity(intent);


            }
        });
        //设置长按的监听器,弹出一个popupwindow
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Toast.makeText(CollectActivity.this,""+i,Toast.LENGTH_SHORT).show();
                View view1= LayoutInflater.from(CollectActivity.this).inflate(R.layout.deletepop,null);
                linearLayout=(LinearLayout) view1.findViewById(R.id.delete);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      int a=  database.delete("collect","url=?",new String[]{list_url.get(i)});
                        Toast.makeText(CollectActivity.this,"删除了"+a+"条记录",Toast.LENGTH_SHORT).show();
                    CollectActivity.this.finish();
                    }
                });
                PopupWindow window=new PopupWindow(view1,view.getWidth()*2/3,view.getHeight()*2/3,true);
                window.setOutsideTouchable(true);
                window.setBackgroundDrawable(new ColorDrawable());
                setBgAlpha(0.5f);
                window.showAsDropDown(view,view.getWidth()/6,0);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        setBgAlpha(1f);
                    }
                });
                return false;
            }
        });

    }
/*设置上下文菜单来处理长按选中的item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1,0,0,"删除");
        menu.add(1,1,1,"编辑");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(CollectActivity.this,"delete", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(CollectActivity.this,"edit", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }*/

    void setBgAlpha(float bgAlpha){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=bgAlpha;
        getWindow().setAttributes(lp);
    }
}
