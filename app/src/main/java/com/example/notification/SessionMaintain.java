package com.example.notification;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SessionMaintain {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String THEME = "theme";
    SessionMaintain(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("notify",0);
        editor = sharedPreferences.edit();
    }

    public void changeTheme(Boolean b)
    {
        editor.putBoolean(THEME,b);
        editor.commit();
    }

    public Boolean getTheme(String key)
    {
        return sharedPreferences.getBoolean(key,false);
    }




}
