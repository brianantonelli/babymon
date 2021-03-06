package com.brianantonelli.babymon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by monkeymojo on 9/15/13.
 */
public class SettingsFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.settings, container, false);
        final MainActivity activity = (MainActivity)getActivity();

        setText(v, R.id.serverURL, activity.getServerAddress());
        setText(v, R.id.serverUN, activity.getServerUsername());
        setText(v, R.id.serverPW, activity.getServerPassword());

        Button saveButton = (Button) v.findViewById(R.id.saveSettings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setServerAddress(getText(v, R.id.serverURL));
                activity.setServerUsername(getText(v, R.id.serverUN));
                activity.setServerPassword(getText(v, R.id.serverPW));
                Toast.makeText(activity, "Settings Saved :)", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setText(View view, int id, String value){
        ((EditText)view.findViewById(id)).setText(value);
    }

    private String getText(View view, int id){
        return ((EditText)view.findViewById(id)).getText().toString();
    }
}
