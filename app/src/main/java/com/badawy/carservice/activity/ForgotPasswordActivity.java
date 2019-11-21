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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText Email_address;
    private Button Reset_Password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Email_address=(EditText)findViewById(R.id.forgotpass_et_email);
        Reset_Password=(Button)findViewById(R.id.forgotpass_bu_resetpass);
        firebaseAuth=FirebaseAuth.getInstance();

        // chick email (send email to firebase)

        Reset_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String Email =Email_address.getText().toString().trim();
              if (Email.equals("")){
                  Toast.makeText(ForgotPasswordActivity.this,"please enter your email registered ID",Toast.LENGTH_SHORT).show();
              }
              else {
                  firebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(ForgotPasswordActivity.this,"Password reset email sent",Toast.LENGTH_SHORT).show();
                              finish();
                              startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                          }
                          else {
                              Toast.makeText(ForgotPasswordActivity.this,"Error in sending password reset email",Toast.LENGTH_SHORT).show();

                          }
                      }
                  });
              }
            }
        });
    }
}
