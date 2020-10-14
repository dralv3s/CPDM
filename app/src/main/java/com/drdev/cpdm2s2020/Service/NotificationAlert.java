package com.drdev.cpdm2s2020.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class NotificationAlert {

    private CharSequence name = "CPDM_Channel";
    private String description = "Chanel for Notifications";
    private int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationChannel channel;

    private Calendar notifDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationAlert(CharSequence name, String description, String channelId, Calendar notifDate){
        this.name = name;
        this.description = description;
        this.notifDate = notifDate;

        channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);
    }

    public void setAlert(){

    }



}
