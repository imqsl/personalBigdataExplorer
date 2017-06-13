package imqsl.com.explorer.SearchActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imqsl.com.explorer.MainActivity;
import imqsl.com.explorer.R;
import imqsl.com.explorer.Tools;
import imqsl.com.explorer.UserActivities.CollectActivity;
import imqsl.com.explorer.UserActivities.DownloadActivity;
import imqsl.com.explorer.UserActivities.HistoryActivity;
import imqsl.com.explorer.UserActivities.SetActivity;


public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    TextView backtv = null;
    TextView hometv = null;
    TextView refreshtv = null;
    TextView moretv = null;
    TextView result_num = null;
    ListView result_listview = null;
    PopupWindow myPopupWindow = null;
    GridView menuGrid = null;
    LinearLayout linearbottom = null;
    View result_progress = null;
    View result_form = null;
    SimpleAdapter adapter = null;
    String[] menu_item_name = {"收藏", "历史", "下载", "设置", "退出"};
    String url = "http://192.168.1.152:8080/BigdataHandler/testservlet";
    String body = null;
    Handler handler = null;
    List<Map<String, String>> list = null;
    JSONArray array = null;
    int[] menu_item_image = {R.drawable.collect, R.drawable.history,
            R.drawable.download, R.drawable.set, R.drawable.quit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final String name = getIntent().getStringExtra("name");
        result_listview = (ListView) findViewById(R.id.result_listview);
        result_num = (TextView) findViewById(R.id.result_num);
        backtv = (TextView) findViewById(R.id.back_tx);
        hometv = (TextView) findViewById(R.id.home_tx);
        refreshtv = (TextView) findViewById(R.id.refresh_tx);
        moretv = (TextView) findViewById(R.id.more_tx);

        result_progress = findViewById(R.id.result_progress);
        result_form = findViewById(R.id.result_form);
        backtv.setOnClickListener(this);
        hometv.setOnClickListener(this);
        refreshtv.setOnClickListener(this);
        moretv.setOnClickListener(this);
        list = new ArrayList<Map<String, String>>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("new thread start");
                    String param = null;
                    try {
                        param = "name=" + URLEncoder.encode(name, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    body = Tools.getJsonArray(url, param);
                    System.out.println("body:" + body);
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);

                }
            }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (body != null) {
                    try {
                        array = new JSONArray(body);
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            JSONObject obj = new JSONObject();
                            obj = array.getJSONObject(i);
                            map.put("name", obj.getString("name"));
                            map.put("sex", obj.getString("sex"));
                            map.put("email", obj.getString("email"));
                            map.put("phonenum", obj.getString("phonenum"));
                            map.put("alipay", obj.getString("alipay"));
                            map.put("wechat", obj.getString("wechat"));
                            map.put("qq", obj.getString("qq"));
                            map.put("jd", obj.getString("jd"));
                            map.put("baidu", obj.getString("baidu"));
                            list.add(map);

                        }
                        System.out.println("--------listsize:" + list.size());


                        adapter = new SimpleAdapter(ResultActivity.this, list, R.layout.result_item, new String[]{"name", "sex", "email",
                                "phonenum", "alipay", "qq", "wechat", "baidu", "jd",},
                                new int[]{R.id.text_name_ri, R.id.text_sex_ri, R.id.text_email_ri, R.id.text_phonenum_ri,
                                        R.id.text_alipay_ri, R.id.text_qq_ri, R.id.text_wechat_ri, R.id.text_bd_ri, R.id.text_jd_ri});

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result_listview.setAdapter(adapter);
                    result_num.setText("搜索到" + list.size() + "条结果");
                    result_progress.setVisibility(View.GONE);
                    result_form.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ResultActivity.this, "连接失败!", Toast.LENGTH_LONG).show();
                }
                super.handleMessage(msg);
            }


        };


    }

    ;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_tx:
                ResultActivity.this.finish();
                break;
            case R.id.home_tx:

                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                ResultActivity.this.finish();
                break;
            case R.id.refresh_tx:
                ResultActivity.this.recreate();

                break;
            case R.id.more_tx:
                showBottomDialog();
                break;
        }
    }

    private void showBottomDialog() {
        final View contentview = LayoutInflater.from(ResultActivity.this).inflate(R.layout.popupwindow, null);
        myPopupWindow = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        myPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        menuGrid = (GridView) contentview.findViewById(R.id.gridview1);
        linearbottom = (LinearLayout) findViewById(R.id.bottom_resultfirst);
        myPopupWindow.setOutsideTouchable(true);
        myPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        menuGrid.setAdapter(new SimpleAdapter(this, Tools.getList(menu_item_name, menu_item_image), R.layout.item_menu, new String[]{"text_item", "image_item"}, new int[]{R.id.text_item, R.id.image_item}));
        myPopupWindow.showAtLocation(menuGrid, Gravity.BOTTOM, 0, linearbottom.getHeight());
        setBgAlpha(0.5f);
        myPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBgAlpha(1f);
            }
        });
        menuGrid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_MENU && keyEvent.getRepeatCount() == 0 &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (myPopupWindow != null && myPopupWindow.isShowing()) {
                        myPopupWindow.dismiss();
                        setBgAlpha(1f);
                    }
                    return true;
                }
                return false;
            }
        });
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(ResultActivity.this, CollectActivity.class);//跳转到收藏界面并传递数据
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(ResultActivity.this, HistoryActivity.class);//跳转到历史界面并传递数据
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(ResultActivity.this, DownloadActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(ResultActivity.this, SetActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        ResultActivity.this.finish();
                        break;

                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
            if (myPopupWindow != null && !myPopupWindow.isShowing()) {
                linearbottom = (LinearLayout) findViewById(R.id.bottom_resultfirst);
                myPopupWindow.showAtLocation(menuGrid, Gravity.BOTTOM, 0, linearbottom.getHeight());
                setBgAlpha(0.5f);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setBgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
