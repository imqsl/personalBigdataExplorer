package imqsl.com.explorer.SearchActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import imqsl.com.explorer.R;
import imqsl.com.explorer.Tools;

public class SearchActivity extends AppCompatActivity{
    EditText searchet = null;
    TextView searchtv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchet = (EditText) findViewById(R.id.searchedit);
        searchtv = (TextView) findViewById(R.id.searchtv);
        searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switch (charSequence.toString()){
                    case "":searchtv.setText("取消");break;
                    default:searchtv.setText("搜索");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchtv.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (searchtv.getText().toString()){
               case "搜索":
                   if (!Tools.isOnline(SearchActivity.this)) {
                       Toast.makeText(SearchActivity.this, "网络不可用,请检查手机是否联网", Toast.LENGTH_LONG).show();
                   } else {
                   Intent intent=new Intent(SearchActivity.this,ResultActivity.class);
                   intent.putExtra("name",searchet.getText().toString());
                   startActivity(intent);
                   }

                   break;
               case "取消":
                   SearchActivity.this.finish();
           }

       }
   });



    }

    @Override
    protected void onRestart() {
        SearchActivity.this.finish();
        super.onRestart();
    }
}
