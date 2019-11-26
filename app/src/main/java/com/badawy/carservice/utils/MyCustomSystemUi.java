package com.badawy.carservice.utils;

import android.app.Activity;
import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.badawy.carservice.R;

/**
 * Created by Mahmoud Badawy 21/11/2019
 * - Contains different methods to handle android system configurations
 */

public class MyCustomSystemUi {


    public static void showPassword(EditText editText, ImageView icon) {

        editText.setTransformationMethod(null);
        icon.setImageResource(R.drawable.ic_eye_black);

    }

    public static void hidePassword(EditText editText, ImageView icon) {

        editText.setTransformationMethod(new PasswordTransformationMethod());
        icon.setImageResource(R.drawable.ic_eye_grey);
    }

    public static void showKeyboard(Context context, EditText targetEditText) {

        targetEditText.requestFocus();
        targetEditText.setFocusableInTouchMode(true);

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(targetEditText, InputMethodManager.SHOW_FORCED);

    }

    public static void clearInput(EditText editText){
        editText.setText("");
    }

    public static void setFullScreenMode(Activity activity) {

        View decorView = activity.getWindow().getDecorView();
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
