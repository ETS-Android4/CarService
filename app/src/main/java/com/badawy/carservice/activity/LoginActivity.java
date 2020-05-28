package com.badawy.carservice.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badawy.carservice.R;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.badawy.carservice.utils.SharePreference;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText emailET, passwordET;
    private Button signInBtn;
    private ImageView showPasswordIcon, facebookIcon, googleIcon, twitterIcon;
    private TextView forgotPassword,signUp;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // Google Client
    private int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    // CallBack manager
    private CallbackManager callbackManager;

    // Twitter provider
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

    // Local Variables
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUi();


        //callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();



        // Firebase Auth Listener to send user to homepage
        // After logging in with any of the 4 methods [emailPSW,google,fb,twitter]
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };


        // Click Listeners
        signInBtn.setOnClickListener(this);
        facebookIcon.setOnClickListener(this);
        googleIcon.setOnClickListener(this);
        twitterIcon.setOnClickListener(this);
        showPasswordIcon.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }//END OF ON CREATE


    // Initialize Views
    private void initializeUi() {

        // setFullScreenMode();
        emailET = findViewById(R.id.login_et_email);
        passwordET = findViewById(R.id.login_et_password);
        signInBtn = findViewById(R.id.login_btn_signIn);
        signUp = findViewById(R.id.login_tv_signUp);
        showPasswordIcon = findViewById(R.id.login_icon_showPassword);
        facebookIcon = findViewById(R.id.login_img_facebook);
        googleIcon = findViewById(R.id.login_img_google);
        twitterIcon = findViewById(R.id.login_img_twitter);
        forgotPassword = findViewById(R.id.login_tv_forgotPassword);


    }



    // Email and Password Methods
    private void signInWithEmailPassword() {

        final String emailAddress = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "The Email or Password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    // Facebook Methods
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
                            // Sign in success
                            //BY Alfred 18/2/2020
                            //check user data
                            check();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }



    // Google Methods
    private void signInWithGoogle() {
        buildGoogleClient();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResult method to pass the login results to the LoginManager via callbackManager
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResultOfGoogle(task);
        }
    }

    private void handleSignInResultOfGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            passCredentialsToFirebaseAuth(account);


        } catch (ApiException e) {
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();

        }
    }

    private void passCredentialsToFirebaseAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //BY Alfred 18/2/2020
                    //check user data
                    check();

                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    // Twitter Methods
    private void signInWithTwitter() {
        mAuth.startActivityForSignInWithProvider(this, provider.build()).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        authResult.getAdditionalUserInfo().getProfile();
                        authResult.getCredential();
                        check();
                    }
                });
    }



    // Edit Text Validation
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



    //To be Modified later
    private void check() {
        FirebaseDatabase.getInstance().getReference("/Users").child(mAuth.getUid()).child("/Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //check if userID in realtime database
                //if he isn't, so save his data to realtime database
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (dataSnapshot.getValue() == null) {
                    Map<String, String> Users_map = new HashMap<>();
                    Users_map.put("Username", currentUser.getDisplayName());
                    Users_map.put("EmailAddress", currentUser.getEmail());
                    Users_map.put("PhoneNumber", currentUser.getPhoneNumber());
                    FirebaseDatabase.getInstance().getReference("/Users").child(mAuth.getUid()).setValue(Users_map);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    // Click Events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_signIn:
                if (isDataValid()) {
                    signInWithEmailPassword();
                }
                break;
            case R.id.login_img_facebook:
                signInWithFacebook();
                break;
            case R.id.login_img_google:
                signInWithGoogle();
                break;
            case R.id.login_img_twitter:
                signInWithTwitter();
                break;
            case R.id.login_icon_showPassword:
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
                break;
            case R.id.login_tv_forgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.login_tv_signUp:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
        }
    }




    //Activity life cycle
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyCustomSystemUi.clearInput(emailET);
        MyCustomSystemUi.clearInput(passwordET);
    }



    // Request Focus on Keyboard when clicking near Edit Texts
    public void showLoginEmailKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, emailET);
    }

    public void showLoginPasswordKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, passwordET);
    }

}
