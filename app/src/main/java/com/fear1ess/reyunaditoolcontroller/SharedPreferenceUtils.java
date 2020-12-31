package com.fear1ess.reyunaditoolcontroller;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtils {
    public static String getSharedPreferencesValue(String spName, String key) {
        Context cxt = AdiToolControllerApp.getAppContext();
        SharedPreferences configuration = cxt.getSharedPreferences("configuration", MODE_PRIVATE);
        String address = configuration.getString("server_address", null);
        return address;
    }

    public static void setSharedPreferencesValue(String spName, String key, String value) {
        Context cxt = AdiToolControllerApp.getAppContext();
        SharedPreferences configuration = cxt.getSharedPreferences(spName, MODE_PRIVATE);
        SharedPreferences.Editor editor = configuration.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getServerConfig(int index) {
        return getSharedPreferencesValue("configuration", "server_address" + index);
    }

    public static void setServerConfig(int index, String value) {
        setSharedPreferencesValue("configuration", "server_address" + index, value);
    }

}
