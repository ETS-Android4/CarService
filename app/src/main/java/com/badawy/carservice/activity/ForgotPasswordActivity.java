package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.badawy.carservice.utils.MyValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    // @Ahmed Rabea

    private EditText emailAddressET;
    private Button resetPasswordET;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initializeUi();

        // Initialize FireBase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Reset Password Email
        resetPasswordET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()) {
                    sendResetPasswordEmail();
                }
            }
        });
    }

    // Logic Methods
    private void sendResetPasswordEmail() {

        String email = emailAddressET.getText().toString().trim();

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email was sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private boolean isDataValid() {

        if (!MyValidation.isEmail(emailAddressET)) {

            emailAddressET.setError("Enter a valid email");
            emailAddressET.requestFocus();
            return false;
        } else
            return true;
    }

    // Views Methods
    private void initializeUi() {


        emailAddressET = findViewById(R.id.forgotPassword_et_email);
        resetPasswordET = findViewById(R.id.forgotPassword_btn_sendResetLink);

    }

    public void showForgotPasswordEmailKeyboard(View view) {
        MyCustomSystemUi.showKeyboard(this, emailAddressET);
    }
}
