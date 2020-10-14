package com.drdev.cpdm2s2020.Service;

import android.app.AlarmManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.drdev.cpdm2s2020.Model.TarefaModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NotifService {

    public static AlarmManager alarmManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void notificationService(){
        List<TarefaModel> alertList = new DataBaseHelper(getApplicationContext()).GetTarefasList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alertList.sort(new Comparator<TarefaModel>() {
                @Override
                public int compare(TarefaModel o1, TarefaModel o2) {
                    return o1.DataEntrega.compareTo(o2.DataEntrega);
                }
            });
        }


    }

}
