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

public class ExibirActivity extends AppCompatActivity {
    private ArrayList<TarefaModel> tarefas;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DataBaseHelper dbHelper;
    private int action = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir);
        Init();
        GetExtras();
        GetData();
        SetRecyclerView();
    }

    private void GetExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            action = extras.getInt(getString(R.string.Action));
    }

    private void GetData() {
        tarefas = dbHelper.GetTarefas();
    }

    private void SetRecyclerView() {
        recyclerView = findViewById(R.id.VisualizarRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapterVisualizar adapter = new RecyclerAdapterVisualizar(this, tarefas, action, dbHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void Init() {
        dbHelper = new DataBaseHelper(getApplicationContext());
    }
}