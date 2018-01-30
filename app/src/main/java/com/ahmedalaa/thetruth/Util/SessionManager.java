package com.ahmedalaa.thetruth.Util;

import android.content.SharedPreferences;


public class SessionManager {
    private SharedPreferences sharedPreferences;
    private String FIRST_TIME_KEY = "firstTime";

    public SessionManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME_KEY, true);

    }

    public void visited() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_KEY, false);
        editor.apply();

    }
}