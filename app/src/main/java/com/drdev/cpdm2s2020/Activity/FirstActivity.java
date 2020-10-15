package com.drdev.cpdm2s2020.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drdev.cpdm2s2020.Model.UsuarioModel;
import com.drdev.cpdm2s2020.R;
import com.drdev.cpdm2s2020.Service.FuncAux;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static java.lang.Integer.parseInt;

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailText;
    private  TextInputLayout  passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private  GoogleSignInOptions gso;
    private int RC_SIGN_IN = 9001;
    private UsuarioModel usuario;
    private FuncAux func;


    private void Construtor(){
        func = new FuncAux(FirstActivity.this);
        usuario = new UsuarioModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Construtor();
        HideStuff();
        StartFirebaseServices();
        StartGoogleServices();
        SetBTListeners();
    }

    private void HideStuff(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void SetBTListeners(){

        emailText = findViewById(R.id.IdEmailTxtLOGIN);
        passwordText = findViewById(R.id.IdPasswordTxtLOGIN);

        Button signInEmail = findViewById(R.id.googleSignInEmailButton);
        signInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SignUpEmail();
            }
        });

        SignInButton signInGoogle = findViewById(R.id.googleSignInButton);
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateUserByGoogle();
            }
        });
    }

    private void StartGoogleServices(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestId()
                .requestEmail()
                .requestIdToken(getString(R.string.requestId))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount currentUserGoogle = GoogleSignIn.getLastSignedInAccount(this);

        if (currentUserGoogle != null){
            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
            finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()){
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                usuario.DisplayName = account.getDisplayName();
                usuario.IdToken = account.getIdToken();

                func.SaveUserPrefs(getString(R.string.DisplayName), usuario.DisplayName);
                func.SaveUserPrefs(getString(R.string.IdTokenGoogle), usuario.IdToken);

                CreateFirebaseUserWithGoogle(account.getIdToken());
            }else{
                func.Toast("Não foi possivel recuperar os dados do usuario, verifique as permissoes do aplicativo e tente novamente.",0);
            }
        } catch (ApiException e) {
            func.Toast("Ocorreu um erro ao registrar o usuario, tente novamente mais tarde.",0);
        }
    }

    private void CreateFirebaseUserWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            func.SaveUserPrefs(getString(R.string.IdTokenFirebase),user.getUid());
                            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            func.Toast("Não foi possivel criar o usuario, tente novamente mais tarde.", 1 );
                        }
                    }
                });
    }

    private void CreateUserByGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void StartFirebaseServices(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUserFB = mAuth.getCurrentUser();

        if (currentUserFB != null){
            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void SignUpEmail(){

        usuario.Login = emailText.getEditText().getText().toString();
        usuario.Senha = passwordText.getEditText().getText().toString();

        if (!usuario.Login.matches("")){
            if (!usuario.Senha.matches("")){
                CreateFirebaseUserByEmail(usuario.Login, usuario.Senha);
            }else{
                func.Toast("É necessario digitar uma senha para se cadastrar.", 0);
            }
        }else{
            func.Toast("É necessario digitar um email para se cadastrar.",0);
        }
    }

    private void CreateFirebaseUserByEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            func.SaveUserPrefs(getString(R.string.IdTokenGoogle), user.getUid());
                            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                            finish();
                        }else{
                            func.Toast("Não foi possivel registrar o usuario, verifique a sua conexão e tente novamente.",0);
                        }
                    }
                });
    }
}


