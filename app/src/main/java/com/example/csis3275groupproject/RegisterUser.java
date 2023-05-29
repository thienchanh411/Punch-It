package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.csis3275groupproject.DB.Employee;
import com.example.csis3275groupproject.DB.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterUser extends AppCompatActivity {

    //Creating object of database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://punchitapp-dffc0-default-rtdb.firebaseio.com/").child("Employees");
    Employee employee;
    ArrayList<Employee> employeeAddedList;

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

//        getSupportActionBar().setTitle("Punch it!");

        //Getting fields
        final EditText legalName = findViewById(R.id.txtLegalName);
        final EditText dateOfBirth = findViewById(R.id.txtDob);
        final EditText email = findViewById(R.id.txtEmail);
        final EditText address = findViewById(R.id.txtAddress);
        final EditText passwordReg = findViewById(R.id.txtPasswordReg);
        final EditText phone = findViewById(R.id.txtPhone);
        final EditText jobTitle = findViewById(R.id.txtJobTitle);
        final EditText salary = findViewById(R.id.txtBaseSalary);
        final EditText hireDate = findViewById(R.id.txtHireDate);

        final Button btnCreateEmployee = findViewById(R.id.btnCreateEmployee);

        btnCreateEmployee.setOnClickListener(view -> {

            //Getting data from the fields
            final String legalNameTxt = legalName.getText().toString();
            final String dateOfBirthTxt = dateOfBirth.getText().toString();
            final String emailTxt = email.getText().toString();
            final String pswTxt = passwordReg.getText().toString();
            final String addressTxt = address.getText().toString();
            final String phoneTxt = phone.getText().toString();
            final String jobTitleTxt = jobTitle.getText().toString();
            final Long salaryTxt = Long.parseLong(salary.getText().toString());
            final String hireDateTxt = hireDate.getText().toString();
            employee = new Employee();

            //Checking if email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                email.setError("Please provide a valid Email Address!");
                email.requestFocus();
                return;
            }
            if (pswTxt.isEmpty()) {
                passwordReg.setError("Please enter a password!");
                passwordReg.requestFocus();
                return;
            }
            //Check if all information was filled
            if (legalNameTxt.isEmpty() || dateOfBirthTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || addressTxt.isEmpty() || jobTitleTxt.isEmpty() || hireDateTxt.isEmpty()) {
                Toast.makeText(RegisterUser.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            }

            employee = new Employee(legalNameTxt, dateOfBirthTxt, emailTxt, pswTxt, phoneTxt, addressTxt, jobTitleTxt, salaryTxt, hireDateTxt, false, employee.getUid());

            btnCreateEmployee.setOnClickListener(view1 -> {

                firebaseAuth = FirebaseAuth.getInstance();
                progressBar = findViewById(R.id.progressBar);

                //Proceed with register
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(emailTxt,pswTxt).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        databaseReference.child(userUID).setValue(employee);

                        Toast.makeText(RegisterUser.this, "New Employee Added!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(RegisterUser.this, Login.class));
                    }
                    else{
                        Toast.makeText(RegisterUser.this, "Register Unsuccessful!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            });
        });
    }
}