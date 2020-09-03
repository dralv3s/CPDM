package com.drdev.cpdm2s2020.util;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FuncAux extends AppCompatActivity {

    public void Toast(Context ctx, String msg, int leng){
        Toast.makeText(ctx, msg, leng == 1 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
