package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.csis3275groupproject.DB.Employee;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EmployeeList extends AppCompatActivity implements RecycleViewAdapter.ItemClickListener{

    DatabaseReference databaseReference;
    ArrayList<String> listKeys;
    ArrayList<String> listEmails;
    ArrayList<String> listNames;
    RecyclerView recycler_list_employees;
    SharedPreferences preferForm_Admin_ListUser;
    Button addEmployee;
    String emailLogin;
    String userFullName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        preferForm_Admin_ListUser = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferencesFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        emailLogin = preferencesFromLogin.getString("userEmail", "none");
        userFullName = preferencesFromLogin.getString("userFulName", "NoName");

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
                case R.id.nav_manage_user:
                    drawerLayout.closeDrawers();
                    Intent intent = new Intent(getApplicationContext(), EmployeeList.class);
                    startActivity(intent);
                    break;
                case R.id.nav_punch:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), PunchScreen.class);
                    startActivity(intent);
                    break;
                case R.id.nav_performance:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), EmployeePerformance.class);
                    startActivity(intent);
                    break;
                case R.id.nav_personal_info:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), userProfile.class);
                    startActivity(intent);
                    break;
                case R.id.nav_sign_out:
                    preferForm_Admin_ListUser.edit().clear().apply();
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
        TextView navUserName = (TextView) headerView.findViewById(R.id.full_name);

        //Activity items references
        recycler_list_employees = findViewById(R.id.employeeList);
        addEmployee = findViewById(R.id.btnAddEmployee);
        //DB Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees");


        listKeys = new ArrayList<>();
        listEmails = new ArrayList<>();
        listNames = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    if(employee.getAccountStatus().equalsIgnoreCase("active")){
                        listNames.add(employee.getFullName());
                        listEmails.add(employee.getEmail());
                        listKeys.add(dataSnapshot.getKey());
                    }
                    if (listEmails.contains(emailLogin)) {
                        int pos = listEmails.indexOf(emailLogin);
                        String name = listNames.get(pos);
                        navUserName.setText(name);
                    }
                }
                RecycleViewAdapter adapter = new RecycleViewAdapter(getBaseContext(), listNames);
                recycler_list_employees.setAdapter(adapter);
                recycler_list_employees.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recycler_list_employees.addItemDecoration(new DividerItemDecoration(EmployeeList.this,DividerItemDecoration.HORIZONTAL));
                adapter.setClickListener(EmployeeList.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeList.this, RegisterUser.class));
            }
        });

    }
    @Override
    public void onItemClick(View view, int position) {
        SharedPreferences.Editor editorUserEmployee = preferForm_Admin_ListUser.edit();
        editorUserEmployee.putString("employeeToEdit", listEmails.get(position));
        editorUserEmployee.putString("uid", listKeys.get(position));
        editorUserEmployee.apply();
        startActivity(new Intent(EmployeeList.this, EditEmployeeAccount.class));
    }
}
