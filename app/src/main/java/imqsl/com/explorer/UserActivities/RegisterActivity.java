package imqsl.com.explorer.UserActivities;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import imqsl.com.explorer.R;
import imqsl.com.explorer.Tools;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    View register_progress = null;
    View register_form = null;
    private EditText username;
    private EditText password;
    private EditText psw_next;
    private EditText realname;
    ;
    private EditText email;
    private EditText phone;
    private Button reg_btn;
    private ImageView iv;
    private String getUsername;
    private String getPsw;
    private String getPsw_next;
    private String emailtext;
    private String realnametext;
    private String phonetext;
    private String result;
    final String url = "http://192.168.1.152:8080/BigdataHandler/InsertUser";
    private Handler mHandler = null;

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.et_register_userName);
        password = (EditText) findViewById(R.id.et_register_password);
        psw_next = (EditText) findViewById(R.id.et_register_password2);
        realname = (EditText) findViewById(R.id.et_realname);
        email = (EditText) findViewById(R.id.et_register_email);
        phone = (EditText) findViewById(R.id.et_register_phone);
        reg_btn = (Button) findViewById(R.id.btn_register);
        register_progress = findViewById(R.id.register_progress);
        register_form = findViewById(R.id.register_form);
        iv = (ImageView) findViewById(R.id.reg_back);
        reg_btn.setOnClickListener(this);
        iv.setOnClickListener(this);

        super.onCreate(savedInstanceState);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (result) {
                    case "succeed":
                        register_progress.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        RegisterActivity.this.finish();
                        break;
                    case "failed":
                        register_progress.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        register_progress.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_back:
                RegisterActivity.this.finish();
                break;
            case R.id.btn_register:
                getUsername = username.getText().toString();
                getPsw = password.getText().toString();
                getPsw_next = psw_next.getText().toString();
                realnametext = realname.getText().toString();
                phonetext = phone.getText().toString();
                emailtext = email.getText().toString();

                if ("".equals(getUsername) || getUsername == "") {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (getPsw == "" || getPsw_next == "" || "".equals(getPsw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (checkPassword(getPsw, getPsw_next) == true) {


                    if (!Tools.isOnline(RegisterActivity.this)) {

                        Toast.makeText(RegisterActivity.this, "网络不可用,请检查手机是否联网", Toast.LENGTH_LONG).show();
                    } else {
                        register_form.setVisibility(View.GONE);
                        register_progress.setVisibility(View.VISIBLE);
                        reg(getUsername, getPsw, realnametext, phonetext, emailtext);
                    }
                } else if (checkPassword(getPsw, getPsw_next) == false) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    public boolean checkPassword(String psw, String psw_next) {
        if (psw.equals(psw_next)) {
            return true;
        } else {
            return false;
        }

    }

    public void reg(final String username, final String password, final String realname, final String phone, final String email) {


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String param = "username=" + username + "&password=" + password + "&realname="
                            + URLEncoder.encode(realname, "utf-8") + "&phone=" +
                            phone + "&email=" + email;
                    result = Tools.getJsonArray(url, param);
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}