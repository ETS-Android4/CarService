/**

JAVA CODE
        package com.ahmed.popupwindow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.RemoteActionCompatParcelizer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.graphics.BlurMaskFilter;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.RemoteActionCompatParcelizer;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.BlurMaskFilter;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the application context
        mContext = getApplicationContext();
        // Get the activity
        mActivity = MainActivity.this;
        textView = (TextView) findViewById(R.id.tv);
        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mButton = (Button) findViewById(R.id.btn);


        // Set a click listener for the text view
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);


                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.layoutcustom, null);


                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }



                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);

            }
        });

    }

}
..........................................................................................................................
        activity_main.xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/rl"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp"
            tools:context=".MainActivity"
            android:background="#f5f1e0"
            >
<Button
        android:id="@+id/btn"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Show Popup Window"
                />
</RelativeLayout>
        ....................................................................................................................
        layoutcutom.xml


<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/rl_custom_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#C42F40"
        android:padding="2dp">

<!--   <ImageButton
        android:id="@+id/ib_close"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"

                android:layout_alignParentEnd="true"
                android:layout_alignParentRight="true"
                android:background="@null"
                />-->
<TextView
        android:id="@+id/tv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true"
                android:layout_margin="5dp"
                android:padding="100sp"
                android:text="hey there i am ahmed tarek saad and this is my first popup window" />
</RelativeLayout>
*/