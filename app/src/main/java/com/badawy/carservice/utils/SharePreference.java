package com.badawy.carservice.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

//AhmedRabie
//sharepreference
public class SharePreference {

    public static boolean SaveEmail (String email , Context context)

    {
        SharedPreferences SharePref = PreferenceManager.getDefaultSharedPreferences( context);
        SharedPreferences.Editor PrefEditor =SharePref.edit();
        PrefEditor.putString(Constants.Key_Password,email);
        PrefEditor.apply();
        return true;
    }

    public static String GetEmail (Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Constants.Key_Email,null);
    }

    public static boolean SavePassword (String Password , Context context)
    {
        SharedPreferences SharePref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor PrefEditor =SharePref.edit();
        PrefEditor.putString(Constants.Key_Password,Password);
        PrefEditor.apply();
        return true;
    }

    public static String GetPassword (Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Constants.Key_Password,null);
    }


}
