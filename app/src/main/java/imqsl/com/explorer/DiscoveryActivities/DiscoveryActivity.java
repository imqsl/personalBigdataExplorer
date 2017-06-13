package imqsl.com.explorer.DiscoveryActivities;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import imqsl.com.explorer.OpenHelper;
import imqsl.com.explorer.R;
import imqsl.com.explorer.Tools;
import imqsl.com.explorer.UserActivities.CollectActivity;
import imqsl.com.explorer.UserActivities.DownloadActivity;
import imqsl.com.explorer.UserActivities.HistoryActivity;
import imqsl.com.explorer.UserActivities.SetActivity;

public class DiscoveryActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView backtv = null;
    private TextView forwardtv = null;
    private TextView hometv = null;
    private TextView refresh_tx = null;
    private TextView moretv = null;
    private String url = null;
    private WebView webView1 = null;
    private PopupWindow myPopupWindow = null;
    private GridView menuGrid = null;
    private GridView shareGrid = null;
    private LinearLayout linearbottom = null;
    private ProgressBar progressBar = null;
    private OpenHelper openHelper = null;
    private Cursor cursor_collect = null;
    private Cursor cursor_history = null;
    private ArrayList<String> list_name = null;
    private ArrayList<String> list_url = null;
    private ArrayList<String> list_name_history = null;
    private ArrayList<String> list_url_history= null;
    private int rawnum_collect;
    private int rawnum_history;
    private ContentValues values = null;
    private ContentValues values2 = null;
    private SQLiteDatabase database = null;
    private Boolean isExist_history=false;
    private Boolean isExist_collect=false;
    String[] menu_item_name = {"添加收藏", "收藏", "历史", "分享", "下载", "设置", "退出"};
    String[] share_item_name = {"朋友圈", "微信好友", "微博", "QQ空间", "QQ好友", "人人", "复制链接","短信","更多"};
    int[] menu_item_image = {R.drawable.add_collect, R.drawable.collect, R.drawable.history, R.drawable.share2,
            R.drawable.download, R.drawable.set, R.drawable.quit};
    int[] share_item_image={R.drawable.friend,R.drawable.wechat,R.drawable.weibo,R.drawable.qqzone,R.drawable.qq,
            R.drawable.renren,R.drawable.copy,R.drawable.sms,R.drawable.more
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        url = this.getIntent().getStringExtra("url");
        backtv = (TextView) findViewById(R.id.back_tx);
        forwardtv = (TextView) findViewById(R.id.forwward_tx);
        hometv = (TextView) findViewById(R.id.home_tx);
        refresh_tx = (TextView) findViewById(R.id.refresh_tx);
        moretv = (TextView) findViewById(R.id.more_tx);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_web);
        webView1 = (WebView) findViewById(R.id.webview1);
        openHelper = new OpenHelper(DiscoveryActivity.this, "mylist.db", null, 1);
        database = openHelper.getWritableDatabase();


        //判断是否得到了新的url
        if (url == null)
            webView1.loadUrl("http://stormzhang.com");
        else {
            webView1.loadUrl(url);
        }
        //设置是否url重载
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //查询得到历史列表的结果
                cursor_history = database.rawQuery("select * from history", null);
                list_name_history = new ArrayList<String>();
                list_url_history = new ArrayList<String>();
                rawnum_history = cursor_history.getCount();
                cursor_history.moveToFirst();
                for (int i = 0; i < rawnum_history; i++) {
                    list_url_history.add(cursor_history.getString(0));
                    list_name_history.add(cursor_history.getString(1));
                    cursor_history.moveToNext();
                }
                //如果重复了就不必添加
                for (int i=0;i<list_url_history.size();i++){
                    isExist_history=false;
                if (list_url_history.get(i).equals(webView1.getUrl())){
                    isExist_history=true;
                    break;
                }
                }
                //不存在才添加到数据库
                if (!isExist_history){
                        values2 = new ContentValues();
                        values2.put("url", webView1.getUrl());
                        values2.put("name", webView1.getTitle());
                       long a= database.insert("history", null, values2);
                }


                        view.loadUrl(url);


                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //设置进度改变的时候做出动作
        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

        backtv.setOnClickListener(this);
        forwardtv.setOnClickListener(this);
        hometv.setOnClickListener(this);
        refresh_tx.setOnClickListener(this);
        moretv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_tx:
                if (webView1.canGoBack()) {
                    webView1.goBack();
                } else {
                    DiscoveryActivity.this.finish();
                }
                break;
            case R.id.forwward_tx:
                if (webView1.canGoForward()) {
                    webView1.goForward();
                }
                break;
            case R.id.home_tx:
                DiscoveryActivity.this.finish();
                break;
            case R.id.refresh_tx:
                webView1.reload();
                break;
            case R.id.more_tx:
                showMoreWIndow();
                break;
        }
    }
    //创建popupwindow弹出框
    private void showMoreWIndow() {
        final View contentview = LayoutInflater.from(DiscoveryActivity.this).inflate(R.layout.popupwindow, null);
        myPopupWindow = new PopupWindow(contentview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        myPopupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        menuGrid = (GridView) contentview.findViewById(R.id.gridview1);
        linearbottom = (LinearLayout) findViewById(R.id.bottom_discovery);
        int a = linearbottom.getHeight();//得到该linearbottom的高度
        myPopupWindow.setOutsideTouchable(true);//设置外部时候可以点击
        myPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        menuGrid.setAdapter(new SimpleAdapter(this, Tools.getList(menu_item_name, menu_item_image), R.layout.item_menu, new String[]{"text_item", "image_item"}, new int[]{R.id.text_item, R.id.image_item}));
        myPopupWindow.showAtLocation(menuGrid, Gravity.BOTTOM, 0, a);//设置显示位置并显示出来
        setBgAlpha(0.5f);
        myPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBgAlpha(1f);
            }
        });//设置退出popupwindow时候的响应事件
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//设置该popupwindow每个按钮的响应动作
                switch (i) {
                    case 0:
                        addCollect();
                        myPopupWindow.dismiss();
                        break;
                    case 1:
                        Intent intent = new Intent(DiscoveryActivity.this, CollectActivity.class);//跳转到收藏界面并传递数据
                        /*intent.putStringArrayListExtra("url", list_url);
                        intent.putStringArrayListExtra("name", list_name);*/
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(DiscoveryActivity.this, HistoryActivity.class);//跳转到历史界面并传递数据
                      /*  intent2.putStringArrayListExtra("url", list_url_history);
                        intent2.putStringArrayListExtra("name", list_name_history);*/
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.setType("text/plain");
                        intent1.putExtra(Intent.EXTRA_TITLE, "title");
                        intent1.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        intent1.putExtra(Intent.EXTRA_TEXT, webView1.getTitle());

                        Intent chooserIntent = Intent.createChooser(intent1, "Select app to share");
                        if (chooserIntent == null) {
                            return;
                        }
                        try {
                            startActivity(chooserIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(DiscoveryActivity.this, "Can't find share component to share", Toast.LENGTH_SHORT).show();
                        }
                       /* sharecontent();*/
                        break;
                    case 4:
                        Intent intent3 = new Intent(DiscoveryActivity.this, DownloadActivity.class);
                        startActivity(intent3);
                        break;
                    case 5:
                        Intent intent4 = new Intent(DiscoveryActivity.this, SetActivity.class);
                        startActivity(intent4);

                        break;
                    case 6:
                        DiscoveryActivity.this.finish();
                        break;

                }
            }
        });
        //gridview中加入按键监听
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
    }
    //分享内容
    private void sharecontent() {
        View shareview=LayoutInflater.from(DiscoveryActivity.this).inflate(R.layout.sharepopupwindow,null);

        final PopupWindow sharewindow=new PopupWindow(shareview, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        shareGrid= (GridView) shareview.findViewById(R.id.sharegrid);
        shareGrid.setAdapter(new SimpleAdapter(DiscoveryActivity.this,
                Tools.getList(share_item_name,share_item_image),
                R.layout.item_menu,
                new String[]{"text_item","image_item"},
                new int[]{R.id.text_item,R.id.image_item}));
        sharewindow.setOutsideTouchable(true);
        sharewindow.setBackgroundDrawable(new ColorDrawable());
        sharewindow.showAtLocation(shareview,Gravity.CENTER,0,0);
        TextView cancel= (TextView) shareview.findViewById(R.id.share_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharewindow.dismiss();
                Toast.makeText(DiscoveryActivity.this,"分享已取消",Toast.LENGTH_SHORT).show();
            }
        });
        myPopupWindow.dismiss();
        setBgAlpha(0.5f);
        sharewindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBgAlpha(1f);
            }
        });
        shareGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){

                }
            }
        });
    }
    //添加收藏
    private void addCollect() {
        //查询得到收藏列表的结果
        cursor_collect = database.rawQuery("select * from collect", null);
        list_name = new ArrayList<String>();
        list_url = new ArrayList<String>();
        rawnum_collect = cursor_collect.getCount();
        cursor_collect.moveToFirst();
        for (int i = 0; i < rawnum_collect; i++) {
            list_url.add(cursor_collect.getString(0));
            list_name.add(cursor_collect.getString(1));
            cursor_collect.moveToNext();
        }
        for (int i=0;i<list_url.size();i++){
            isExist_collect=false;
            if (list_url.get(i).equals(webView1.getUrl())){
                Toast.makeText(DiscoveryActivity.this,"该网页已存在!",Toast.LENGTH_SHORT).show();
                isExist_collect=true;
                break;
            }
        }
        if (!isExist_collect){
        values = new ContentValues();
        values.put("url", webView1.getUrl());
        values.put("name", webView1.getTitle());
        database.insert("collect", null, values);
        Toast.makeText(DiscoveryActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
        }
    }
    //activity中加入按键监听,点击菜单键与返回健时候做出相应
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
            if (myPopupWindow != null && !myPopupWindow.isShowing()) {
                linearbottom = (LinearLayout) findViewById(R.id.bottom_discovery);
                myPopupWindow.showAtLocation(menuGrid, Gravity.BOTTOM, 0, linearbottom.getHeight());
                setBgAlpha(0.5f);
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView1.canGoBack()) {
                webView1.goBack();
                return true;
            }


        }
        return super.onKeyDown(keyCode, event);
    }
    //设置透明度
    public void setBgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
