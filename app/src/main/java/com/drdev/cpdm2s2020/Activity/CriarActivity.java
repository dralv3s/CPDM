package com.drdev.cpdm2s2020.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CriarActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    private DataBaseHelper dbHelper;
    private FuncAux func;
    private TarefaModel model;

    private TextInputLayout tituloTarefa;
    private TextInputLayout aliasTarefa;
    private TextInputLayout descricaoTarefa;
    private TextInputLayout notificar;
    private TextInputLayout valorNota;
    private TextInputLayout dataEntrega;

    private CardView cardViewNotificar;
    private SwitchMaterial switchNotificar;

    private TextInputEditText dataEntregaTextField;

    private Button insertBT;

    private String[] iconeArr;

    private Integer iconeTarefaSelected = R.drawable.physics;
    private Integer iconeTarefaSelectedIndex = 4;

    private ImageView iconeTarefaImageView;
    private TextView clickIcon;

    private boolean isOpace = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar);

        iconeArr = new String[]{
          "Prova", "Trabalho", "Apresentação", "Extra", "Outros"
        };

        func = new FuncAux(CriarActivity.this);

        dbHelper = new DataBaseHelper(getApplicationContext());

        model = new TarefaModel();

        cardViewNotificar = findViewById(R.id.CardViewNotificar);
        switchNotificar = findViewById(R.id.switchNotificar);
        tituloTarefa = findViewById(R.id.InputTituloTarefa);
        aliasTarefa = findViewById(R.id.InputAliasTarefa);
        descricaoTarefa = findViewById(R.id.InputDescricaoTarefa);
        notificar = findViewById(R.id.InputNotificarTarefa);
        valorNota = findViewById(R.id.InputValorNota);
        dataEntrega = findViewById(R.id.InputDataEntregaTarefa);
        dataEntregaTextField = findViewById(R.id.InputDataEntregaTextField);
        insertBT = findViewById(R.id.SalvarTarefaButton);
        iconeTarefaImageView = findViewById(R.id.ImageViewIconeCriarTarefa);

        switchNotificar.setChecked(false);
        //cardViewNotificar.setVisibility(View.GONE );

        switchNotificar.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardViewNotificar.setVisibility(isChecked ? View.VISIBLE : View.GONE );
            }
        });



        insertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InsertStuff();
            }
        });

        iconeTarefaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ct = CriarActivity.this;
                ct.setTheme(R.style.MaterialTheme);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ct);
                builder.setTitle("Selecione...");

                builder.setSingleChoiceItems(iconeArr, iconeTarefaSelectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iconeTarefaSelectedIndex = which;
                        iconeTarefaSelected = GetDrawable(iconeTarefaSelectedIndex);
                        iconeTarefaImageView.setImageResource(iconeTarefaSelected);
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        dataEntregaTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(CriarActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDataEntrega();
        }
    };

    private Integer GetDrawable(Integer selection){

        Integer retorno = R.drawable.nightsky;
        switch (selection){
            case 0:
                //"Prova"
                retorno = R.drawable.test;
                break;
            case 1:
                //Trabalho
                retorno = R.drawable.abacus;
                break;
            case 2:
                //Apresentação
                retorno = R.drawable.projector;
                break;
            case 3:
                //Extra
                retorno = R.drawable.medal;
                break;
            case 4:
                //Outros
                retorno = R.drawable.physics;
                break;
        };
        return retorno;
    };

    private void updateDataEntrega() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dataEntregaTextField.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean ValidarCampos(){

        boolean retorno = true;

        if (model.TituloTarefa.equals("")){
            tituloTarefa.setError("Titulo é obrigatório");
            retorno = false;
        }

        if (model.DescricaoTarefa.equals("")){
            descricaoTarefa.setError("Descrição é obrigatória");
            retorno = false;
        }

        if (dataEntrega.getEditText().getText().toString().equals("")){
            dataEntrega.setError("Data de entrega é obrigatória");
            retorno = false;
        }

        return retorno;
    }

    private void InsertStuff(){
        model.TituloTarefa = tituloTarefa.getEditText().getText().toString();
        model.DescricaoTarefa = descricaoTarefa.getEditText().getText().toString();

        if(!ValidarCampos())
            return;

        model.AliasTarefa = aliasTarefa.getEditText().getText().toString();
        model.DataEntrega = dataEntrega.getEditText().getText().toString();
        model.Notificar = notificar.getEditText().getText().toString();
        model.ValorNota = Float.parseFloat(valorNota.getEditText().getText().toString());
        model.IconeTarefa = iconeTarefaSelected;

        boolean success = dbHelper.SalvarTarefa(model);

        if (success){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);

            startActivity(new Intent(CriarActivity.this, HomeActivity.class));
            func.Toast("Registro incluso com sucesso!", Toast.LENGTH_LONG);
        }else{
            func.Toast("Não foi possivel incluir o registro", Toast.LENGTH_LONG);
        }
    }
}