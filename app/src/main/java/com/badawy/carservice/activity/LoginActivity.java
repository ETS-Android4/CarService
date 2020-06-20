package com.badawy.carservice.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.badawy.carservice.R;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // providers IDs Do Not Change them
    private final String GOOGLE_PROVIDER = "google.com";
    private final String FACEBOOK_PROVIDER = "facebook.com";
    private final String TWITTER_PROVIDER = "twitter.com";


    // Views
    private EditText emailET, passwordET;
    private Button signInBtn;
    private ImageView showPasswordIcon, facebookIcon, googleIcon, twitterIcon;
    private TextView forgotPassword, signUp;


    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;


    // Google Client
    private int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    // CallBack manager
    private CallbackManager callbackManager;
    private ProgressBar progressBar;

    // Twitter provider
    OAuthProvider.Builder provider = OAuthProvider.newBuilder(TWITTER_PROVIDER);

    // Local Variables
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.login_progressbar);
        initializeUi();


        //callbackManager to handle login responses
        callbackManager = CallbackManager.Factory.create();


        // Firebase Auth Listener to send user to homepage
        // After logging in with any of the 4 methods [emailPSW,google,fb,twitter]
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
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
        progressBar.setVisibility(View.VISIBLE);
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
                        mAuth.addAuthStateListener(firebaseAuthListener);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
    }




    // if user used google or FB or twitter , then check to see if its the first time
    //  if it is the first time.. then create an account for him with all the available data we can get from providers
    // if not .. then direct him to the auth listener
    private void checkFirstTimeSignIn(final String provider, final String userID, final FirebaseUser user) {
        progressBar.setVisibility(View.VISIBLE);
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.USERS);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(userID)) {
                    createUserProfile(provider, userID, user);
                } else {
                    hideProgress();
                    mAuth.addAuthStateListener(firebaseAuthListener);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Create account for user from the data we have taken from providers
    private void createUserProfile(String provider, String userID, FirebaseUser user) {

        currentUser = user;


        // prepare user profile object
        UserProfileModel userProfileObject = new UserProfileModel();

        if (provider.equals(GOOGLE_PROVIDER)) {

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String userName = acct.getDisplayName();
                String email = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();

                userProfileObject.setEmailAddress(email);
                userProfileObject.setUserName(userName);
                userProfileObject.setUserId(userID);
//                userProfileObject.setPhoneNumber("");
                if (personPhoto != null) {
                    userProfileObject.setProfileImageUri(personPhoto.toString());
                }

//                else {
//                    userProfileObject.setProfileImageUri("");
//                }
//                userProfileObject.setAddress("");

            }
        } else if (provider.equals(FACEBOOK_PROVIDER) || provider.equals(TWITTER_PROVIDER)) {

            String userName = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            String personPhoto = "";
            String phoneNumber = "";

            userProfileObject.setEmailAddress(email);
            userProfileObject.setUserName(userName);
            userProfileObject.setUserId(userID);

//            if (currentUser.getPhoneNumber() != null) {
//                phoneNumber = currentUser.getPhoneNumber();
//            }
//
//            if (currentUser.getPhotoUrl() != null) {
//                personPhoto = currentUser.getPhotoUrl().toString();
//            }
            if (currentUser.getPhotoUrl()!=null){
                userProfileObject.setProfileImageUri(currentUser.getPhotoUrl().toString());
            }
            if (currentUser.getPhoneNumber()!=null){
                userProfileObject.setPhoneNumber(currentUser.getPhoneNumber().toString());
            }
//            userProfileObject.setPhoneNumber(phoneNumber);
//            userProfileObject.setProfileImageUri(personPhoto);
//            userProfileObject.setAddress("");

        }

        // prepare firebase root
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.USERS)
                .child(userID);


        userRef.child(Constants.USER_CARS).setValue("0");
        userRef.child(Constants.APPOINTMENTS_ORDERS).child(Constants.APPOINTMENTS).setValue("0");
        userRef.child(Constants.APPOINTMENTS_ORDERS).child(Constants.ORDERS).setValue("0");
        userRef.child(Constants.SHOPPING_CART).setValue("0");

        // add user profile to database
        userRef.child(Constants.USER_PROFILE)
                .setValue(userProfileObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mAuth.getCurrentUser() != null) {
                            hideProgress();
                            mAuth.addAuthStateListener(firebaseAuthListener);
                        }
                    }
                });
    }




    // Facebook Methods
    private void signInWithFacebook() {
        // @alfred

        showProgress();
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
                        hideProgress();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        hideProgress();
                        Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        //@alfred
        //to get an access token for the signed-in user, put it in FireBase then auth
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userID = user.getUid();
                                checkFirstTimeSignIn(FACEBOOK_PROVIDER, userID, user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            hideProgress();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }




    // Google Methods
    private void signInWithGoogle() {
        showProgress();
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
            showProgress();
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
            hideProgress();
//            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();

        }
    }

    private void passCredentialsToFirebaseAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userID = user.getUid();
                        checkFirstTimeSignIn(GOOGLE_PROVIDER, userID, user);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication Failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




    // Twitter Methods
    private void signInWithTwitter() {
        mAuth.startActivityForSignInWithProvider(this, provider.build()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String userID = user.getUid();
                        checkFirstTimeSignIn(TWITTER_PROVIDER, userID, user);
                    }
                } else {
                    hideProgress();
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
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


    // Progress Bar
    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
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
    protected void onStop() {
        super.onStop();
        if (null != firebaseAuthListener) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != firebaseAuthListener) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            mAuth.addAuthStateListener(firebaseAuthListener);
        }
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
