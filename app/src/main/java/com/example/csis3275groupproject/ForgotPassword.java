package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private Button btnReset;
    private EditText txtEmail;

    private FirebaseAuth uAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        txtEmail = findViewById(R.id.txtEmailReset);
        btnReset = findViewById(R.id.btnResetPassword);
        progressBar = findViewById(R.id.progressBarResetPassword);
        uAuth = FirebaseAuth.getInstance();





        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString().trim();

                if(email.isEmpty()){
                    txtEmail.setError("Email is required!");
                    txtEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    txtEmail.setError("Please enter a valid email!");
                    txtEmail.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                uAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Please check your email to reset your password!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            txtEmail.setText("");
                        }
                        else{
                            Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}