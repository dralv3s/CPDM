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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private TextInputLayout valorNota;
    private TextInputLayout dataEntrega;
    private TextInputLayout textInputLayoutFrequencia;

    private TextInputEditText dataEntregaTextField;

    private Spinner spinnerNotificacao;
    private Spinner spinnerNotificacaoRepetir;

    private CardView cardViewNotificar;

    private SwitchMaterial switchNotificar;
    private SwitchMaterial switchNotificarRepetir;

    private Button insertBT;

    private String[] iconeArr;
    private String[]  notificacaoArr;
    private String[]  notificacaoRepetirArr;
    private Integer iconeTarefaSelected = R.drawable.physics;
    private Integer iconeTarefaSelectedIndex = 4;
    private Integer action = 0;
    private Integer IdTarefa = 0;

    private ImageView iconeTarefaImageView;
    private Context contextMaterialTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar);

        model = new TarefaModel();
        func = new FuncAux(CriarActivity.this);
        dbHelper = new DataBaseHelper(getApplicationContext());

        contextMaterialTheme = CriarActivity.this;
        contextMaterialTheme.setTheme(R.style.MaterialTheme);

        PopulateArrays();
        GetObjectsFromView();
        SetListeners();
        PopulateSpinners();

        action = GetAction();

        switch(action){
            case 1:
                //Criando registro
                InitFields();
                break;
            case 2:
                //Editando registro
                PupulateFields();
                break;
            case 3:
                //Visualizando registro
                DisableFields();
                break;
            default:
                break;
        }
    }



    private void DisableFields() {
    }

    private void PupulateFields() {
    }


    private int GetAction() {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            action = Integer.parseInt(extras.getString("Action"));
            IdTarefa = Integer.parseInt(extras.getString("IdTarefa"));
        }
        return 0;
    }

    private void PopulateSpinners(){
        ArrayAdapter<String> notificacaoAdapter = new ArrayAdapter<>(CriarActivity.this, R.layout.drop_notificacao_item, notificacaoArr);
        notificacaoAdapter.setDropDownViewResource(R.layout.drop_notificacao_item);
        spinnerNotificacao.setAdapter(notificacaoAdapter);

        ArrayAdapter<String> notificacaoRepetirAdapter = new ArrayAdapter<>(CriarActivity.this, R.layout.drop_notificacao_item, notificacaoRepetirArr);
        notificacaoRepetirAdapter.setDropDownViewResource(R.layout.drop_notificacao_item);
        spinnerNotificacaoRepetir.setAdapter(notificacaoRepetirAdapter);
    }

    private void PopulateArrays(){
        iconeArr = getResources().getStringArray(R.array.iconeArr);
        notificacaoArr = getResources().getStringArray(R.array.notificacaoArr);
        notificacaoRepetirArr = getResources().getStringArray(R.array.notificacaoRepetirArr);
    }

    private void InitFields(){

        switchNotificarRepetir.setChecked(false);
        spinnerNotificacaoRepetir.setVisibility(View.GONE);
        switchNotificar.setChecked(false);
        cardViewNotificar.setVisibility(View.GONE);
        textInputLayoutFrequencia.getEditText().setText(0);
    }

    private void SetListeners() {
        dataEntregaTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new DatePickerDialog(CriarActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        iconeTarefaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(contextMaterialTheme);
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

        insertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InsertStuff();
            }
        });

        switchNotificarRepetir.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinnerNotificacaoRepetir.setVisibility(isChecked ? View.VISIBLE : View.GONE );
            }
        });

        switchNotificar.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cardViewNotificar.setVisibility(isChecked ? View.VISIBLE : View.GONE );
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

        if (tituloTarefa.getEditText().getText().toString().equals("")){
            tituloTarefa.setError("Titulo é obrigatório");
            retorno = false;
        }

        if (descricaoTarefa.getEditText().getText().toString().equals("")){
            descricaoTarefa.setError("Descrição é obrigatória");
            retorno = false;
        }

        if (dataEntrega.getEditText().getText().toString().equals("")){
            dataEntrega.setError("Data de entrega é obrigatória");
            retorno = false;
        }

        if (switchNotificar.isChecked() && textInputLayoutFrequencia.getEditText().getText().toString().equals("")){
            func.Toast("É necessario digitar a quantidade de tempo da notificação", Toast.LENGTH_LONG);
            retorno = false;
        }
        return retorno;
    }

    private void InsertStuff(){
        if(!ValidarCampos())
            return;

        model.TituloTarefa = tituloTarefa.getEditText().getText().toString();
        model.DescricaoTarefa = descricaoTarefa.getEditText().getText().toString();
        model.AliasTarefa = aliasTarefa.getEditText().getText().toString();
        model.DataEntrega = dataEntrega.getEditText().getText().toString();
        model.ValorNota = Float.parseFloat(valorNota.getEditText().getText().toString());
        model.IconeTarefa = iconeTarefaSelected;
        model.Notificar = BuildNotificacao();

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

    private String BuildNotificacao() {
        StringBuilder sb = new StringBuilder();

        if (switchNotificar.isChecked() && ValidarCampos()){
            sb.append(textInputLayoutFrequencia.getEditText().getText() + "|");
            sb.append(spinnerNotificacao.getSelectedItemPosition());

            if (switchNotificarRepetir.isChecked()){
                sb.append("|"+ spinnerNotificacaoRepetir.getSelectedItemPosition());
            }
        }
        return sb.toString();
    }

    private void GetObjectsFromView(){
        switchNotificarRepetir = findViewById(R.id.switchRepetir);
        spinnerNotificacao = findViewById(R.id.spinnerNotificacao);
        spinnerNotificacaoRepetir = findViewById(R.id.spinnerRepetir);
        switchNotificar = findViewById(R.id.switchNotificar);
        tituloTarefa = findViewById(R.id.InputTituloTarefa);
        aliasTarefa = findViewById(R.id.InputAliasTarefa);
        descricaoTarefa = findViewById(R.id.InputDescricaoTarefa);
        valorNota = findViewById(R.id.InputValorNota);
        dataEntrega = findViewById(R.id.InputDataEntregaTarefa);
        dataEntregaTextField = findViewById(R.id.InputDataEntregaTextField);
        insertBT = findViewById(R.id.SalvarTarefaButton);
        iconeTarefaImageView = findViewById(R.id.ImageViewIconeCriarTarefa);
        cardViewNotificar = findViewById(R.id.CardViewNotificar);
        textInputLayoutFrequencia = findViewById(R.id.textInputLayoutFrequencia);
    }
}