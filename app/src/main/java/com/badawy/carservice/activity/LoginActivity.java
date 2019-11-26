package com.badawy.carservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badawy.carservice.R;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private Button signInBtn;
    private ImageView showPasswordIcon, facebookIcon, googleIcon, twitterIcon;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    private CallbackManager callbackManager;
    int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUi();
        //@AhmedMahmoud GooGleSignIn
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);


        //callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();

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
                signInWithFacebook();

            }
        });

        //Google Authentication
        googleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();

            }
        });


        //Twitter Authentication
        twitterIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        // not completed yet .. @badawy to @alfred
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    }//END OF ON CREATE

    //@AhmedMahmoud GooGleSignIn
    private void SignInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //@alfred
    //@AhmedMahmoud GooGleSignIn
    // onActivityResult method to pass the login results to the LoginManager via callbackManager.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a lis    tener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //@AhmedMahmoud GooGleSignIn
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Sign In Successfully",
                    Toast.LENGTH_SHORT).show();
            firebaseAuthWithGoogle(account);


        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Sign In Faild",
                    Toast.LENGTH_SHORT).show();

        }
    }

    //@AhmedMahmoud GooGleSignIn
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successfuly",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent goToLoginActivity = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(goToLoginActivity);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Faild",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }





    // Logic Methods
    //@AhmedMahmoud SignIn
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

    private void signInWithFacebook() {
        // @alfred
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));

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

    private void handleFacebookAccessToken(AccessToken token) {
        //@alfred
        //to get an access token for the signed-in user, put it in FireBase then auth
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
