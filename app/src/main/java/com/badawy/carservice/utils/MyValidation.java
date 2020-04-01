package com.badawy.carservice.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Mahmoud Badawy 17/11/2019
 * - Contains methods to validate different types of Fields in a registration form
 */

public class MyValidation {

    public static boolean isEmpty(EditText editText) {
        CharSequence inputText = editText.getText().toString().trim();

        return TextUtils.isEmpty(inputText);
    }

    public static boolean isEmail(EditText editText) {
        CharSequence email = editText.getText().toString().trim();

        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isPassword(EditText editText) {

        CharSequence password = editText.getText().toString().trim();
        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{8,24}");

        return (!TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches());
    }

    public static boolean isConfirmed(EditText originalText, EditText confirmedText) {

        CharSequence originalPassword = originalText.getText().toString().trim();
        CharSequence confirmedPassword = confirmedText.getText().toString().trim();
        return (!TextUtils.isEmpty(confirmedPassword) && confirmedPassword.equals(originalPassword));
    }

    public static boolean isPhone(EditText editText) {
        CharSequence phone = editText.getText().toString().trim();
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }


    //NEW MODIFICATIONS BY AHMED TAREK FOR PHONE NUMBER IN SIGN UP..... CREATED NEW 2 FUNCTIONS .....1/4/2020

    public static boolean ismyphone(EditText editText1){

        CharSequence myphone=editText1.getText().toString().trim();
        //new pattern.........{min,max} .....INTIATE A NEW PATTERN  TO ATTACH IT WITH THE OLD CONDITIONS TO MAKE THE PHONE NUMBER START WITH 01 AND MAKE THE MAX(LENGTH) OF NUMBER=11
        Pattern PHONE_PATTERN = Pattern.compile("[0-9]{11,11}");
        return (!TextUtils.isEmpty(myphone) && PHONE_PATTERN.matcher(myphone).matches() && Patterns.PHONE.matcher(myphone).matches());
    }

    public static boolean ismyphone1(EditText editText2){
        CharSequence myphone1=editText2.getText().toString().trim();
        //........ new string var to start with 01
        String phone1=editText2.getText().toString().trim();

        //new pattern.........{min,max} .....INTIATE A NEW PATTERN  TO ATTACH IT WITH THE OLD CONDITIONS TO MAKE THE PHONE NUMBER START WITH 01 AND MAKE THE MAX(LENGTH) OF NUMBER=11
        Pattern PHONE_PATTERN = Pattern.compile("[0-9]{11,11}");

        //this condition to make the phone number start with 01 as any egyptian number
        if(phone1.startsWith("01")) {
            return (!TextUtils.isEmpty(myphone1) && PHONE_PATTERN.matcher(myphone1).matches() && Patterns.PHONE.matcher(myphone1).matches());
        }
        else{return false;}

    }


    //....................RND OF NEW MODIFICATIONS


    public static boolean isChecked(CheckBox checkBox) {
        return checkBox.isChecked();
    }

}
