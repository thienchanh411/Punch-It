package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csis3275groupproject.DB.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {


    ArrayList<Employee> listEmployees;
    private EditText usernameTxt;
    private EditText passwordTxt;
    private Button btnLogin;
    private TextView forgotPassword;

    private FirebaseAuth uAuth;
    private ProgressBar progressBar;
    boolean isAdm;
    String accStatus;

    String email,password;

    SharedPreferences sharedFromLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        usernameTxt = findViewById(R.id.txtEmailReset);
        passwordTxt = findViewById(R.id.txtPasswordReg);
        btnLogin = findViewById(R.id.btnResetPassword);
        forgotPassword = findViewById(R.id.txtForgotPassword);
        progressBar = findViewById(R.id.progressBarResetPassword);

        listEmployees = new ArrayList<>();

        //SharedPreferences to pass email of employee into EmployeePerformance Activity
        sharedFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedFromLogin.edit();



        btnLogin.setOnClickListener(view -> {
                    String email = usernameTxt.getText().toString().trim();
                    String password = passwordTxt.getText().toString().trim();

                    if (email.isEmpty()) {
                        usernameTxt.setError("Email is required!");
                        usernameTxt.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        usernameTxt.setError("Please enter a valid email!");
                        usernameTxt.requestFocus();
                        return;
                    }
                    if (password.isEmpty()) {
                        passwordTxt.setError("Password is required!");
                        passwordTxt.requestFocus();
                        return;
                    }
                    if (password.length() < 6) {
                        passwordTxt.setError("Min Password length is 6!");
                        passwordTxt.requestFocus();
                        return;
                    }


                    progressBar.setVisibility(View.VISIBLE);
                    //Get data from Firebase*/
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
                    Query query = reference.orderByChild("email").equalTo(email);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Employee employee = dataSnapshot.getValue(Employee.class);

                                if (email.equalsIgnoreCase(employee.getEmail()) &&
                                        password.equalsIgnoreCase(employee.getPassword())) {
                                    editor.putString("userEmail", email);
                                    editor.apply();
                                    if (employee.isAdmin()) {
                                        startActivity(new Intent(Login.this, AdminHomeScreen.class));
                                    } else if (employee.getAccountStatus().equalsIgnoreCase("active")){
                                        startActivity(new Intent(Login.this, EmployeeHomeScreen.class));
                                    }
                                    else {
                                        Toast.makeText(Login.this, "Failed to Login, please check your credentials", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(Login.this, "Failed to Login, please check your credentials", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                });
        forgotPassword.setOnClickListener(view -> startActivity(new Intent(Login.this, ForgotPassword.class)));
    }
}