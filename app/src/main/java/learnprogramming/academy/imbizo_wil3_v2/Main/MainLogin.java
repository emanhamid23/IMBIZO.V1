package learnprogramming.academy.imbizo_wil3_v2.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import learnprogramming.academy.imbizo_wil3_v2.MainActivity;
import learnprogramming.academy.imbizo_wil3_v2.R;

public class MainLogin extends AppCompatActivity {

    //Variables initialized for buttons, Text Edits and Firebase Authentication.
    public Button buttonLogin, buttonRegister;
    public EditText etUsername, etPassword, conPass;
    public RadioButton mRadioButtonReg, mRadioButtonLog;
    private TextView forgot_password, create_account_tv;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //Getting current firebase instance
        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();

        //Setting Text Edit variables with the ones in the UI.
        etUsername = findViewById(R.id.login_email_edtx);
        etPassword = findViewById(R.id.login_password_edtx);

        //Setting login button with the ones in the UI.
        buttonLogin = findViewById(R.id.login_btn);

        //Setting Text View variables with the ones in the UI
        forgot_password = findViewById(R.id.forgot_password_tv);
        create_account_tv = findViewById(R.id.login_create_new_account_tv);



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLogin.this, MainForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        create_account_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLogin.this, MainSignup.class);
                startActivity(intent);
                finish();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(MainLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
//        MainActivity.checkView = 4;
        mAuth.addAuthStateListener(authStateListener);
    }

    public void Login() { //start of Login() Method

        //getting Text from edit text fields
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(MainLogin.this, "Success",
                            Toast.LENGTH_SHORT).show();

                    currentUser = mAuth.getCurrentUser();
                    Intent i = new Intent(MainLogin.this, MainActivity.class);
                    startActivity(i);

                } else {
                    // If sign in fails, display a message to the user.
                    currentUser = null;
                    Toast.makeText(MainLogin.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//end of Login() method


}