package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

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
                    drawerLayout.closeDrawers();
                    intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        //setting drawer header name, needs to be set later with firebase data, see already coded examples
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.full_name);

    }

    //function to inflate the action bar on creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
