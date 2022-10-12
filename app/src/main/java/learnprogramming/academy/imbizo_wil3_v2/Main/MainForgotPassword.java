package learnprogramming.academy.imbizo_wil3_v2.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import learnprogramming.academy.imbizo_wil3_v2.R;

public class MainForgotPassword extends AppCompatActivity {

    //Initialise variable
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private EditText Username_edtx;
    private Button Forgot_password_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forgot_password);

        //gets auth instance
        mAuth = FirebaseAuth.getInstance();
        //have on start or not coz code is below
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // reload();
        }

        //Assign variable
        Username_edtx = findViewById(R.id.fp_email_edtx);
        Forgot_password_btn = findViewById(R.id.fp_btn);

        //Reset password for account
        Forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //extract the username and send reset password link
                String mail = Username_edtx.getText().toString();

                //Validation
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    Username_edtx.setError("Please enter a valid email");
                    Username_edtx.requestFocus();
                    return;
                }

                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //link sent, message will be displayed
                        Toast.makeText(MainForgotPassword.this, "Reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //link not sent, error message
                        Toast.makeText(MainForgotPassword.this, "Error! Reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainForgotPassword.this, MainLogin.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}