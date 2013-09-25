package com.brianantonelli.babymon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        ((EditText)v.findViewById(R.id.serverURL)).setText(activity.getServerAddress());

        Button saveButton = (Button) v.findViewById(R.id.saveSettings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setServerAddress(((EditText)v.findViewById(R.id.serverURL)).getText().toString());
            }
        });

        return v;
    }
}
