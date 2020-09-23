package com.drdev.cpdm2s2020.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.drdev.cpdm2s2020.Service.RecyclerAdapterVisualizar;

import java.io.Serializable;
import java.util.ArrayList;

public class ExibirActivity extends AppCompatActivity implements  RecyclerAdapterVisualizar.OnTarefaListener, Serializable {
    private FuncAux func;

    private ArrayList<TarefaModel> tarefas;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir);
        func = new FuncAux(ExibirActivity.this);
        db = new DataBaseHelper(getApplicationContext());
        tarefas = db.GetTarefasList();
        recyclerView = findViewById(R.id.VisualizarRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapterVisualizar adapter = new RecyclerAdapterVisualizar(this, tarefas, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onTarefaClick(int position) {
        func.Toast("Position = " + position, Toast.LENGTH_LONG);

        TarefaModel model = tarefas.get(position);

        Intent intent = new Intent(ExibirActivity.this, EditarActivity.class);

        //intent.putExtra("", model);

        startActivity(intent);
    }
}