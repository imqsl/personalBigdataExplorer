package imqsl.com.explorer.UserActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import imqsl.com.explorer.R;

public class SetActivity extends AppCompatActivity {
TextView textback=null;
    Button quit=null;
    SharedPreferences pref;
    boolean islogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        textback = (TextView) findViewById(R.id.set_back);
        quit= (Button) findViewById(R.id.quitlogin);
        pref=getSharedPreferences("login",MODE_PRIVATE);
        islogin=pref.getBoolean("islogin",false);
        if (islogin){
            quit.setVisibility(View.VISIBLE);
            quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(SetActivity.this).setMessage("确定要退出登录吗?").setNegativeButton("退出登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences.Editor editor=pref.edit();
                            editor.clear();
                            editor.apply();
                            dialogInterface.dismiss();
                            quit.setVisibility(View.GONE);
                            SetActivity.this.recreate();
                        }
                    }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setTitle("退出登录").create().show();
                }
            });
        }

        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetActivity.this.finish();
            }
        });
    }
}
