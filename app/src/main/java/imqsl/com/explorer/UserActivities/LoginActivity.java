package imqsl.com.explorer.UserActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import imqsl.com.explorer.R;
import imqsl.com.explorer.Tools;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private Button reg_bt;
    private Button login_bt;
    private ProgressBar loginprogressbar = null;
    private View login_form;
    String result = null;
    String url = "http://192.168.1.152:8080/BigdataHandler/CheckUser";
    Handler handle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserView = (AutoCompleteTextView) findViewById(R.id.username_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        reg_bt = (Button) findViewById(R.id.register_bt);
        login_bt = (Button) findViewById(R.id.login_bt);
        loginprogressbar = (ProgressBar) findViewById(R.id.login_progress);
        login_form = findViewById(R.id.login_form);
        reg_bt.setOnClickListener(this);
        login_bt.setOnClickListener(this);
        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                System.out.println("result:" + result);
                switch (result) {
                    case "succeed":
                        loginprogressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("islogin", true);
                        editor.putString("username", mUserView.getText().toString());
                        editor.apply();
                        LoginActivity.this.finish();
                        break;
                    case "failed":
                        loginprogressbar.setVisibility(View.GONE);
                        login_form.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "连接失败!", Toast.LENGTH_LONG).show();
                        loginprogressbar.setVisibility(View.GONE);
                        break;
                }

                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_bt:

            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.login_bt:
                if (!Tools.isOnline(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "网络不可用,请检查手机是否联网", Toast.LENGTH_LONG).show();
                }else{
                loginprogressbar.setVisibility(View.VISIBLE);
                    login_form.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            checkUser(mUserView.getText().toString(), mPasswordView.getText().toString());
                            Message msg = handle.obtainMessage();
                            handle.sendMessage(msg);
                        }
                    }).start();
                }
                break;

        }
    }

    private void checkUser(String username, String password) {
        String param = "username=" + username + "&password=" + password;
        result = Tools.getJsonArray(url, param);
        System.out.println(result);

    }
}

