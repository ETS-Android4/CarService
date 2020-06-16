package com.badawy.carservice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.badawy.carservice.models.AboutUsDataModel;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // time in milliseconds to press back again to exit
    private static final int EXIT_APP_TIME_INTERVAL = 2000;
    private long backButtonPressed;
    private static Toast exitToast;
    private GoogleSignInClient mGoogleSignInClient;
    private static DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    private TextView navUserName, navUserEmail;
    private CircleImageView navProfileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initializeUi();

        buildGoogleClient();

        // Initialize Firebase Auth Object
        firebaseAuth = FirebaseAuth.getInstance();

        //Add Click listener to Navigation list
        navigationView.setNavigationItemSelectedListener(this);


        //To Start the Home Fragment once the activity starts
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home).setChecked(true));

        // Initialize Shared Preferences
        MySharedPreferences.init(getApplicationContext());

        // Fetch user`s Data and Cars From Firebase
        fetchUserProfileFromFirebase();
        fetchUserCarsFromFirebase();

        // Fetch About us Data
        fetchAboutUsDataFromFirebase();


        //To Be Continued @Ahmed mahmoud
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

          //  Toast.makeText(this, personName + personFamilyName + personEmail + personId, Toast.LENGTH_LONG).show();

        }


    }



    // [[ FIREBASE DATA RETRIEVAL ]]
    private void fetchUserProfileFromFirebase() {

        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.USERS)
                .child(userId)
                .child(Constants.USER_PROFILE);

        dbRef.keepSynced(true);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileModel userProfileObject = dataSnapshot.getValue(UserProfileModel.class);
                saveUserDataIntoSharedPreference(userProfileObject);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchUserCarsFromFirebase() {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.USERS)
                .child(userId)
                .child(Constants.USER_CARS);
        dbRef.keepSynced(true);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Intent intent = new Intent(HomepageActivity.this, AddCarActivity.class);
                    HomepageActivity.this.startActivity(intent);
                } else {
                    ArrayList<CarModel> carList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()
                    ) {
                        carList.add(ds.getValue(CarModel.class));

                    }

                    saveUserCarsIntoSharedPreference(carList);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchAboutUsDataFromFirebase() {
        // initialize Firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA);

       //  Retrieve data from firebase
        dbRef.child(Constants.ABOUT_US).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AboutUsDataModel obj = dataSnapshot.getValue(AboutUsDataModel.class);

                if (obj != null) {
                    saveAboutUsDataIntoSharedPreference( obj);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    // [[ HANDLE USER`S DATA ]]
    private void saveUserDataIntoSharedPreference(UserProfileModel userData) {
        Gson gson = new Gson();
        String userSerializedData = gson.toJson(userData);
        MySharedPreferences.write(MySharedPreferences.USER_DATA, userSerializedData);

        bindUserDataIntoNavHeader();
    }

    private void bindUserDataIntoNavHeader() {
        Gson gson = new Gson();
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");
        if (!userSerializedData.equals("")) {
            UserProfileModel userData = gson.fromJson(userSerializedData, UserProfileModel.class);
            // Profile image
            if (userData.getProfileImageUri().equals("")){
                navProfileImg.setImageResource(R.drawable.ic_default_profile);
            }
            else{
                Glide.with(this).load(userData.getProfileImageUri()).into(navProfileImg);
            }

            navUserName.setText(userData.getUserName());
            navUserEmail.setText(userData.getEmailAddress());

        } else
            Toast.makeText(this, "Race Condition", Toast.LENGTH_SHORT).show();

    }


    // [[ HANDLE USER`S CARS ]]
    private void saveUserCarsIntoSharedPreference(ArrayList<CarModel> carList) {
        Gson gson = new Gson();
        String userSerializedCars = gson.toJson(carList);
        MySharedPreferences.write(MySharedPreferences.USER_CARS, userSerializedCars);

    }


    // [[ HANDLE ABOUT US DATA ]]
    private void saveAboutUsDataIntoSharedPreference(AboutUsDataModel obj) {
        Gson gson = new Gson();
        String serializedAboutUsData = gson.toJson(obj);
        MySharedPreferences.write(MySharedPreferences.ABOUT_US,serializedAboutUsData);
    }


    // init Ui
    private void initializeUi() {

        drawerLayout = findViewById(R.id.homepage_drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        View navHeaderView = navigationView.getHeaderView(0);
        navUserName = navHeaderView.findViewById(R.id.nav_userName);
        navUserEmail = navHeaderView.findViewById(R.id.nav_email);
        navProfileImg = navHeaderView.findViewById(R.id.nav_profileImage);
    }

    // [[ HANDLE NAVIGATION ]]
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
                mGoogleSignInClient.signOut();
                MySharedPreferences.remove(MySharedPreferences.USER_DATA);
                MySharedPreferences.remove(MySharedPreferences.USER_CARS);
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

    // Used in Check Out to Open the Settings Tab
    public void openSettings() {
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_settings).setChecked(true));
    }


    private void showExitMessage() {
        exitToast = Toast.makeText(getApplicationContext(), "Tab back button again to exit", Toast.LENGTH_SHORT);
        exitToast.show();
    }

    private void buildGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
}