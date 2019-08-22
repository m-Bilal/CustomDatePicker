package com.bilal.lib.view.customdatepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.annotation.RequiresApi;

import com.bilal.lib.view.customdatepicker.R;
import com.bilal.lib.view.customdatepicker.viewModel.CustomDatePickerViewModel;

import java.util.Calendar;

public class CustomDatePicker extends FrameLayout {

    private NumberPicker mDayNumberPicker;
    private NumberPicker mMonthNumberPicker;
    private NumberPicker mYearNumberPicker;
    private CustomDatePickerViewModel viewModel;
    private Calendar minDate;
    private Calendar maxDate;

    public CustomDatePicker(Context context) {
        super(context);
        init(null);
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private View inflateXmlView() {
        View v = inflate(getContext(), R.layout.custom_date_picker, null);
        mDayNumberPicker = v.findViewById(R.id.picker_day);
        mMonthNumberPicker = v.findViewById(R.id.picker_month);
        mYearNumberPicker = v.findViewById(R.id.picker_year);
        mMonthNumberPicker.setDisplayedValues(viewModel.getMonths());
        addView(v);
        return v;
    }

    private void init(AttributeSet attributeSet) {
        viewModel = new CustomDatePickerViewModel();
        if (attributeSet == null) {
            return;
        }
        inflateXmlView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomDatePicker);
        styleView(typedArray);
        typedArray.recycle();
        setTestValues();
    }

    private void setDefaultMinAndMaxDates() {
        minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        minDate.set(Calendar.MONTH, 1);
        minDate.set(Calendar.YEAR, 1970);

        maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, 31);
        maxDate.set(Calendar.MONTH, 12);
        minDate.set(Calendar.YEAR, 2099);
    }

    public void setDate(Calendar date) {
        mDayNumberPicker.setValue(date.get(Calendar.DAY_OF_MONTH));
        mMonthNumberPicker.setValue(date.get(Calendar.MONTH));
        mYearNumberPicker.setValue(date.get(Calendar.YEAR));

        mDayNumberPicker.setMinValue(viewModel.getDaysStart(minDate, date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
        mDayNumberPicker.setMaxValue(viewModel.getDaysStart(maxDate, date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
        mMonthNumberPicker.setMinValue(viewModel.getMonthsStart(minDate, date.get(Calendar.YEAR)));
        mMonthNumberPicker.setMaxValue(viewModel.getMonthsEnd(maxDate, date.get(Calendar.YEAR)));
        mYearNumberPicker.setMinValue(minDate.get(Calendar.YEAR));
        mYearNumberPicker.setMaxValue(maxDate.get(Calendar.YEAR));
    }

    private void styleView(TypedArray typedArray) {
        boolean showDays = typedArray.getBoolean(R.styleable.CustomDatePicker_showDays, true);
        boolean showMonths = typedArray.getBoolean(R.styleable.CustomDatePicker_showMonths, true);
        boolean showYear = typedArray.getBoolean(R.styleable.CustomDatePicker_showYear, true);
        boolean wrapDaySelectorWheel = typedArray.getBoolean(R.styleable.CustomDatePicker_wrapDaySelectorWheel, true);
        boolean wrapMonthSelectorWheel = typedArray.getBoolean(R.styleable.CustomDatePicker_wrapMonthSelectorWheel, true);
        boolean wrapYearSelectorWheel = typedArray.getBoolean(R.styleable.CustomDatePicker_wrapYearSelectorWheel, true);
        int gapSize = typedArray.getDimensionPixelSize(R.styleable.CustomDatePicker_gapSize, 0);
        int selectionDividerHeight = typedArray.getDimensionPixelSize(R.styleable.CustomDatePicker_selectionDividerSize, -1);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.CustomDatePicker_textSize, -1);
        int color = typedArray.getColor(R.styleable.CustomDatePicker_textColor, -1);

        // Set visibility of views
        mDayNumberPicker.setVisibility(showDays ? VISIBLE : GONE);
        mMonthNumberPicker.setVisibility(showMonths ? VISIBLE : GONE);
        mYearNumberPicker.setVisibility(showYear ? VISIBLE : GONE);

        // Set wrapping of selector wheel
        mDayNumberPicker.setWrapSelectorWheel(wrapDaySelectorWheel);
        mMonthNumberPicker.setWrapSelectorWheel(wrapMonthSelectorWheel);
        mYearNumberPicker.setWrapSelectorWheel(wrapYearSelectorWheel);

        // Left margin for Month picker
        MarginLayoutParams monthLayoutParams = (MarginLayoutParams) mMonthNumberPicker.getLayoutParams();
        monthLayoutParams.leftMargin = gapSize;
        mMonthNumberPicker.requestLayout();

        // Left margin for Month picker
        MarginLayoutParams yearLayoutParams = (MarginLayoutParams) mYearNumberPicker.getLayoutParams();
        yearLayoutParams.leftMargin = gapSize;
        mYearNumberPicker.requestLayout();

        // Set selection divider height. NOTE: Only works on API Level 29 (Android Q) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && selectionDividerHeight != -1) {
            mDayNumberPicker.setSelectionDividerHeight(selectionDividerHeight);
            mMonthNumberPicker.setSelectionDividerHeight(selectionDividerHeight);
            mYearNumberPicker.setSelectionDividerHeight(selectionDividerHeight);
        }

        // Set text size. NOTE: Only works on API Level 29 (Android Q) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && selectionDividerHeight != -1) {
            mDayNumberPicker.setTextSize(textSize);
            mMonthNumberPicker.setTextSize(textSize);
            mYearNumberPicker.setTextSize(textSize);
        }

        // Set text color. NOTE: Only works on API Level 29 (Android Q) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && selectionDividerHeight != -1) {
            mDayNumberPicker.setTextColor(color);
            mMonthNumberPicker.setTextColor(color);
            mYearNumberPicker.setTextColor(color);
        }
    }

    private void setTestValues2() {
        setDate(Calendar.getInstance());
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
