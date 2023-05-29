package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csis3275groupproject.DB.Employee;
import com.example.csis3275groupproject.DB.Punch;
import com.example.csis3275groupproject.DB.PunchDB;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EmployeePerformance extends AppCompatActivity {

    DatePickerDialog datePickerDialog;

    ArrayList <Employee> listEmployees;
    ArrayList <PunchDB> listPunches;
    ArrayList<LocalDate> listWorkDates;
    ArrayList<LocalTime> list_StartTime;
    ArrayList<LocalTime> list_EndTime;
    ArrayList<Long> listWorkedMinutes;
    Long wage;
    String emailLogin;
    String userFullName;
    LocalDate choose_FromDate;
    LocalDate choose_ToDate;
    String test, test2;
    long totalSalary;
    double rating;
    String assetRemark;
    EditText txt_FromDate;
    EditText txt_ToDate;

    private int cYear, cMonth, cDayOfMonth;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_performance);

        SharedPreferences preferencesFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        emailLogin = preferencesFromLogin.getString("userEmail", "none");
        userFullName = preferencesFromLogin.getString("userFulName", "NoName");
        wage = preferencesFromLogin.getLong("userWage", 0);

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
        listPunches = new ArrayList<>();
        listWorkDates = new ArrayList<>();
        list_StartTime = new ArrayList<>();
        list_EndTime = new ArrayList<>();
        listWorkedMinutes = new ArrayList<>();

        TextView txtFullName = findViewById(R.id.performane_Name);
        TextView txtSalary = findViewById(R.id.performance_Salary);
        TextView txtRemark = findViewById(R.id.performance_Remark);
        TextView txtRating = findViewById(R.id.performance_Rating);
        txt_FromDate = findViewById(R.id.performance_From);
        txt_ToDate = findViewById(R.id.performance_ToDate);
        TextView txtTest = findViewById(R.id.test);
        Button btnCalculate = findViewById(R.id.performance_Btn_Calculate);

        DecimalFormat format = new DecimalFormat("###.##");



        //Get data from Firebase and set into field at the beginning*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
        Query query = reference.orderByChild("email").equalTo(emailLogin);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            txtFullName.setText(foundEmp.getFullName());
                            wage = foundEmp.getBaseSalary();

                        }
                    }

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

        //Assigne user Fullname into textView*/
//        navUserName.setText(userFullName);
//        txtFullName.setText(userFullName);

        //Set DatePicker and Get date from DatePicker for beginning Date*/
        txt_FromDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            cYear = calendar.get(Calendar.YEAR); //get Year;
            cMonth = calendar.get(Calendar.MONTH); //get Month;
            cDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); //get Day;

            //Show Datepicker dialog
            datePickerDialog = new DatePickerDialog(EmployeePerformance.this,
                    android.R.style.Theme_DeviceDefault_Dialog,
                    (view, year, month, dayOfMonth) -> {
                        choose_FromDate = LocalDate.of(year, month+1, dayOfMonth);
                        txt_FromDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }, cYear, cMonth, cDayOfMonth);
            datePickerDialog.show();
        });
        //Set DatePicker and Get date from DatePicker for ending Date*/
        txt_ToDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            cYear = calendar.get(Calendar.YEAR); //get Year;
            cMonth = calendar.get(Calendar.MONTH); //get Month;
            cDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); //get Day;

            //Show Datepicker dialog
            datePickerDialog = new DatePickerDialog(EmployeePerformance.this,
                    android.R.style.Theme_DeviceDefault_Dialog,
                    (view, year, month, dayOfMonth) -> {
                        choose_ToDate = LocalDate.of(year, month+1, dayOfMonth);
                        txt_ToDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }, cYear, cMonth, cDayOfMonth);
            datePickerDialog.show();

        });

        //Click button Calculate to show result*/
        btnCalculate.setOnClickListener(v -> {
            listWorkDates.clear();
            list_StartTime.clear();
            list_EndTime.clear();

            //Get data from Firebase in Punches table where email equal to userEmail*/
            DatabaseReference dbPunch = FirebaseDatabase.getInstance().getReference("Punches");
            Query query1 = dbPunch.orderByChild("email").equalTo(emailLogin);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PunchDB punch = dataSnapshot.getValue(PunchDB.class);

                        if(ChronoUnit.DAYS.between(choose_FromDate, LocalDate.of(Integer.parseInt(punch.getYear()),
                                Integer.parseInt(punch.getMonth()), Integer.parseInt(punch.getDay()))) >= 0
                                && ChronoUnit.DAYS.between(LocalDate.of(Integer.parseInt(punch.getYear()),
                                Integer.parseInt(punch.getMonth()), Integer.parseInt(punch.getDay())), choose_ToDate) >= 0){

                            listPunches.add(punch);
                            listWorkDates.add(LocalDate.of(Integer.parseInt(punch.getYear()),
                                    Integer.parseInt(punch.getMonth()), Integer.parseInt(punch.getDay())));
                            if(punch.getInOut()){
                                list_StartTime.add(LocalTime.of(Integer.parseInt(punch.getHour()),
                                        Integer.parseInt(punch.getMinute()), Integer.parseInt(punch.getSecond())));

                            }else list_EndTime.add(LocalTime.of(Integer.parseInt(punch.getHour()),
                                    Integer.parseInt(punch.getMinute()), Integer.parseInt(punch.getSecond())));
                        }

                    }

                    //Remove the duplicated dates because the punch in is stored in 1 day, and
                    //punch out is stored in another day, both days are similar*/
                    for(int i =0; i<listWorkDates.size()-1; i++){
                        if(listWorkDates.get(i).equals(listWorkDates.get(i+1))){
                            listWorkDates.remove(listWorkDates.get(i));
                        }
                    }
                    //Calculate worked minutes for every single day
                    //LocalTime.ChroUnit only return Long value => Arraylist has to set Long as well
                    for(int i = 0; i< list_EndTime.size(); i++){
                        listWorkedMinutes.add(ChronoUnit.MINUTES.between(list_StartTime.get(i), list_EndTime.get(i)));
                    }

                    if(listWorkedMinutes.size()>0){
                        totalSalary = CalTotalSalary(wage, listWorkedMinutes);
                        rating = CalRating(listWorkedMinutes);
                        assetRemark = GiveRemark(rating);

                        txtRemark.setText(assetRemark);
                        txtRating.setText(format.format(rating*100)+"%");
                        txtSalary.setText("$ " + totalSalary);
                    } else
                        Toast.makeText(EmployeePerformance.this, "Sorry! You do not have any worked hours " +
                                "in the period of time", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    //Methods to Calculate salary, rating, give remark*/
    public static long CalTotalSalary(long wage, ArrayList<Long>listWorkedMinures){

        long totalSalary = 0;
        for(int i = 0; i<listWorkedMinures.size(); i++){
            totalSalary += wage*(listWorkedMinures.get(i)/60);
        }
        return totalSalary;
    }
    public static double CalRating(ArrayList<Long>listWorkedMinures){
        double rating;
        int totalMins = 0;
        for(int i =0; i<listWorkedMinures.size();i++){
            totalMins += listWorkedMinures.get(i);
        }
        rating = (double)totalMins/(listWorkedMinures.size()*8*60);
        return  rating;

    }
    public static String GiveRemark(double rating){
        String remark = "";
        if(rating>0.95){
            remark = "Exellent";
        }
        if(0.9 < rating && rating<=0.95){
            remark = "Average";
        }
        if(rating<=0.9){
            remark = "Poor";
        }
        return remark;
    }
}