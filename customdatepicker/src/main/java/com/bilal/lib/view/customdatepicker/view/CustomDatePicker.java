package com.bilal.lib.view.customdatepicker.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.annotation.RequiresApi;

import com.bilal.lib.view.customdatepicker.R;
import com.bilal.lib.view.customdatepicker.viewModel.CustomDatePickerViewModel;

public class CustomDatePicker extends FrameLayout {

    private Context mContext;
    private View mInflatedView;
    private NumberPicker mDayNumberPicker;
    private NumberPicker mMonthNumberPicker;
    private NumberPicker mYearNumberPicker;
    private CustomDatePickerViewModel viewModel;

    public CustomDatePicker(Context context) {
        super(context);
        init(context,null);
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private View inflateXmlView() {
        View v = inflate(mContext, R.layout.custom_date_picker, null);
        mDayNumberPicker = v.findViewById(R.id.picker_day);
        mMonthNumberPicker = v.findViewById(R.id.picker_month);
        mYearNumberPicker = v.findViewById(R.id.picker_year);
        addView(v);
        return v;
    }

    private void init(Context context,AttributeSet attributeSet) {
        mContext = context;
        if (attributeSet == null) {
            return;
        }
        inflateXmlView();
        setTestValues();
    }

    private void setTestValues() {
        mYearNumberPicker.setMinValue(1970);
        mYearNumberPicker.setMaxValue(2099);
        mMonthNumberPicker.setMinValue(0);
        mMonthNumberPicker.setMaxValue(11);
        mMonthNumberPicker.setDisplayedValues(viewModel.getMonths());
        mDayNumberPicker.setMinValue(1);
        mDayNumberPicker.setMaxValue(viewModel.getDays(mMonthNumberPicker.getValue(), mYearNumberPicker.getValue()));

        mMonthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                resetDayPicker();
            }
        });
        mYearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                resetDayPicker();
            }
        });
    }

    private void resetDayPicker() {
        mDayNumberPicker.setMinValue(1);
        mDayNumberPicker.setMaxValue(viewModel.getDays(mMonthNumberPicker.getValue(), mYearNumberPicker.getValue()));
    }
}
