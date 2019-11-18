package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.badawy.carservice.R;
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


    private EditText editTextUsername;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonCreateAccount;
    private CheckBox checkBoxTermsConditions;
    private ImageView iconShowPassword, iconShowConfirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeUi();


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write Create Authentication and Upload to Realtime database here inside this if statement
                if (validateData()) {

                    final String username = editTextUsername.getText().toString().trim();
                    final String emailAddress = editTextEmail.getText().toString().trim();
                    final String password = editTextPassword.getText().toString().trim();
                    final String phoneNumber = editTextPassword.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        // Sign in success, update UI with the signed-in user's information
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        DatabaseReference userid = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                        Map<String,String> Users_map=new HashMap<>();
                                        Users_map.put("Username",username);
                                        Users_map.put("EmailAddress",emailAddress);
                                        Users_map.put("PhoneNumber",phoneNumber);
                                        userid.setValue(Users_map);
                                        Intent goToLoginActivity = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(goToLoginActivity);
                                        finish();
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistrationActivity.this, "Registration failed",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });



                }

            }
        });


        iconShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Password Code
            }
        });


        iconShowConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show confirm Password Code
            }
        });
        mAuth = FirebaseAuth.getInstance();

        //Ahmed Tarek`s code ... Commented by mahmoud Badawy
//        editTextUsername.addTextChangedListener(loginTextWatcher);
//        editTextPhone.addTextChangedListener(loginTextWatcher); //modified last name to phone @badawy
//        editTextEmail.addTextChangedListener(loginTextWatcher);
//        editTextPassword.addTextChangedListener(loginTextWatcher);
//        editTextConfirmPassword.addTextChangedListener(loginTextWatcher);

    }

    private boolean validateData() {

        if (MyValidation.isEmpty(editTextUsername)) {

            editTextUsername.setError(" A username is required");
            editTextUsername.requestFocus();
            return false;

        } else if (!MyValidation.isEmail(editTextEmail)) {

            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;

        } else if (!MyValidation.isPassword(editTextPassword)) {

            editTextPassword.setError("Password must be > 8 characters and have at least 1 number");
            editTextPassword.requestFocus();
            return false;

        } else if (!MyValidation.isConfirmed(editTextPassword, editTextConfirmPassword)) {

            editTextConfirmPassword.setError("Please confirm the password");
            editTextConfirmPassword.requestFocus();
            return false;

        } else if (!MyValidation.isPhone(editTextPhone)) {

            editTextPhone.setError("Enter a valid phone number");
            editTextPhone.requestFocus();
            return false;

        } else if (!MyValidation.isChecked(checkBoxTermsConditions)) {

            Toast.makeText(this, "please read and accept our terms and conditions", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            return true;
        }

    }


    private void initializeUi() {


        // hideSystemUI();

        //first step
        editTextUsername = findViewById(R.id.registration_et_username); // modified first name to username @badawy
        editTextEmail = findViewById(R.id.registration_et_emailAddress);
        editTextPassword = findViewById(R.id.registration_et_password);
        editTextConfirmPassword = findViewById(R.id.registration_et_confirmPassword);
        buttonCreateAccount = findViewById(R.id.registration_btn_createAccount);
        editTextPhone = findViewById(R.id.registration_et_phone); //modified last name to phone @badawy
        checkBoxTermsConditions = findViewById(R.id.registration_cb_termsCheck);
        iconShowPassword = findViewById(R.id.registration_icon_showPassword);
        iconShowConfirmPassword = findViewById(R.id.registration_icon_showConfirmPassword);


    }

    // ahmed tarek`s code .. @badawy
    private void AhmedTarekCode() {
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //new variables
                String usernameInput = editTextUsername.getText().toString().trim();
                String emailInput = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPasswordInput = editTextConfirmPassword.getText().toString().trim();
                String phoneInput = editTextPhone.getText().toString().trim(); //modified last name to phone @badawy

                //the function is here ahmed.......!!
                //modified  && !lastnameinput.isEmpty()  to phone @badawy
                buttonCreateAccount.setEnabled(!usernameInput.isEmpty() && !emailInput.isEmpty() && !password.isEmpty() && !confirmPasswordInput.isEmpty() && !phoneInput.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void showLoginActivity(View view) {
        finish();
    }
}
