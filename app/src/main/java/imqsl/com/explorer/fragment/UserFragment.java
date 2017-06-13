package imqsl.com.explorer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import imqsl.com.explorer.R;
import imqsl.com.explorer.UserActivities.CollectActivity;
import imqsl.com.explorer.UserActivities.DownloadActivity;
import imqsl.com.explorer.UserActivities.FeedbackActivity;
import imqsl.com.explorer.UserActivities.HistoryActivity;
import imqsl.com.explorer.UserActivities.LoginActivity;
import imqsl.com.explorer.UserActivities.SetActivity;


public class UserFragment extends Fragment implements View.OnClickListener {
    TextView happytext = null;
    LinearLayout collecttext = null;
    LinearLayout historytext = null;
    LinearLayout downloadtext = null;
    LinearLayout feedbacktext = null;
    TextView logintoptv = null;
    LinearLayout settext = null;
    SharedPreferences pref;
    String username;
    boolean islogin=false;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        happytext = (TextView) view.findViewById(R.id.happy_text);
        happytext.setOnClickListener(this);
        collecttext = (LinearLayout) view.findViewById(R.id.collect_text);
        collecttext.setOnClickListener(this);
        historytext = (LinearLayout) view.findViewById(R.id.history_text);
        historytext.setOnClickListener(this);
        downloadtext = (LinearLayout) view.findViewById(R.id.download_text);
        downloadtext.setOnClickListener(this);
        feedbacktext = (LinearLayout) view.findViewById(R.id.feedback_text);
        feedbacktext.setOnClickListener(this);
        settext = (LinearLayout) view.findViewById(R.id.set_text);
        logintoptv = (TextView) view.findViewById(R.id.logintoptv);

        pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        username=pref.getString("username","");
        islogin=pref.getBoolean("islogin",false);
        if (islogin){
            logintoptv.setText(username);
            happytext.setClickable(false);
            view.refreshDrawableState();
        }


        settext.setOnClickListener(this);
        return view;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.happy_text:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.collect_text:
                Intent intent2 = new Intent(getActivity(), CollectActivity.class);
                startActivity(intent2);
                break;
            case R.id.history_text:
                Intent intent3 = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.download_text:
                Intent intent4 = new Intent(getActivity(), DownloadActivity.class);
                startActivity(intent4);
                break;
            case R.id.feedback_text:
                Intent intent5 = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent5);
                break;
            case R.id.set_text:
                Intent intent6 = new Intent(getActivity(), SetActivity.class);
                startActivity(intent6);
                break;

        }
    }
}
