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

    private MainActivity getActivty(){
        return (MainActivity) getActivity();
    }

    private SharedPreferences getPrefs(){
        return getActivty().getSharedPreferences(MainActivity.PREFS_NAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);

        ((EditText)v.findViewById(R.id.serverURL)).setText(getActivty().getServerAddress());

        Button saveButton = (Button) v.findViewById(R.id.saveSettings);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO get and set URL. also we should hide the path from the user
            }
        });

        return v;
    }
}
