package com.badawy.carservice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.badawy.carservice.R;
import com.badawy.carservice.fragment.NavGarageFragment;
import com.badawy.carservice.fragment.NavHelpFragment;
import com.badawy.carservice.fragment.NavHomeFragment;
import com.badawy.carservice.fragment.NavSettingsFragment;
import com.badawy.carservice.utils.SharePreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText emailET , passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initializeUi();



        //Add Click listener to Navigation list
        navigationView.setNavigationItemSelectedListener(this);



        //To Start the Home Fragment once the activity starts
        replaceFragment(new NavHomeFragment());
        navigationView.setCheckedItem(R.id.nav_home);



        //To Be Continued @Ahmed mahmoud
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }


    }


    private void initializeUi(){

        drawerLayout = findViewById(R.id.homepage_drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
    }

    @Override
    public void onBackPressed() {

        //Closing the Navigation drawer if it was open when clicking on the back button
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    //To take actions when an item is clicked in the navigation list
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                replaceFragment(new NavHomeFragment());
                break;
            case R.id.nav_garage:
                replaceFragment(new NavGarageFragment());
                break;
            case R.id.nav_settings:
                replaceFragment(new NavSettingsFragment());
                break;
            case R.id.nav_help:
                replaceFragment(new NavHelpFragment());
                break;
            case R.id.nav_signOut:
                //Toast.makeText(this, "sign out", Toast.LENGTH_SHORT).show();
                //AhmedRabie
                //sharepreference to log out
                SharePreference.SavePassword("",this);
                SharePreference.SaveEmail("",this);
                Intent intent=new Intent(HomepageActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //To open the navigation list from inside the Fragments
    //Called inside fragment class
    public static void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .commit();


    }

}
