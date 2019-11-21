package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.badawy.carservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private Button signInBtn;
    private ImageView showPasswordIcon, facebookIcon, googleIcon, twitterIcon;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    String d7k = "d7k";
    private TextView forgot_passowrd; //forgot password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgot_passowrd=(TextView) findViewById(R.id.login_tv_forgotPassword); //forgot password

        initializeUi();


        // Initialize FireBase Auth
        mAuth = FirebaseAuth.getInstance();

        //Sign In Authentication
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write sign in Authentication here inside this if statement
                if (isDataValid()) {
                    signIn();
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


        //Facebook Authentication
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Google Authentication
        googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Twitter Authentication
        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //forgot password
        forgot_passowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

    }


    // Logic Methods
    private void signIn() {

        String emailAddress = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent goToLoginActivity = new Intent(LoginActivity.this, HomepageActivity.class);
                            startActivity(goToLoginActivity);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private boolean isDataValid() {

        if (!MyValidation.isEmail(emailET)) {

            emailET.setError("Enter a valid email");
            emailET.requestFocus();
            return false;

        } else if (!MyValidation.isPassword(passwordET)) {

            passwordET.setError("Password is not correct");
            passwordET.requestFocus();
            return false;

        } else {

            return true;
        }

    }




    // Views Methods
    private void initializeUi() {

        // setFullScreenMode();
        emailET = findViewById(R.id.login_et_email);
        passwordET = findViewById(R.id.login_et_password);
        signInBtn = findViewById(R.id.login_btn_signIn);
        showPasswordIcon = findViewById(R.id.login_icon_showPassword);
        facebookIcon = findViewById(R.id.login_img_facebook);
        googleIcon = findViewById(R.id.login_img_google);
        twitterIcon = findViewById(R.id.login_img_twitter);

    }

    public void goToRegistrationActivity(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void goToForgotPasswordActivity(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    public void showLoginEmailKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, emailET);
    }

    public void showLoginPasswordKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, passwordET);
    }
}
