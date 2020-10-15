package com.drdev.cpdm2s2020.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;

import java.util.HashMap;

public class ReminderBroadcast extends BroadcastReceiver {

    public static HashMap<Integer, TarefaModel> mapTarefas;

    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO Criar uma validação para ver se o id que esta sendo chamado, bate as informacoes com o mapTarefas
        //TODO Substituir as informacoes Pela informacao da Tarefa

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyCPDM")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }

    private static void getAlertData(){

    }

}
