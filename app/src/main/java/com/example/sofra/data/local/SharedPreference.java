package com.example.sofra.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static final String SHARED_PREF_TAG = "sofra";
    public static final String USER_TYPE_KEY = "userType";
    public static final int USER_TYPE_BUY = 1;
    public static final int USER_TYPE_SELL = 2;
    public static final String API_TOKEN_KEY = "apiToken";




    private static SharedPreferences sharedPreferences;


    public static void setSharedPreference(Context activity){

        if (sharedPreferences==null){

            sharedPreferences = activity.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        }

    }

    public static void saveData(Context activity , String key,String Value){

        setSharedPreference(activity);

        if (sharedPreferences!=null){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key,Value);
            editor.apply();
        }


    }
    public static void saveData(Context activity , String key,int Value){

        setSharedPreference(activity);

        if (sharedPreferences!=null){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key,Value);
            editor.apply();
        }


    }
    public static void saveData(Context activity , String key,boolean Value){

        setSharedPreference(activity);

        if (sharedPreferences!=null){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key,Value);
            editor.apply();
        }


    }


    public static String loadString(Context activity,String key) {

        setSharedPreference(activity);

        return sharedPreferences.getString(key,null);
    }

    public static int loadInt(Context activity,String key) {

        setSharedPreference(activity);

        return sharedPreferences.getInt(key,0);
    }
    public static boolean loadBoolean(Context activity,String key) {

        setSharedPreference(activity);

        return sharedPreferences.getBoolean(key,false);
    }
}
