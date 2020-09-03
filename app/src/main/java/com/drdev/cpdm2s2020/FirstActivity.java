package com.drdev.cpdm2s2020;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.drdev.cpdm2s2020.util.FuncAux;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout emailText;
    private  TextInputLayout  passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private  GoogleSignInOptions gso;
    private int RC_SIGN_IN = 9001;

    private FuncAux func;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        FuncAux func = new FuncAux();

        HideStuff();
        StartFB();
        StartGoogleServices();
        SetBTListeners();
    }

    private void StartGoogleServices(){

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestId()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount currentUserGoogle = GoogleSignIn.getLastSignedInAccount(this);

        if (currentUserGoogle != null){
            signOut();
            //startActivity(new Intent(FirstActivity.this, HomeActivity.class));
        }
    }

    private void signOut() {
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
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (completedTask.isSuccessful()){
                startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                //TODO Enviar os dados para o firebase
            }
        } catch (ApiException e) {
            func.Toast(getApplicationContext(),"Ocorreu um erro ao registrar o usuario, tente novamente mais tarde.",0);
        }
    }

    private void CreateUserByGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void StartFB(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUserFB = mAuth.getCurrentUser();

        if (currentUserFB != null){
            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
            //FirebaseAuth.getInstance().signOut();
        }
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

    private void SignUpEmail(){
        if (!emailText.getEditText().getText().toString().matches("")){
            if (!passwordText.getEditText().getText().toString().matches("")){
                CreateUserByEmail(emailText.getEditText().getText().toString(), passwordText.getEditText().getText().toString());
            }else{
                func.Toast(getApplicationContext(),"É necessario digitar uma senha para se cadastrar.", 0);
            }
        }else{
            func.Toast(getApplicationContext(),"É necessario digitar um email para se cadastrar.",0);
        }
    }

    private void CreateUserByEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            func.Toast(getApplicationContext(),task.toString(),0);
                            startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                            //String uid = task.getResult().getUser().getUid();

                        }else{
                            func.Toast(getApplicationContext(),"Não foi possivel registrar o usuario, verifique a sua conexão e tente novamente.",0);
                        }
                    }
                });
    }
}


