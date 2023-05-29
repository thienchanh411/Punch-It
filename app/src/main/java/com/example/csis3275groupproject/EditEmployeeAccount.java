package com.example.csis3275groupproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.csis3275groupproject.DB.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditEmployeeAccount extends AppCompatActivity {
    EditText nameDisplay;
    EditText dobDisplay;
    EditText emailDisplay;
    EditText addressDisplay;
    EditText phoneDisplay;
    EditText jobTitleDisplay;
    EditText hireDateDisplay;
    EditText baseSalaryDisplay;

    Button deleteEmpBtn;
    Button updateEmpBtn;
    Employee employee;

    SharedPreferences preferForm_Admin_ListUser;
    String employeeUID;
    String employeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);

        ImageView editIcon = findViewById(R.id.editProfileBtn);

        nameDisplay = findViewById(R.id.editProfileNameDisplay);
        dobDisplay = findViewById(R.id.editProfileDobDisplay);
        emailDisplay = findViewById(R.id.editProfileEmailDisplay);
        addressDisplay = findViewById(R.id.editProfileAddressDisplay);
        phoneDisplay = findViewById(R.id.editProfilePhoneDisplay);
        jobTitleDisplay = findViewById(R.id.editProfileJobTittleDisplay);
        hireDateDisplay = findViewById(R.id.editProfileHiredDateDisplay);
        baseSalaryDisplay = findViewById(R.id.editProfileBaseSalaryDisplay);

        updateEmpBtn = findViewById(R.id.updateEmployeeBtn);
        deleteEmpBtn = findViewById(R.id.deleteEmployeeBtn);

        preferForm_Admin_ListUser = PreferenceManager.getDefaultSharedPreferences(this);
        employeeEmail = preferForm_Admin_ListUser.getString("employeeToEdit", "0");
        employeeUID = preferForm_Admin_ListUser.getString("uid", "1");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employees");
        Query queryGetEmployee = databaseReference.orderByChild("email").equalTo(employeeEmail);

        queryGetEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    employee = dataSnapshot.getValue(Employee.class);
                    nameDisplay.setText(employee.getFullName());
                    dobDisplay.setText(employee.getBirthDate());
                    emailDisplay.setText(employee.getEmail());
                    addressDisplay.setText(employee.getAddress());
                    phoneDisplay.setText(employee.getPhone());
                    jobTitleDisplay.setText(employee.getJobTitle());
                    hireDateDisplay.setText(employee.getHireDate());
                    baseSalaryDisplay.setText("" + employee.getBaseSalary());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditIconFunction();
            }
        });

        updateEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameUpdated = nameDisplay.getText().toString();
                String dateBirthUpdated = dobDisplay.getText().toString();
                String addressUpdated = addressDisplay.getText().toString();
                String phoneUpdated = phoneDisplay.getText().toString();
                double salaryUpdated = Double.parseDouble(baseSalaryDisplay.getText().toString());
                String hireDateUpdated = hireDateDisplay.getText().toString();
                String jobTitleUpdated = jobTitleDisplay.getText().toString();

                EditData(employeeUID, nameUpdated, dateBirthUpdated,  phoneUpdated, addressUpdated, jobTitleUpdated, hireDateUpdated, salaryUpdated);
                startActivity(new Intent(getBaseContext(), EmployeeList.class));
            }
        });

        deleteEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("Employees").child(employeeUID).child("accountStatus").setValue("inactive");
                Toast.makeText(EditEmployeeAccount.this, "Employee: " + employee.getFullName() + " deleted!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), EmployeeList.class));
            }
        });



    }

    public void EditIconFunction(){
        EditText[] editTextFields = {nameDisplay, dobDisplay, addressDisplay, phoneDisplay, hireDateDisplay, baseSalaryDisplay, jobTitleDisplay};
        for (int i = 0; i < editTextFields.length; i++) {
                editTextFields[i].setEnabled(true);
                editTextFields[i].setBackground(getDrawable(R.drawable.edit_text_border));
                editTextFields[i].setCursorVisible(true);
        }
    }

    public void EditData(String id, String fName, String dateOfBirth, String phoneNum, String address, String jobTitle, String hiredDate, double salary){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Employees").child(id);
        databaseReference.child("fullName").setValue(fName);
        databaseReference.child("birthDate").setValue(dateOfBirth);
        databaseReference.child("phone").setValue(phoneNum);
        databaseReference.child("address").setValue(address);
        databaseReference.child("baseSalary").setValue(salary);
        databaseReference.child("jobTitle").setValue(jobTitle);
        databaseReference.child("hireDate").setValue(hiredDate);
    }

}