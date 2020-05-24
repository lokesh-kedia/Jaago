package com.jaago.jaago;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPref;
    public SharedPref(Context context){
        mySharedPref=context.getSharedPreferences("filename",Context.MODE_PRIVATE);

    }
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public boolean loadNightModeState(){
        Boolean state = mySharedPref.getBoolean("NightMode",false);
        return state;
    }
}
