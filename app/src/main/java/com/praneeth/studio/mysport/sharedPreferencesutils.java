package com.praneeth.studio.mysport;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

public class sharedPreferencesutils {

    public static void setCurrentTeamId(Context context,String teamkey)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("teamid",teamkey);
        editor.commit();
    }
    public static String getCurrentTeamId(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        return preferences.getString("teamid","");
    }

    public static void setCurrentName(Context context,String name)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",name);
        editor.commit();
    }
    public static String getCurrentName(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        return preferences.getString("name","");
    }





}
