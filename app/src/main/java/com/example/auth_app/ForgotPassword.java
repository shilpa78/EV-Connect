package com.example.auth_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText edit_email;
    FirebaseAuth auth;
    private Button reset_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edit_email = (EditText) findViewById(R.id.email_forgot_pass);
        reset_pass = (Button) findViewById(R.id.forgot_pass_b);
        auth = FirebaseAuth.getInstance();
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        String email = edit_email.getText().toString().trim();
        if(email.isEmpty()){
            edit_email.setError("Email is empty");
            edit_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_email.setError("Please Provide Valid Email!");
            edit_email.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check Email For Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}