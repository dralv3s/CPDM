package com.drdev.cpdm2s2020.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.drdev.cpdm2s2020.Model.TarefaModel;

import java.util.Comparator;
import java.util.List;

public class NotifService {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void notificationService(Context context, AlarmManager alarmManager){

        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        List<TarefaModel> listTarefas = dataBaseHelper.GetTarefas();

        listTarefas.sort(new Comparator<TarefaModel>() {
            @Override
            public int compare(TarefaModel o1, TarefaModel o2) {
                return o1.DataEntrega.compareTo(o2.DataEntrega);
            }
        });

        for (TarefaModel tarefa:
             listTarefas) {
            if(ReminderBroadcast.mapTarefas.containsKey(tarefa.IdTarefa)){
                ReminderBroadcast.mapTarefas.replace(tarefa.IdTarefa, tarefa);
            }else{
                ReminderBroadcast.mapTarefas.put(tarefa.IdTarefa, tarefa);
            }
        }

        //TODO Olhar o calendário para o momento de notificar, e criar um RTC_WAKEUP para o momento.

    }
}
