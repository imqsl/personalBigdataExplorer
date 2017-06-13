package imqsl.com.explorer.UserActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class HistoryActivity extends AppCompatActivity {

    private SimpleAdapter adapter=null;
    private ListView listView=null;
    View view=null;
    private ArrayList<String> list_name=null;
    private ArrayList<String> list_url=null;
    List<Map<String,String>> data=null;
    TextView textback=null;
    TextView tv_clearall=null;
    OpenHelper helper=null;
    SQLiteDatabase database=null;
    Cursor cursor=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
       helper=new OpenHelper(HistoryActivity.this,"mylist.db", null, 1);
        database=helper.getWritableDatabase();
        cursor=database.rawQuery("select * from history",null);
        cursor.moveToFirst();
        list_name=new ArrayList<String>();
        list_url=new ArrayList<String>();

       for (int i=0;i<cursor.getCount();i++){
           list_url.add(cursor.getString(0));
           list_name.add(cursor.getString(1));
            cursor.moveToNext();
        }
        data=new ArrayList<Map<String,String>>();
        for (int i=0;i<list_url.size();i++){
            Map<String,String> map=new HashMap<String,String>();
            map.put("url", list_url.get(i));
            map.put("name",list_name.get(i));
            data.add(map);
        }
        listView=(ListView) findViewById(R.id.listview_history);
        textback= (TextView) findViewById(R.id.history_back);
        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryActivity.this.finish();
            }
        });

        tv_clearall=(TextView) findViewById(R.id.clearall);
        tv_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HistoryActivity.this).setIcon(android.R.drawable.ic_dialog_info).setMessage("确定要清空所有?").setTitle("提示").setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int a= database.delete("history", null,null);
                        Toast.makeText(HistoryActivity.this,"已清空"+a+"条历史记录",Toast.LENGTH_SHORT).show();
                        cursor.close();
                        database.close();
                        helper.close();
                        HistoryActivity.this.finish();
                    }
                }).create().show();

            }
        });

        adapter=new SimpleAdapter(this,data,R.layout.item_web,new String[]{"url","name"},new int[]{R.id.text_url,R.id.text_title});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(HistoryActivity.this, DiscoveryActivity.class);
                intent.putExtra("url",list_url.get(i));
                startActivity(intent);

            }
        });

    }

}
