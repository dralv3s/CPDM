package com.drdev.cpdm2s2020.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drdev.cpdm2s2020.R;

public class FuncAux extends AppCompatActivity {

    public void Toast(Context ctx, String msg, int leng){
        Toast.makeText(ctx, msg, leng == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }


    public void ClearPrefs(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();

        editor.commit();
    }


    public void SaveUserPrefs(Context context, String key, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);

        editor.commit();
    }


    public void SaveUserPrefs(Context context, String key, boolean value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(key, value);

        editor.commit();
    }

    public void SaveUserPrefs(Context context, String key, int value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(key, value);

        editor.commit();
    }



    public String GetUserPrefsString(Context context, String key, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        return sharedPref.getString(key, value);
    }

}
