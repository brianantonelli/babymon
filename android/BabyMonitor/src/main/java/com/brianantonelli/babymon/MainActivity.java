package com.brianantonelli.babymon;

import java.util.Locale;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    public static final String PREFS_NAME = "BabyMonPrefsFile";
    private static final String PREFS_KEY_SERVER = "serverAddress";
    private static final String PREFS_KEY_UN = "serverUN";
    private static final String PREFS_KEY_PW = "serverPW";
    private String serverAddress;
    private String serverUsername;
    private String serverPassword;
    private WebViewFragment webviewFragment;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Restore preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        serverAddress = prefs.getString(PREFS_KEY_SERVER, "10.0.1.31");
        serverUsername = prefs.getString(PREFS_KEY_UN, "user");
        serverPassword = prefs.getString(PREFS_KEY_PW, "pass");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public String getServerAddress(){
        return serverAddress;
    }

    public void setServerAddress(String serverAddress){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putString(PREFS_KEY_SERVER, serverAddress).apply();
        this.serverAddress = serverAddress;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putString(PREFS_KEY_UN, serverUsername).apply();
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putString(PREFS_KEY_PW, serverPassword).apply();
        this.serverPassword = serverPassword;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            if(position == 0){
                webviewFragment = new WebViewFragment();
                return webviewFragment;
            }
            else if(position == 1){
                return new MusicFragment();
            }
            else if(position == 2){
                return new SettingsFragment();
            }
            else{
                Fragment fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment.setArguments(args);
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public void onDestroy(){
        if(webviewFragment != null){
            webviewFragment.stopAudio();
        }
        super.onDestroy();
    }

    public void onResume(){
        if(webviewFragment != null){
            webviewFragment.startAudio();
        }
        super.onResume();
    }

    public void onBackPressed(){
        if(webviewFragment != null){
            webviewFragment.stopAudio();
        }
        super.onBackPressed();
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
