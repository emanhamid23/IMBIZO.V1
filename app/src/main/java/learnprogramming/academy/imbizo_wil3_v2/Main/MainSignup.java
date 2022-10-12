package learnprogramming.academy.imbizo_wil3_v2.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MainSignup extends AppCompatActivity {

    //Variables initialized for buttons, Text Edits and Firebase Authentication.
    public Button buttonSignup;
    public EditText etUsername, etPassword, conPass;
    private TextView currentUserTv;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);

        //Getting current firebase instance
        mAuth = FirebaseAuth.getInstance();
        //currentUser = mAuth.getCurrentUser();

        //Setting Text Edit variables with the ones in the UI.
        etUsername = findViewById(R.id.signup_email_edtx);
        etPassword = findViewById(R.id.signup_password_edtx);
        conPass = findViewById(R.id.signup_confirm_password_edtx);

        //Setting Text View variables with the ones in the UI
        currentUserTv = findViewById(R.id.user_tv);


        //Setting login button with the ones in the UI.
        buttonSignup = findViewById(R.id.signup_btn);

        currentUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainSignup.this, MainLogin.class);
                startActivity(intent);
                finish();
            }
        });


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(MainSignup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainSignup.this, MainLogin.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        MainActivity.checkView = 4;
        mAuth.addAuthStateListener(authStateListener);
    }

    public void Register() { //start of Register() Method

        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        String confirmPassword = conPass.getText().toString();

        if (confirmPassword.equals(password)) {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainSignup.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainSignup.this, "Success",
                                Toast.LENGTH_SHORT).show();
                        // Sign in success, update UI with the signed-in user's information
                        currentUser = mAuth.getCurrentUser();
                        Intent i = new Intent(MainSignup.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        // If sign in fails, display a message to the user.
                        currentUser = null;
                        Toast.makeText(MainSignup.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainSignup.this, "Password is not matching",
                    Toast.LENGTH_SHORT).show();
        }
    } //end of Register() Method

}