package com.example.auth_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView register,forgot_password;
    EditText edit_username, edit_email, edit_password;
    Button sign_in;
    private FirebaseAuth mAuth;
    ProgressBar pBar;
    public static  final String emailExtra = "com.example.auth_app.emailExtra";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.Register);

        forgot_password = (TextView)findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);
        register.setOnClickListener((View.OnClickListener) this);
        sign_in = (Button) findViewById(R.id.login);
        sign_in.setOnClickListener(this);
        edit_username = (EditText) findViewById(R.id.reg_name);
        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.password);
        pBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.Register:
                startActivity(new Intent(this,Register_user.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(this,ForgotPassword.class));
                break;

        }

    }

    private void userLogin() {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
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
        if(password.isEmpty()){
            edit_password.setError("Password is required");
            edit_password.requestFocus();
            return;
        }
        if(password.length()<6){
            edit_password.setError("Minimum length of password should be 6");
            edit_password.requestFocus();
            return;
        }
        pBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //redirect to user profile
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        intent.putExtra(emailExtra, email);
                        startActivity(intent);
                        pBar.setVisibility(View.GONE);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check Email to Verify Account",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(MainActivity.this,"Failed to Login"+e.getMessage(),Toast.LENGTH_LONG).show();
                    pBar.setVisibility(View.GONE);
                }
            }
        });
    }
}