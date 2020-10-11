package com.example.giris.drdroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.giris.drdroid.fragments.AboutFragment;
import com.example.giris.drdroid.fragments.AppointmentsFragment;
import com.example.giris.drdroid.fragments.DiagnoseFragment;
import com.example.giris.drdroid.fragments.FindDoctorsFragment;
import com.example.giris.drdroid.fragments.PrescriptionsFragment;


public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREFS_NAME = "Login Credentials";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Main Menu");

        //startActivity(new Intent(this, MapsActivity.class));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        //FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        //frame.removeAllViews();
        //Fragment fragment = null;
        //fragment = new DetailsFragment();
        //setTitle("Profile");
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.removeAllViews();
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_find_doctors) {
            fragment = new FindDoctorsFragment();
            setTitle("Find Doctors");

        } else if (id == R.id.nav_diagnose) {
            fragment = new DiagnoseFragment();
            setTitle("Diagnose");

        } else if (id == R.id.nav_prescriptions) {
            fragment = new PrescriptionsFragment();
            setTitle("Prescriptions");

        }else if (id == R.id.nav_appointments) {
            fragment = new AppointmentsFragment();
            setTitle("Booked Appointments");

        }else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
            setTitle("About");

        } else if (id == R.id.nav_logout) {
            //fragment = new TODO();
            setTitle("Log Out");
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Toast.makeText(NavActivity.this, "Logged out.", Toast.LENGTH_SHORT).show();

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        } else if (id == R.id.nav_view) {

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
