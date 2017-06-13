package imqsl.com.explorer.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import imqsl.com.explorer.R;
import imqsl.com.explorer.SearchActivities.SearchActivity;


public class HomeFragment extends Fragment implements View.OnClickListener {
    EditText indexEdit=null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        indexEdit=(EditText)view.findViewById(R.id.homeSearch_text);
        indexEdit.setOnClickListener(this);
        return view;

    }

    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),SearchActivity.class);
        startActivity(intent);
    }

}
