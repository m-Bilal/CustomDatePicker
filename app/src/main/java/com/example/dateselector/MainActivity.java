package com.example.dateselector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bilal.lib.view.customdatepicker.view.CustomDatePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePicker datePicker;
    Button setMinDateButton;
    Button setCurDateButton;
    Button setMaxDateButton;
    CheckBox showDaysCheckbox;
    CheckBox wrapDaysCheckbox;
    CheckBox showMonthsCheckbox;
    CheckBox wrapMonthsCheckbox;
    CheckBox showYearCheckbox;
    CheckBox wrapYearCheckbox;
    CustomDatePicker customDatePicker;
    TextView curDateTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViewComponents();
    }

    private void initialiseViewComponents() {
        datePicker = findViewById(R.id.date_picker);
        setMinDateButton = findViewById(R.id.button_set_min_date);
        setMaxDateButton = findViewById(R.id.button_set_max_date);
        setCurDateButton = findViewById(R.id.button_set_cur_date);
        showDaysCheckbox = findViewById(R.id.checkbox_show_days);
        wrapDaysCheckbox = findViewById(R.id.checkbox_wrap_days);
        showMonthsCheckbox = findViewById(R.id.checkbox_show_months);
        wrapMonthsCheckbox = findViewById(R.id.checkbox_wrap_months);
        showYearCheckbox = findViewById(R.id.checkbox_show_year);
        wrapYearCheckbox = findViewById(R.id.checkbox_wrap_year);
        curDateTextview = findViewById(R.id.textview_cur_date);
        customDatePicker = findViewById(R.id.custom_date_picker);

        setMinDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = getCalendar(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                customDatePicker.setMinDate(date);
            }
        });

        setMaxDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = getCalendar(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                customDatePicker.setMaxDate(date);
            }
        });

        setCurDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = getCalendar(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                try {
                    customDatePicker.setDate(date);
                } catch (CustomDatePicker.DateRangeException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        showDaysCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.showDays(b);
            }
        });

        showMonthsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.showMonths(b);
            }
        });

        showYearCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.showYears(b);
            }
        });

        wrapDaysCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.wrapDays(b);
            }
        });

        wrapMonthsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.wrapMonths(b);
            }
        });

        wrapYearCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                customDatePicker.wrapYears(b);
            }
        });

        customDatePicker.addOnDateChangedListener(new CustomDatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(Calendar newDate) {
                String date = "" + newDate.get(Calendar.DAY_OF_MONTH);
                date += " / " + (newDate.get(Calendar.MONTH) + 1);
                date += " / " + newDate.get(Calendar.YEAR);
                curDateTextview.setText(date);
            }
        });
    }

    private Calendar getCalendar(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }
}
