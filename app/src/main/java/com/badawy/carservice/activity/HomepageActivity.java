package com.badawy.carservice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.badawy.carservice.R;
import com.badawy.carservice.fragment.NavCarsFragment;
import com.badawy.carservice.fragment.NavAppointmentsFragment;
import com.badawy.carservice.fragment.NavHomeFragment;
import com.badawy.carservice.fragment.NavSettingsFragment;
import com.badawy.carservice.fragment.NavShoppingCartFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // time in milliseconds to press back again to exit
    private static final int EXIT_APP_TIME_INTERVAL = 2000;
    private long backButtonPressed;
    private static Toast exitToast;

    private static DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        initializeUi();

        // Initialize Firebase Auth Object
        firebaseAuth = FirebaseAuth.getInstance();

        //Add Click listener to Navigation list
        navigationView.setNavigationItemSelectedListener(this);


        //To Start the Home Fragment once the activity starts
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home).setChecked(true));


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


    private void initializeUi() {

        drawerLayout = findViewById(R.id.homepage_drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
    }

    @Override
    public void onBackPressed() {
        // Know which fragment is currently Displayed
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homepage_fragment_container);

        // if nav drawer is open >> close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // if nav cars is open >> back to homepage
        else if (currentFragment instanceof NavCarsFragment) {
            openHomepage();
        }
        // if nav Appointments and Orders is open >> back to homepage
        else if (currentFragment instanceof NavAppointmentsFragment) {
            openHomepage();
        }
        // if nav Shopping Cart is open >> back to homepage
        else if (currentFragment instanceof NavShoppingCartFragment) {
            openHomepage();
        }
        // if nav Settings and Account is open >> back to homepage
        else if (currentFragment instanceof NavSettingsFragment) {
            openHomepage();
        }
        // if Homepage is open and sub fragments are open >> back from fragments
        else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        }
        // if Homepage is open and no sub fragments are open >> exit the application after pressing back button twice in 2 seconds
        else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (backButtonPressed + EXIT_APP_TIME_INTERVAL > System.currentTimeMillis()) {
                exitToast.cancel(); // stop toast after exiting the application
                super.onBackPressed();
            } else {
                showExitMessage();
            }
            backButtonPressed = System.currentTimeMillis();
        }


    }


    //To take actions when an item is clicked in the navigation list
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {


            case R.id.nav_home:
                clearBackStack();
                replaceFragment(new NavHomeFragment());

                break;


            case R.id.nav_cars:
                clearBackStack();
                replaceFragment(new NavCarsFragment());
                break;


            case R.id.nav_settings:
                clearBackStack();
                replaceFragment(new NavSettingsFragment());
                break;


            case R.id.nav_appointments:
                clearBackStack();
                replaceFragment(new NavAppointmentsFragment());
                break;


            case R.id.nav_shopping_cart:
                clearBackStack();
                replaceFragment(new NavShoppingCartFragment());
                break;


            case R.id.nav_signOut:
                firebaseAuth.signOut();
                Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            default:
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //To open the navigation list from inside the Fragments
    //Called inside multiple fragment classes
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

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void openHomepage() {
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home).setChecked(true));
    }

    // Used in Spare Parts Shop to Open the Shopping Cart
    public void openShoppingCart() {
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_shopping_cart).setChecked(true));
    }

    private void showExitMessage() {
        exitToast = Toast.makeText(getApplicationContext(), "Tab back button again to exit", Toast.LENGTH_SHORT);
        exitToast.show();
    }
}