package imqsl.com.explorer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import imqsl.com.explorer.DiscoveryActivities.DiscoveryActivity;
import imqsl.com.explorer.fragment.HomeFragment;
import imqsl.com.explorer.fragment.UserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    HomeFragment homefram = null;
    UserFragment userfrag = null;
    LinearLayout homeLayout = null;
    LinearLayout bulbLayout = null;
    LinearLayout userLayout = null;
    TextView home_pic=null;
    TextView user_pic=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setTabSelection(0);


    }

    private void initView() {
        homeLayout = (LinearLayout) findViewById(R.id.bt_home);
        bulbLayout = (LinearLayout) findViewById(R.id.bt_bulb);
        userLayout = (LinearLayout) findViewById(R.id.bt_user);
        home_pic= (TextView) findViewById(R.id.home_pic);
        user_pic= (TextView) findViewById(R.id.user_pic);
        homeLayout.setOnClickListener(this);
        userLayout.setOnClickListener(this);
        bulbLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_home:
                setTabSelection(0);
                break;
            case R.id.bt_bulb:
                setTabSelection(1);
                break;
            case R.id.bt_user:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int i) {
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction transation = fm.beginTransaction();
        switch (i) {
            case 0:
                if (userfrag != null){
                    user_pic.setBackgroundResource(R.drawable.user_unclicked);
                    transation.hide(userfrag);
                }
                homefram = new HomeFragment();
                home_pic.setBackgroundResource(R.drawable.home_clicked);
                transation.replace(R.id.id_content, homefram);
                break;
            case 1:
                if (!Tools.isOnline(MainActivity.this)){
                    Toast.makeText(MainActivity.this, "网络不可用,请检查手机是否联网", Toast.LENGTH_LONG).show();
                }else{
                Intent intent = new Intent(MainActivity.this, DiscoveryActivity.class);
                startActivity(intent);
                }
                break;
            case 2:
                if (homefram != null){
                    home_pic.setBackgroundResource(R.drawable.home_unclicked);

                    transation.hide(homefram);
                }
                userfrag = new UserFragment();
                user_pic.setBackgroundResource(R.drawable.user);
                transation.replace(R.id.id_content, userfrag);
                break;

        }
        transation.commit();
    }

    @Override
    protected void onRestart() {
        MainActivity.this.recreate();
        super.onRestart();
    }
}
