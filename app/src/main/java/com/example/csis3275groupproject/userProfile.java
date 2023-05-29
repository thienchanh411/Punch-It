package com.example.csis3275groupproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.csis3275groupproject.DB.Employee;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class userProfile extends BaseActivity {
    ArrayList <Employee> listEmployees;
    boolean admin;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Get SharedPreferences from Login account activity*/
        SharedPreferences preferencesFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        String emailLogin = preferencesFromLogin.getString("userEmail", "none");
        String userFullName = preferencesFromLogin.getString("userFulName", "NoName");
        admin = preferencesFromLogin.getBoolean("admin", false);

        //creating elements that are needed for the toolbar and side drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //setting the action bar as the custom toolbar layout and changing the title text
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        //setting the side drawer button up
        toolbar.setNavigationIcon(R.drawable.ic_drawer_button);
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        //listener for what you have selected in the side drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(getApplicationContext(), EmployeeHomeScreen.class);
                    startActivity(intent);
                    break;
                case R.id.nav_manage_user:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), EmployeeList.class);
                    startActivity(intent);
                    break;
                case R.id.nav_performance:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), EmployeePerformance.class);
                    startActivity(intent);
                    break;
                case R.id.nav_punch:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), PunchScreen.class);
                    startActivity(intent);
                    break;
                case R.id.nav_personal_info:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), userProfile.class);
                    startActivity(intent);
                    break;
                case R.id.nav_sign_out:
                    preferencesFromLogin.edit().clear().apply();
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    break;
                case R.id.nav_admin_home:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), AdminHomeScreen.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        //setting drawer header name
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.full_name);

        //Initialize Arraylist to store data from Firebase
        listEmployees = new ArrayList<>();

        TextView topName = findViewById(R.id.profileNameUser);
        TextView userRole = findViewById(R.id.profileUserRole);
        EditText fullName = findViewById(R.id.profileName2);
        EditText DOB = findViewById(R.id.profileDOB);
        EditText address = findViewById(R.id.profileAddress);
        TextView email = findViewById(R.id.profileEmail);
        EditText password = findViewById(R.id.profile_Password);
        TextView hiredDate = findViewById(R.id.profile_HiredDate);
        EditText phone = findViewById(R.id.profilePhone);
        TextView wage = findViewById(R.id.profile_BaseSalary);

        Button btnEdit = findViewById(R.id.userProfile_BtnEdit);

        navUserName.setText(userFullName);

        //Get data from Firebase and set into field at the beginning*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
        Query query = reference.orderByChild("email").equalTo(emailLogin);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    userRole.setText(employee.getJobTitle());
                    fullName.setText(employee.getFullName());
                    email.setText(employee.getEmail());
                    address.setText(employee.getAddress());
                    hiredDate.setText(employee.getHireDate());
                    DOB.setText(employee.getBirthDate());
                    phone.setText(employee.getPhone());
                    wage.setText("$"+ employee.getBaseSalary());
                    navUserName.setText(employee.getFullName());
                    password.setText(employee.getPassword());
                    topName.setText(employee.getFullName());

                    if (employee.isAdmin()) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.navigation_menu_admin);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Click the Edit Button*/
        btnEdit.setOnClickListener(v -> {
            //Validate input*/
            if (TextUtils.isEmpty(fullName.getText().toString()) || TextUtils.isEmpty(DOB.getText().toString()) ||
                    TextUtils.isEmpty(password.getText().toString()) ||
                    TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(phone.getText().toString())) {

                if (TextUtils.isEmpty(fullName.getText().toString()))
                    fullName.setError("Please enter your name");
                if (TextUtils.isEmpty(DOB.getText().toString()))
                    DOB.setError("Please enter your birthday");
                if (TextUtils.isEmpty(password.getText().toString()))
                    password.setError("Please enter your password");
                if (TextUtils.isEmpty(address.getText().toString()))
                    address.setError("Please enter your address");
                if (TextUtils.isEmpty(phone.getText().toString()))
                    phone.setError("Please enter your phone");
            }

            //Update personal information*/
            Query queryEdit = reference.orderByChild("email").equalTo(emailLogin);
            queryEdit.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    reference.child(Objects.requireNonNull(snapshot.getKey())).child("phone").setValue(phone.getText().toString());
                    Task<Void> fullName1 = reference.child(snapshot.getKey()).child("fullName").setValue(fullName.getText().toString());
                    reference.child(snapshot.getKey()).child("address").setValue(address.getText().toString());
                    reference.child(snapshot.getKey()).child("birthDate").setValue(DOB.getText().toString());
                    reference.child(snapshot.getKey()).child("password").setValue(password.getText().toString());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(userProfile.this, "Update information successful", Toast.LENGTH_SHORT).show();
            if (admin) {
                startActivity(new Intent(userProfile.this, AdminHomeScreen.class));
            } else {
                startActivity(new Intent(userProfile.this, EmployeeHomeScreen.class));
            }

        });
                                   
    }
}