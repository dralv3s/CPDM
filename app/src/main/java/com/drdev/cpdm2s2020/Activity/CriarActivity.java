package com.drdev.cpdm2s2020.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CriarActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private FuncAux func;
    private TarefaModel model;

    private TextInputLayout tituloTarefa;
    private TextInputLayout aliasTarefa;
    private TextInputLayout descricaoTarefa;
    private TextInputLayout notificar;
    private TextInputLayout valorNota;
    private TextInputLayout dataEntrega;
    private TextInputLayout iconeTarefa;

    private Date dateParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar);

        func = new FuncAux();
        dbHelper = new DataBaseHelper(getApplicationContext());

        model = new TarefaModel();


        tituloTarefa = findViewById(R.id.InputTituloTarefa);
        aliasTarefa = findViewById(R.id.InputAliasTarefa);
        descricaoTarefa = findViewById(R.id.InputDescricaoTarefa);
        notificar = findViewById(R.id.InputNotificarTarefa);
        valorNota = findViewById(R.id.InputValorNota);
        dataEntrega = findViewById(R.id.InputDataEntregaTarefa);
        iconeTarefa = findViewById(R.id.InputIconeTarefa);








        Button insertBT = findViewById(R.id.SalvarTarefaButton);

        insertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InsertStuff();
            }
        });

    }

    private void InsertStuff(){
        
        model.TituloTarefa = tituloTarefa.getEditText().getText().toString();
        model.DescricaoTarefa = descricaoTarefa.getEditText().getText().toString();
        model.AliasTarefa = aliasTarefa.getEditText().getText().toString();

        try {
            dateParse = new SimpleDateFormat("dd/MM/yyyy").parse(dataEntrega.getEditText().getText().toString());
            model.DataEntrega = dateParse;

        } catch (ParseException e) {
           func.Toast(getApplicationContext(), "Data no formato incorreto", Toast.LENGTH_LONG);
           return;
        }

        model.Notificar = notificar.getEditText().getText().toString();
        model.ValorNota = Float.parseFloat(valorNota.getEditText().getText().toString());
        model.IconeTarefa = Integer.parseInt(iconeTarefa.getEditText().getText().toString());


        boolean success = dbHelper.SalvarTarefa(model);

        if (success){
            func.Toast(getApplicationContext(), "Registro incluso com sucesso!", Toast.LENGTH_LONG);
        }else{
            func.Toast(getApplicationContext(), "Não foi possivel incluir o registro", Toast.LENGTH_LONG);
        }
    }
}