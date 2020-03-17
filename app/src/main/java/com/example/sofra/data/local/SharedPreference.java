package com.example.sofra.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static final String SHARED_PREF_TAG = "sofra";
    public static final String HAS_USER_DATA_SAVED_CORRECT_TAG = "sofra";
    public static final int USER_TYPE_BUY = 1;
    public static final int USER_TYPE_SELL = 2;
    public static final String USER_TYPE_KEY = "userType";
    public static final String API_TOKEN_KEY = "apiToken";
    public static final String IMG_URL_KEY = "imgUrl";
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String PHONE_KEY = "phone";
    public static final String CITY_ID_KEY = "cityID";
    public static final String REGION_ID_KEY = "regionId";
    //the following are additional keys for restaurant users
    public static final String DELIVERY_CHARGE_KEY = "deliveryCharge";
    public static final String DELIVERY_TIME_KEY = "deliveryTime";
    public static final String MIN_CHARGE_KEY = "minCharge";
    public static final String WHATSAPP_KEY = "whatsApp";
    public static final String STATUS_KEY = "status";








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
