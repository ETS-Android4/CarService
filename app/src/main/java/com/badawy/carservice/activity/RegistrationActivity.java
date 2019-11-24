package com.badawy.carservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badawy.carservice.R;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {


    /**
     * uploaded by ahmed tarek....16/11/2019......
     * Modified by Mahmoud Badawy 17/11/2019
     */


    private EditText usernameET;
    private EditText phoneET;
    private EditText emailET;
    private EditText passwordET;
    private EditText confirmPasswordET;
    private Button createAccountBTN;
    private CheckBox termsConditionsCHBX;
    private ImageView showPasswordIcon, showConfirmPasswordIcon;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

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


    }


    // ahmed tarek`s code .. @badawy
    private void ahmedTarekCode() {

        //Ahmed Tarek`s code ... Commented by mahmoud Badawy
//        usernameET.addTextChangedListener(loginTextWatcher);
//        phoneET.addTextChangedListener(loginTextWatcher); //modified last name to phone @badawy
//        emailET.addTextChangedListener(loginTextWatcher);
//        passwordET.addTextChangedListener(loginTextWatcher);
//        confirmPasswordET.addTextChangedListener(loginTextWatcher);
//
//        TextWatcher loginTextWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //new variables
//                String usernameInput = usernameET.getText().toString().trim();
//                String emailInput = emailET.getText().toString().trim();
//                String password = passwordET.getText().toString().trim();
//                String confirmPasswordInput = confirmPasswordET.getText().toString().trim();
//                String phoneInput = phoneET.getText().toString().trim(); //modified last name to phone @badawy
//
//                //the function is here ahmed.......!!
//                //modified  && !lastnameinput.isEmpty()  to phone @badawy
//                createAccountBTN.setEnabled(!usernameInput.isEmpty() && !emailInput.isEmpty() && !password.isEmpty() && !confirmPasswordInput.isEmpty() && !phoneInput.isEmpty());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        };

    }


    // Logic Methods
    private void createAccount() {

        final String username = usernameET.getText().toString().trim();
        final String emailAddress = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String phoneNumber = passwordET.getText().toString().trim();


        //@AhmedMahmoud RealTime Database
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String user_id = currentUser.getUid();
                                DatabaseReference userId = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                Map<String, String> Users_map = new HashMap<>();
                                Users_map.put("Username", username);
                                Users_map.put("EmailAddress", emailAddress);
                                Users_map.put("PhoneNumber", phoneNumber);
                                userId.setValue(Users_map);
                                Intent goToLoginActivity = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(goToLoginActivity);
                                finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationActivity.this, "Registration failed",
                                    Toast.LENGTH_SHORT).show();

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

        } else if (!MyValidation.isChecked(termsConditionsCHBX)) {

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
