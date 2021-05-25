package com.mobcash.android.Services;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedConstant {
    public static final String PREF_NAME = "My Preferences";

    @SuppressWarnings("deprecation")
    public static final int MODE = Context.MODE_WORLD_WRITEABLE;

    public static final String USER_ID = "USER_ID_NEW";
    public static final String USER_NAME = "USER_NAME";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String USER_TYPE = "usertype";
    public static final String USER_STATE = "userstate";
    public static String FIREBASE_TOKEN = "firebase";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 0;
    public static final String SHARED_PREF = "SHAREDPREF";



    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }




    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setSharedPreferenceLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setSharedPreferenceFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.apply();
    }


    public static boolean setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
        return value;
    }




    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getString(key, defValue);
    }


    public static int getSharedPreferenceInt(Context context, String key, int defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getInt(key, defValue);
    }
    public static long getSharedPreferenceLong(Context context, String key, long defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getLong(key, defValue);
    }

    public static float getSharedPreferenceFloat(Context context, String key, float defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getFloat(key, defValue);
    }

    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
        return settings.getBoolean(key, defValue);
    }


    public static void logout(Context ctx) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }


}
