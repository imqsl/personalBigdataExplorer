package imqsl.com.explorer.UserActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import imqsl.com.explorer.R;

public class DownloadActivity extends AppCompatActivity {
    TextView textback=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        textback = (TextView) findViewById(R.id.download_back);
        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadActivity.this.finish();
            }
        });

    }
}
