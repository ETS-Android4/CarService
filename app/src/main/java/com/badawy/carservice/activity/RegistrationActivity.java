package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.badawy.carservice.R;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

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


                   // put these after auth and upload are successful
                    Intent goToLoginActivity = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(goToLoginActivity);

                }

            }
        });

        //Ahmed Tarek`s code ... Commented by mahmoud Badawy
//        editTextUsername.addTextChangedListener(loginTextWatcher);
//        editTextPhone.addTextChangedListener(loginTextWatcher); //modified last name to phone @badawy
//        editTextEmail.addTextChangedListener(loginTextWatcher);
//        editTextPassword.addTextChangedListener(loginTextWatcher);
//        editTextConfirmPassword.addTextChangedListener(loginTextWatcher);

    }

    private boolean validateData() {

        if (isEmpty(editTextUsername)) {

            editTextUsername.setError(" A username is required");
            editTextUsername.requestFocus();
            return false;

        } else if (!isEmail(editTextEmail)) {

            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;

        } else if (!isPassword(editTextPassword)) {

            editTextPassword.setError("Password must be > 8 characters and have at least 1 number");
            editTextPassword.requestFocus();
            return false;

        } else if (!isConfirmed(editTextPassword, editTextConfirmPassword)) {

            editTextConfirmPassword.setError("Please confirm the password");
            editTextConfirmPassword.requestFocus();
            return false;

        } else if (!isPhone(editTextPhone)) {

            editTextPhone.setError("Enter a valid phone number");
            editTextPhone.requestFocus();
            return false;

        } else if (!isChecked(checkBoxTermsConditions)) {

            Toast.makeText(this, "please read and accept our terms and conditions", Toast.LENGTH_SHORT).show();
            return false;

        } else {

            return true;
        }

    }

    private boolean isEmpty(EditText editText) {
        CharSequence inputText = editText.getText().toString().trim();

        return TextUtils.isEmpty(inputText);
    }

    private boolean isEmail(EditText editText) {
        CharSequence email = editText.getText().toString().trim();

        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPassword(EditText editText) {

        CharSequence password = editText.getText().toString().trim();
        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{8,24}");

        return (!TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches());
    }

    private boolean isConfirmed(EditText originalText, EditText confirmedText) {

        CharSequence originalPassword = originalText.getText().toString().trim();
        CharSequence confirmedPassword = confirmedText.getText().toString().trim();
        return (!TextUtils.isEmpty(confirmedPassword) && confirmedPassword.equals(originalPassword));
    }

    private boolean isPhone(EditText editText) {
        CharSequence phone = editText.getText().toString().trim();
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }

    private boolean isChecked(CheckBox checkBox) {
        return checkBox.isChecked();
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
