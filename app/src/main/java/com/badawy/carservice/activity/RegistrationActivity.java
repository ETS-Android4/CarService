package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import java.util.Scanner;


import com.badawy.carservice.R;

public class RegistrationActivity extends AppCompatActivity {

    //uploaded by ahmed tarek..................
    private EditText editTextfirstname;
    private EditText editTextlastname;
    private EditText editTextemail;
    private EditText editTextpassword;
    private EditText editTextconfirmpassword;
    private Button   buttoncreateaccount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        //first step

        editTextfirstname=findViewById(R.id.registration_et_firstName);
        editTextlastname=findViewById(R.id.registration_et_lastName);
        editTextemail=findViewById(R.id.registration_et_emailAddress);
        editTextpassword=findViewById(R.id.registration_et_Password);
        editTextconfirmpassword=findViewById(R.id.registration_et_confirmPassword);
        buttoncreateaccount=findViewById(R.id.registration_btn_createAccount);

        editTextfirstname.addTextChangedListener(LoginTextwatcher);
        editTextlastname.addTextChangedListener(LoginTextwatcher);
        editTextemail.addTextChangedListener(LoginTextwatcher);
        editTextpassword.addTextChangedListener(LoginTextwatcher);
        editTextconfirmpassword.addTextChangedListener(LoginTextwatcher);
       // buttoncreateaccount.addTextChangedListener();

    }
    private TextWatcher LoginTextwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //new variables
            String fistnameinput=editTextfirstname.getText().toString().trim();
            String lastnameinput=editTextlastname.getText().toString().trim();
            String emailinput=editTextemail.getText().toString().trim();
            String password=editTextpassword.getText().toString().trim();
            String confirmoasswordinput=editTextconfirmpassword.getText().toString().trim();
            //the function is here ahmed.......!!
            buttoncreateaccount.setEnabled(!fistnameinput.isEmpty() && !lastnameinput.isEmpty() && !emailinput.isEmpty() && !password.isEmpty() && !confirmoasswordinput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
