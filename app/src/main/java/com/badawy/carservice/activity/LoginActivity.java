package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;




import com.badawy.carservice.utils.MyValidation;
import com.badawy.carservice.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private Button signInBtn;
    private ImageView  facebookIcon, googleIcon, twitterIcon;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUi();
        //callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();
        // Initialize FireBase Auth
        mAuth = FirebaseAuth.getInstance();

        //Signing Authentication
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write sign in Authentication here inside this if statement
                if (validateData()) {

                    String emailAddress = emailET.getText().toString().trim();
                    String password = passwordET.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = mAuth.getCurrentUser();

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

            }
        });

        //@alfred
        //Facebook Authentication
        facebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                handleFacebookAccessToken(loginResult.getAccessToken());


                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });
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
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    }
    //@alfred
    //to get an access token for the signed-in user ,but it in firebase then auth
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
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
    //@alfred
    // onActivityResult method to pass the login results to the LoginManager via callbackManager
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initializeUi() {

        // hideSystemUI();
        emailET = findViewById(R.id.login_et_email);
        passwordET = findViewById(R.id.login_et_password);
        signInBtn = findViewById(R.id.login_btn_signIn);
        facebookIcon = findViewById(R.id.login_img_facebook);
        googleIcon = findViewById(R.id.login_img_google);
        twitterIcon = findViewById(R.id.login_img_twitter);

    }

    private boolean validateData() {

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

    private void hideSystemUI() {

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



    public void goToRegistrationActivity(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }


    public void goToForgotPasswordActivity(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }
}
