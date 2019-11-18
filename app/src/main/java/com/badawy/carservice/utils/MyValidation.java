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

    public static boolean isChecked(CheckBox checkBox) {
        return checkBox.isChecked();
    }

}
