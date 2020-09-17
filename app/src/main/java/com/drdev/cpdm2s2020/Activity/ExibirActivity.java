package com.drdev.cpdm2s2020.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.RecyclerAdapterVisualizar;

import java.util.ArrayList;
import java.util.Calendar;

public class ExibirActivity extends AppCompatActivity {


    private ArrayList<TarefaModel> tarefas;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private DataBaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir);

        db = new DataBaseHelper(getApplicationContext());

        tarefas = db.GetTarefasList();

        recyclerView = findViewById(R.id.VisualizarRecyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        RecyclerAdapterVisualizar adapter = new RecyclerAdapterVisualizar(this, tarefas);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }




}