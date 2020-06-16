package com.badawy.carservice.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badawy.carservice.R;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {


    private static final String TAG = "Registration Activity";
    /**
     * uploaded by ahmed tarek....16/11/2019......
     * Modified by Mahmoud Badawy 17/11/2019
     */


    private EditText usernameET,phoneET,emailET,passwordET,confirmPasswordET;
    private Button createAccountBTN;
    private CheckBox termsConditionsCHBX;
    private ImageView showPasswordIcon, showConfirmPasswordIcon;
    private FirebaseAuth mAuth;
    private String userUID;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    /**
     * link of terms and conditions MODIFIED BY AHMED TAREK 26/11/2019..............
     */
    private TextView conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeUi();



        // Initialize FireBase Auth
        mAuth = FirebaseAuth.getInstance();


        // Create Account
        createAccountBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDataValid()) {
                    createAccount();
                }

            }
        });

        //Show Password
        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MyValidation.isEmpty(passwordET)) {

                    if (!isPasswordVisible) {

                        // show password and change icon to black eye
                        MyCustomSystemUi.showPassword(passwordET, showPasswordIcon);
                        isPasswordVisible = !isPasswordVisible;

                    } else {

                        // hide password and change icon to grey eye
                        MyCustomSystemUi.hidePassword(passwordET, showPasswordIcon);
                        isPasswordVisible = !isPasswordVisible;

                    }
                }

            }
        });

        //Show Confirm Password
        showConfirmPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MyValidation.isEmpty(confirmPasswordET)) {

                    if (!isConfirmPasswordVisible) {

                        // show password and change icon to black eye
                        MyCustomSystemUi.showPassword(confirmPasswordET, showConfirmPasswordIcon);
                        isConfirmPasswordVisible = !isConfirmPasswordVisible;

                    } else {

                        // hide password and change icon to grey eye
                        MyCustomSystemUi.hidePassword(confirmPasswordET, showConfirmPasswordIcon);
                        isConfirmPasswordVisible = !isConfirmPasswordVisible;

                    }

                }
            }
        });

        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistrationActivity.this, "terms and conditions", Toast.LENGTH_SHORT).show();
            }


            //............................................................................. END THE LINK OF CONDITIONS AND TERMS

        });



    }

    // Logic Methods
    private void createAccount() {

        final String username = usernameET.getText().toString().trim();
        final String emailAddress = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String phoneNumber = phoneET.getText().toString().trim();


        //@AhmedMahmoud RealTime Database
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // After the user is created Successfully
                            // Get user UID
                            userUID = mAuth.getUid();

                            // Sign out the user immediately from Auth listener to prevent the listener from directing the user to homepage
                            mAuth.signOut();

                            // prepare user profile object
                            UserProfileModel userProfileObject = new UserProfileModel();
                            userProfileObject.setEmailAddress(emailAddress);
                            userProfileObject.setUserName(username);
                            userProfileObject.setEmailAddress(emailAddress);
                            userProfileObject.setUserId(userUID);
                            userProfileObject.setPhoneNumber(phoneNumber);
                            userProfileObject.setProfileImageUri("");
                            userProfileObject.setAddress("");


                            // prepare firebase root
                            DatabaseReference userId = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child(Constants.USERS)
                                    .child(userUID);



                            userId.child(Constants.USER_CARS).setValue("0");
                            userId.child(Constants.APPOINTMENTS_ORDERS).child(Constants.APPOINTMENTS).setValue("0");
                            userId.child(Constants.APPOINTMENTS_ORDERS).child(Constants.ORDERS).setValue("0");
                            userId.child(Constants.SHOPPING_CART).setValue("0");

                            // add user profile to database
                            userId.child(Constants.USER_PROFILE)
                                    .setValue(userProfileObject)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent goToLoginActivity = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(goToLoginActivity);
                                            finish();
                                        }
                                    });



                        } else if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(RegistrationActivity.this, "Email Already Exist .. Please log in ", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });


    }

    private boolean isDataValid() {

        if (MyValidation.isEmpty(usernameET)) {

            usernameET.setError(" A username is required");
            usernameET.requestFocus();
            return false;

        } else if (!MyValidation.isEmail(emailET)) {

            emailET.setError("Enter a valid email");
            emailET.requestFocus();
            return false;

        } else if (!MyValidation.isPassword(passwordET)) {

            passwordET.setError("Password must be > 8 characters and have at least 1 number");
            passwordET.requestFocus();
            return false;

        } else if (!MyValidation.isConfirmed(passwordET, confirmPasswordET)) {

            confirmPasswordET.setError("Please confirm the password");
            confirmPasswordET.requestFocus();
            return false;

        } else if (!MyValidation.isPhone(phoneET)) {

            phoneET.setError("Enter a valid phone number");
            phoneET.requestFocus();
            return false;

        }


        //NEW MODIFICATIONS BY AHMED TAREK FOR PHNONE NUMBER 1/4/2020
        else if (!MyValidation.ismyphone(phoneET)) {

            phoneET.setError("The Phone Number Must Contain 11 Digits ");
            phoneET.requestFocus();
            return false;
        } else if (!MyValidation.ismyphone1(phoneET)) {
            phoneET.setError("The Phone Number Must Start With 01");
            phoneET.requestFocus();
            return false;

        }

//END OF NEW MODIFICATIONS


        else if (!MyValidation.isChecked(termsConditionsCHBX)) {

            Toast.makeText(this, "please read and accept our terms and conditions", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            return true;
        }

    }


    // Views Methods
    private void initializeUi() {


        //  MyCustomSystemUi.setFullScreenMode(this);

        //first step
        usernameET = findViewById(R.id.registration_et_username); // modified first name to username @badawy
        emailET = findViewById(R.id.registration_et_emailAddress);
        passwordET = findViewById(R.id.registration_et_password);
        confirmPasswordET = findViewById(R.id.registration_et_confirmPassword);
        createAccountBTN = findViewById(R.id.registration_btn_createAccount);
        phoneET = findViewById(R.id.registration_et_phone); //modified last name to phone @badawy
        termsConditionsCHBX = findViewById(R.id.registration_cb_termsCheck);
        showPasswordIcon = findViewById(R.id.registration_icon_showPassword);
        showConfirmPasswordIcon = findViewById(R.id.registration_icon_showConfirmPassword);
        conditions = findViewById(R.id.registration_tv_terms);


        //conditions and terms link ....................................................\
        conditions.setPaintFlags(conditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



    }

    public void showLoginActivity(View view) {
        finish();
    }

    public void showUsernameKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, usernameET);
    }

    public void showEmailKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, emailET);
    }

    public void showPasswordKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, passwordET);
    }

    public void showConfirmPasswordKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, confirmPasswordET);
    }

    public void showPhoneKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, phoneET);
    }
}
