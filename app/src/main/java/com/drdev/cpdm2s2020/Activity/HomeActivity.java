package com.drdev.cpdm2s2020.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.DataBaseHelper;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private DataBaseHelper db;

    private TextView sairText;
    private TextView textViewTarefaCount;

    private TextView textViewTarefaOKCount;
    private TextView textViewTarefaAtrasoCount;


    private CardView visualisarCardView;
    private CardView incluirCardView;
    private CardView editarCardView;
    private CardView configCardView;

    private FuncAux func;

    private ArrayList<TarefaModel> tarefas;

    FirebaseUser currentUserFB;
    GoogleSignInAccount currentUserGoogle;

    private AdView mAdView;

    Notification.Builder notification;
    private int notficationId = 2313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        func = new FuncAux(HomeActivity.this);
        String displayName = func.GetUserPrefsString(getString(R.string.DisplayName), getString(R.string.BemVindo));
        getSupportActionBar().setTitle(getString(R.string.BemVindo) + " " + displayName);
        SetListeners();
        updateUI();
    }

    private void updateUI(){
        textViewTarefaCount = findViewById(R.id.textViewTarefaCount);
        textViewTarefaOKCount = findViewById(R.id.textViewTarefaOKCount);
        textViewTarefaAtrasoCount = findViewById(R.id.textViewTarefaAtrasoCount);

        db = new DataBaseHelper(getApplicationContext());

        tarefas = db.GetTarefas();

        Integer tarefasTotal = tarefas.size();

        Integer tarefasEmDia = 0;

        Integer tarefasAtraso = 0;


        for (TarefaModel tarefa:tarefas) {

            if (Calendar.getInstance().getTime().compareTo(func.FormatDateTime(tarefa.DataEntrega)) >= 0){
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
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        sairText = findViewById(R.id.SairTxt);
        sairText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              LogOut();
            }
        });

        visualisarCardView = findViewById(R.id.cardViewVisualizar);
        visualisarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, ExibirActivity.class);
                intent.putExtra(getString(R.string.Action), getResources().getInteger(R.integer.VisualizarAction));
                startActivity(intent);
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

        editarCardView = findViewById(R.id.cardViewEditar);
        editarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, ExibirActivity.class);
                intent.putExtra(getString(R.string.Action), getResources().getInteger(R.integer.EditarAction));
                startActivity(intent);
            }
        });

        configCardView = findViewById(R.id.cardViewSetup);
        configCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                addNotification();
            }
        });
    }

    private void addNotification() {
        // Builds your notification
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("John's Android Studio Tutorials")
                .setContentText("A video has just arrived!");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notficationId, builder.build());
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