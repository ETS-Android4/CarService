package com.badawy.carservice.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.badawy.carservice.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomCalendar extends ConstraintLayout implements View.OnClickListener {

    // Constant Variables
    int DAYS_AFTER_TODAY = 1;


    // Layout Views
    ImageView nextDayBtn;
    TextView dateTv;
    TextView resetDateTV;

    // Global Variables
    Calendar calendar;
    Date currentDay;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat timeFormatter;


    // Constructor
    public CustomCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }


    // Initialize Component Control
    private void initControl(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_custom_calendar, this);
        initUi();


        calendar = Calendar.getInstance();
        currentDay = calendar.getTime();
        dateFormatter = new SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH);
        timeFormatter = new SimpleDateFormat("h:mm",Locale.ENGLISH);


        dateTv.setText(dateFormatter.format(currentDay));
        resetDateTV.setOnClickListener(this);
        nextDayBtn.setOnClickListener(this);

    }


    // Initialize Views
    private void initUi() {
        resetDateTV = findViewById(R.id.customCalendar_resetDate);
        nextDayBtn = findViewById(R.id.customCalendar_nextDay);
        dateTv = findViewById(R.id.customCalendar_date);
    }


    // Reset the calender to show Today`s Date
    private void onResetDateClick() {
        calendar.setTime(currentDay);
        dateTv.setText(dateFormatter.format(currentDay));
    }

    // Get the next day from the calendar
    private void onNextDayClick() {
        calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, DAYS_AFTER_TODAY);
        dateTv.setText(dateFormatter.format(calendar.getTime()));
    }

    public String getTime(){
        return timeFormatter.format(calendar.getTime());
    }


    public String getDate() {
        return dateFormatter.format(calendar.getTime());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.customCalendar_resetDate:
                onResetDateClick();
                break;
            case R.id.customCalendar_nextDay:
                onNextDayClick();
                break;

        }

    }
}
