/******


public class progressbar {}



//JAVA CODE

package com.ahmed.ahmedprogressbar;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

//CREATED BY AHMED TAREK..............................................................13/3/2020
public class MainActivity extends AppCompatActivity {

    private  int var1=20;//my main variable it means the persent totla of 100/total of car in this example this var is static
    private int i=1;//the index of car


    private int index=1;
    private int persent;
    private int numofcar;

    private ProgressBar progressBar;
    private EditText numberInput;
    private Button submit;
    private Button submit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//intializing our variables AHMED TAREK
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        numberInput = (EditText) findViewById(R.id.number_input);//edit text
        submit = (Button) findViewById(R.id.submit_button);
        submit1=(Button)findViewById(R.id.submit_button1);




//our action by the submit button the first button +
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to rentiate persent

                String receiver=numberInput.getText().toString().trim();
                //if the edit text is not empty do this  or excute these steps
                if (!"".equals(receiver)) {

                    numofcar = Integer.parseInt(receiver);
                    //the numofcars should be less than 100 or 100 <=100
                    persent = 100 / numofcar;


                }


                if ((persent * index) <= 100) {

                    progressBar.setProgress((persent * index));
                    index++;


                    //to make the i=5
                    //if index > numofcar
                    if (index > numofcar) {
                        index = index - 1;
                    }

                }


                // progressBar.setProgress(Integer.parseInt(numberInput.getText().toString()));
            }
        });



        //the second button of -
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
//to reintiate

                //intiate again
                persent=100/numofcar;

                if((persent*index)>persent && (persent*index)<=100) {
                    progressBar.setProgress((persent*index)-persent);
                    index--;
                }
            }

        });





    }
}












//DRAWABLE CODE.XML


..........................................................................................
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
        android:shape="ring"
        android:innerRadiusRatio="2.5"
        android:thickness="4dp"
        android:useLevel="true">

<solid android:color="@color/colorAccent" />


</shape>

        ..............................................................................................








//XMLCODE


<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="@android:dimen/app_icon_size"
        tools:context=".MainActivity">
<!-- we need constraints-->
<EditText
        android:id="@+id/number_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:hint="Enter the number of the cars"
                android:inputType="number"
                android:layout_centerVertical="true"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent"



                />

<Button
        android:id="@+id/submit_button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerHorizontal="true"
                android:text="click to +"
                tools:layout_editor_absoluteX="320dp"
                tools:layout_editor_absoluteY="0dp" />

<Button
        android:id="@+id/submit_button1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="click to -"
                app:layout_constraintEnd_toEndOf="parent"
                tools:layout_editor_absoluteY="0" />

<ProgressBar
        android:id="@+id/progressbar"
                style="?android:progressBarStyleHorizontal"
                android:layout_width="@android:dimen/notification_large_icon_height"
                android:layout_height="@android:dimen/notification_large_icon_width"
                android:progress="100"
                android:max="200"
                android:progressDrawable="@drawable/circle"
                android:rotation="-180"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

<!--  android:layout_width="@android:dimen/notification_large_icon_height"
        android:layout_height="@android:dimen/notification_large_icon_width"
        android:min=""

        -->

<!--  <ProgressBar
        android:id="@+id/progressBar"
                style="?android:attr/progressBarStyleHorizontal"
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:layout_alignParentBottom="true"
                android:layout_centerHorizontal="true"

                android:progress="100"
                android:progressDrawable="@drawable/circle" />
                -->


</androidx.constraintlayout.widget.ConstraintLayout>
......................................................................................................................

**/