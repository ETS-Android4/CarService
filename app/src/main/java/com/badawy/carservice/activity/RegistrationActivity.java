package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.badawy.carservice.R;

public class RegistrationActivity extends AppCompatActivity {

    //uploaded by ahmed tarek..................
    private EditText editTextusername;
    private EditText editTextphone;
    private EditText editTextemail;
    private EditText editTextpassword;
    private EditText editTextconfirmpassword;
    private Button   buttoncreateaccount;
    private TextView registration_tv_signIn; // badawy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        hideSystemUI(); //@badawy


        //first step
        editTextusername =findViewById(R.id.registration_et_username); // modified first name to username
        editTextemail=findViewById(R.id.registration_et_emailAddress);
        editTextpassword=findViewById(R.id.registration_et_password);
        editTextconfirmpassword=findViewById(R.id.registration_et_confirmPassword);
        buttoncreateaccount=findViewById(R.id.registration_btn_createAccount);
        editTextphone =findViewById(R.id.registration_et_phone); //modified last name to phone @badawy


        registration_tv_signIn = findViewById(R.id.registration_tv_signIn);//badawy
        registration_tv_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        editTextusername.addTextChangedListener(LoginTextwatcher);
        editTextphone.addTextChangedListener(LoginTextwatcher); //modified last name to phone @badawy
        editTextemail.addTextChangedListener(LoginTextwatcher);
        editTextpassword.addTextChangedListener(LoginTextwatcher);
        editTextconfirmpassword.addTextChangedListener(LoginTextwatcher);
        //buttoncreateaccount.addTextChangedListener();

    }
    private TextWatcher LoginTextwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //new variables
            String fistnameinput= editTextusername.getText().toString().trim();
            String emailinput=editTextemail.getText().toString().trim();
            String password=editTextpassword.getText().toString().trim();
            String confirmoasswordinput=editTextconfirmpassword.getText().toString().trim();
            String phoneinput=editTextphone.getText().toString().trim(); //modified last name to phone @badawy

            //the function is here ahmed.......!!
            //modified  && !lastnameinput.isEmpty()  to phone @badawy
            buttoncreateaccount.setEnabled(!fistnameinput.isEmpty() && !emailinput.isEmpty() && !password.isEmpty() && !confirmoasswordinput.isEmpty() && !phoneinput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
           };


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

}
