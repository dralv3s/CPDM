package com.drdev.cpdm2s2020.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private  GoogleSignInOptions gso;
    private DataBaseHelper db;

    private TextView sairText;
    private TextView textViewTarefaCount;

    private TextView textViewTarefaOKCount;
    private TextView textViewTarefaAtrasoCount;


    private CardView visualisarCardView;
    private CardView incluirCardView;
    private CardView perfilCardView;
    private CardView configCardView;

    private FuncAux func;

    private ArrayList<TarefaModel> tarefas;

    FirebaseUser currentUserFB;
    GoogleSignInAccount currentUserGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        func = new FuncAux(HomeActivity.this);

        String appTitle = func.GetUserPrefsString(getString(R.string.DisplayName), getString(R.string.BemVindo));
        getSupportActionBar().setTitle(getString(R.string.BemVindo) + " " + appTitle);
        SetListeners();
        updateUI();
    }

    private void updateUI(){
        textViewTarefaCount = findViewById(R.id.textViewTarefaCount);
        textViewTarefaOKCount = findViewById(R.id.textViewTarefaOKCount);
        textViewTarefaAtrasoCount = findViewById(R.id.textViewTarefaAtrasoCount);

        db = new DataBaseHelper(getApplicationContext());

        tarefas = db.GetTarefasList();

        Integer tarefasTotal = tarefas.size();

        Integer tarefasEmDia = 0;

        Integer tarefasAtraso = 0;


        for (TarefaModel tarefa:tarefas) {

            if (Calendar.getInstance().getTime().compareTo(func.formatDateTime(tarefa.DataEntrega)) >= 0){
                tarefasAtraso ++;
            }else{
                tarefasEmDia ++;
            }
        }

        textViewTarefaCount.setText(tarefasTotal.toString());
        textViewTarefaOKCount.setText(tarefasEmDia.toString());
        textViewTarefaAtrasoCount.setText(tarefasAtraso.toString());
    }

    private void SetListeners(){

        visualisarCardView = findViewById(R.id.cardViewVisualizar);
        visualisarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, ExibirActivity.class));
            }
        });

        incluirCardView = findViewById(R.id.cardViewIncluir);
        incluirCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, CriarActivity.class));
            }
        });

        perfilCardView = findViewById(R.id.cardViewEditar);
        perfilCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, PerfilActivity.class));
            }
        });

        configCardView = findViewById(R.id.cardViewSetup);
        configCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, SetupActivity.class));
            }
        });
    }


    private void LogOut(){
        SignOutFirebase();
        SignOutGoogle();

        func.ClearPrefs();
        CheckLogOut();
    }

    private void CheckLogOut(){
        if (currentUserFB == null && currentUserGoogle == null){
            startActivity(new Intent(HomeActivity.this, FirstActivity.class));
        }else{
            LogOut();
        }
    }

    private void SignOutGoogle(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestId()
                .requestEmail()
                .requestIdToken(getString(R.string.requestId))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        currentUserGoogle = GoogleSignIn.getLastSignedInAccount(this);

        if (currentUserGoogle != null){
            GoogleSignOut();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }
    }

    private void GoogleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void SignOutFirebase(){
        mAuth = FirebaseAuth.getInstance();
        currentUserFB = mAuth.getCurrentUser();

        if (currentUserFB != null){
            FirebaseAuth.getInstance().signOut();
            currentUserFB = mAuth.getCurrentUser();
        }
    }
}