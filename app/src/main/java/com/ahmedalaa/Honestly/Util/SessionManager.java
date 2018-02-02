package com.ahmedalaa.Honestly.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ahmedalaa.Honestly.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class SessionManager {
    private SharedPreferences sharedPreferences;
    private String FIRST_TIME_KEY = "firstTime";
    private String Notif_KEY = "Notif_KEY";
    private String SOUND_KEY = "SOUND_KEY";
    private String USER = "USER_KEY";

    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("App", Context.MODE_PRIVATE);
    }

    public void setNotfication(Boolean is) {
        sharedPreferences.edit().putBoolean(Notif_KEY, is).apply();
    }

    public void setNotficationSound(Boolean is) {
        sharedPreferences.edit().putBoolean(SOUND_KEY, is).apply();

    }

    public Boolean isNotficationEnable() {
        return sharedPreferences.getBoolean(Notif_KEY, true);
    }

    public Boolean isNotficationSoundEnable() {
        return sharedPreferences.getBoolean(SOUND_KEY, true);
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME_KEY, true);

    }

    public void visited() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_KEY, false);
        editor.apply();

    }

    public User getUserData() {
        User user = null;

        if (sharedPreferences.contains(USER)) {


            Type type = new TypeToken<User>() {
            }.getType();
            user = new Gson().fromJson(sharedPreferences.getString(USER, ""), type);
        }
        return user;

    }

    public void setUserData(User userData) {
        sharedPreferences.edit().putString(USER, new Gson().toJson(userData)).apply();
    }

}