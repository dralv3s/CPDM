package com.drdev.cpdm2s2020.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.drdev.cpdm2s2020.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FuncAux extends AppCompatActivity {



    private Context context;

    public FuncAux(Context _context) {
        context = _context;
    }

    public void Toast(String msg, int leng){
        Toast.makeText(context, msg, leng == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public void ClearPrefs(){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();

        editor.commit();
    }


    public void SaveUserPrefs(String key, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);

        editor.commit();
    }


    public void SaveUserPrefs( String key, boolean value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(key, value);

        editor.commit();
    }

    public void SaveUserPrefs( String key, int value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(key, value);

        editor.commit();
    }



    public String GetUserPrefsString( String key, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.UserSharedPrefs), context.MODE_PRIVATE);

        return sharedPref.getString(key, value);
    }


    public Date formatDateTime(String timeToFormat) {

        String timeFormated = timeToFormat.replace("/", "-");

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        if (timeFormated != null) {
            try {
                date = iso8601Format.parse(timeFormated);
            } catch (ParseException e) {
                date = null;
            }

//            if (date != null) {
//                long when = date.getTime();
//                int flags = 0;
//                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
//                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
//                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
//                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
//
//                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
//                        when + TimeZone.getDefault().getOffset(when), flags);
//            }
        }
        return date;
    }
}
