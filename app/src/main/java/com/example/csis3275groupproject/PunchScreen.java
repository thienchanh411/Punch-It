package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csis3275groupproject.DB.Employee;
import com.example.csis3275groupproject.DB.Punch;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PunchScreen extends AppCompatActivity {
    boolean admin;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch);

        /**Get SharedPreferences from Login account activity*/
        SharedPreferences preferencesFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        String emailLogin = preferencesFromLogin.getString("userEmail", "none");
        admin = preferencesFromLogin.getBoolean("admin", false);

        //creating elements that are needed for the toolbar and side drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //setting the action bar as the custom toolbar layout and changing the title text
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

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
                case R.id.nav_manage_user:
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), EmployeeList.class);
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
        TextView navUserName = (TextView) headerView.findViewById(R.id.full_name);

        //Initialize Arraylist to store data from Firebase
        ArrayList<Employee> listEmployees = new ArrayList<Employee>();
        ArrayList<Punch>listPunches = new ArrayList<Punch>();

        //setting up clock and date stuff
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final String[] punchDate = {null};
        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat initialFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        final Date[] parsedDate = {null};
        TextView dateView = findViewById(R.id.txt_Date);
        dateView.setText(date);

        //getting userID for db
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String currentUserID = currentFirebaseUser.getUid();



        /**Get data from Firebase*/
        DatabaseReference punchReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://punchitapp-dffc0-default-rtdb.firebaseio.com/").child("Punches");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://punchitapp-dffc0-default-rtdb.firebaseio.com/").child("Employees");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Employee employee = dataSnapshot.getValue(Employee.class);

                    listEmployees.add(new Employee(employee.getFullName(), employee.getBirthDate(),
                            employee.getEmail(), employee.getPassword(), employee.getPhone(), employee.getAddress(),
                            employee.getJobTitle(), employee.getBaseSalary(), employee.getHireDate(), employee.isAdmin(), employee.getUid()));

                    for(int i =0; i<listEmployees.size();i++){
                        if(emailLogin.equalsIgnoreCase(listEmployees.get(i).getEmail())){
                            Employee foundEmp = new Employee(listEmployees.get(i).getFullName(), listEmployees.get(i).getBirthDate(),
                                    listEmployees.get(i).getEmail(), listEmployees.get(i).getPassword(), listEmployees.get(i).getPhone(),
                                    listEmployees.get(i).getAddress(),
                                    listEmployees.get(i).getJobTitle(), listEmployees.get(i).getBaseSalary() ,listEmployees.get(i).getHireDate(), employee.isAdmin(), employee.getUid());

                            navUserName.setText(foundEmp.getFullName());
                            if (foundEmp.isAdmin()) {
                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.navigation_menu_admin);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //all punch related code with the punch button listener
        Button punchButton = findViewById(R.id.btn_SubmitPunch);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean[] inOut = {true};
                RadioGroup radioGroup = findViewById(R.id.rdbG_radioGroup);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rdb_punchIN:
                        inOut[0] = true;
                        break;
                    case R.id.rdb_punchOUT:
                        inOut[0] = false;
                        break;
                    default:
                        inOut[0] = true;
                        break;
                }
                //down here is the code for actually pushing the punch fields to firebase
                Punch newPunch = null;
                try {
                    newPunch = new Punch(currentUserID, new Date().toString(), inOut[0], emailLogin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                punchReference.push().setValue(newPunch);
                Toast.makeText(PunchScreen.this, "Punch Successful", Toast.LENGTH_SHORT).show();
                if(admin==true){
                    Intent intent = new Intent(PunchScreen.this, AdminHomeScreen.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(PunchScreen.this, EmployeeHomeScreen.class);
                    startActivity(intent);
                }

            }
        });

    }

    //function to inflate the action bar on creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}