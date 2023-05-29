package com.example.csis3275groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.csis3275groupproject.DB.Employee;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AdminHomeScreen extends AppCompatActivity {
    //Arrays for Hashmap
    String [] listHomeAdmin = {"Submit punch in/out", "View pay & performance",
            "View personal information", "Manage Employee"};
    int [] iconsAdminList = {R.drawable.punch, R.drawable.wallet,R.drawable.user, R.drawable.performance};
    ListView listView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        //Get SharedPreferences from Login account activity*/
        SharedPreferences preferencesFromLogin = PreferenceManager.getDefaultSharedPreferences(this);
        String emailLogin = preferencesFromLogin.getString("userEmail", "none");
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferencesFromLogin.edit();

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

        listView = findViewById(R.id.listAdmin);
        AdminHomeScreen.MyAdapter adapter = new AdminHomeScreen.MyAdapter(this, listHomeAdmin, iconsAdminList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                Intent intent = new Intent(getApplicationContext(), PunchScreen.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                bundle.putInt("image", iconsAdminList[0]);
                intent.putExtras(bundle);
                intent.putExtra("title", listHomeAdmin[0]);
                intent.putExtra("position", "" + 0);
                startActivity(intent);

            }

            if (position == 1) {
                Intent intent = new Intent(getApplicationContext(), EmployeePerformance.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                bundle.putInt("image", iconsAdminList[1]);
                intent.putExtras(bundle);
                intent.putExtra("title", listHomeAdmin[1]);
                intent.putExtra("position", "" + 1);
                startActivity(intent);

            }

            if (position == 2) {
                Intent intent = new Intent(getApplicationContext(), userProfile.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                bundle.putInt("image", iconsAdminList[2]);
                intent.putExtras(bundle);
                intent.putExtra("title", listHomeAdmin[2]);
                intent.putExtra("position", "" + 2);
                startActivity(intent);
            }


            if (position == 3) {
                Intent intent = new Intent(getApplicationContext(), EmployeeList.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                bundle.putInt("image", iconsAdminList[3]);
                intent.putExtras(bundle);
                intent.putExtra("title", listHomeAdmin[3]);
                intent.putExtra("position", "" + 3);
                startActivity(intent);
            }
        });

        //Get data from Firebase*/

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
        Query queryGetName = reference.orderByChild("email").equalTo(emailLogin);
        queryGetName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    navUserName.setText(employee.getFullName());
                    editor.putBoolean("admin", employee.isAdmin());
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //function to inflate the action bar on creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
  
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] rTitle;
        int[] rImages;

        MyAdapter(Context c, String[] title, int[] imgs) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rImages = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.row, parent, false);

            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);

            images.setImageResource(rImages[position]);
            myTitle.setText(rTitle[position]);
            return row;
        }
    }
}