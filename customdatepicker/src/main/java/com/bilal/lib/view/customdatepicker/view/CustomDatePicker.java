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

    public static final String TAG = "CustomDatePicker";

    private NumberPicker mDayNumberPicker;
    private NumberPicker mMonthNumberPicker;
    private NumberPicker mYearNumberPicker;
    private CustomDatePickerViewModel viewModel;
    private Calendar minDate;
    private Calendar maxDate;
    private Calendar curDate;
    private OnDateChangedListener onDateChangedListener;

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

        mDayNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                curDate.set(Calendar.DAY_OF_MONTH, newVal);
                if (onDateChangedListener != null) {
                    onDateChangedListener.onDateChanged(curDate);
                }
            }
        });

        mMonthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                curDate.set(Calendar.MONTH, newVal);
                setCorrectRangeToScroller();
                if (onDateChangedListener != null) {
                    onDateChangedListener.onDateChanged(curDate);
                }
            }
        });

        mYearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                curDate.set(Calendar.YEAR, newVal);
                setCorrectRangeToScroller();
                if (onDateChangedListener != null) {
                    onDateChangedListener.onDateChanged(curDate);
                }
            }
        });
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
        setDefaultDates();
    }

    private void setDefaultDates() {
        curDate = Calendar.getInstance();

        minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        minDate.set(Calendar.MONTH, 1);
        minDate.set(Calendar.YEAR, 1970);

        maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, 31);
        maxDate.set(Calendar.MONTH, 12);
        maxDate.set(Calendar.YEAR, curDate.get(Calendar.YEAR) + 100);

        setCorrectRangeToScroller();
        setDate();
    }

    public void setDate(Calendar date) throws DateRangeException {
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MILLISECOND, 0);
        curDate = date;
        if (curDate.compareTo(minDate) < 0 || curDate.compareTo(maxDate) > 0) {
            throw new DateRangeException();
        }
        setCorrectRangeToScroller();
        setDate();
    }

    private void setDate() {
        mDayNumberPicker.setValue(curDate.get(Calendar.DAY_OF_MONTH));
        mMonthNumberPicker.setValue(curDate.get(Calendar.MONTH));
        mYearNumberPicker.setValue(curDate.get(Calendar.YEAR));
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

    public void setMinDateSafe(Calendar date) throws DateRangeException {
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(maxDate) > 0) {
            throw new DateRangeException();
        }
        if (date.compareTo(curDate) > 0) {
            curDate.setTime(date.getTime());
            if (onDateChangedListener != null) {
                onDateChangedListener.onDateChanged(curDate);
            }
        }
        minDate.setTime(date.getTime());
        setCorrectRangeToScroller();
    }

    public void setMaxDateSafe(Calendar date) throws DateRangeException {
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(minDate) < 0) {
            throw new DateRangeException();
        }
        if (date.compareTo(curDate) < 0) {
            curDate.setTime(date.getTime());
            if (onDateChangedListener != null) {
                onDateChangedListener.onDateChanged(curDate);
            }
        }
        maxDate.setTime(date.getTime());
        setCorrectRangeToScroller();
    }

    public void addOnDateChangedListener(OnDateChangedListener listener) {
        onDateChangedListener = listener;
    }

    public void showDays(boolean showDays) {
        if (showDays) {
            mDayNumberPicker.setVisibility(VISIBLE);
        } else {
            mDayNumberPicker.setVisibility(GONE);
        }
    }

    public void showMonths(boolean showMonths) {
        if (showMonths) {
            mMonthNumberPicker.setVisibility(VISIBLE);
        } else {
            mMonthNumberPicker.setVisibility(GONE);
        }
    }

    public void showYears(boolean showYears) {
        if (showYears) {
            mYearNumberPicker.setVisibility(VISIBLE);
        } else {
            mYearNumberPicker.setVisibility(GONE);
        }
    }

    public void wrapDays(boolean wrapDays) {
        mDayNumberPicker.setWrapSelectorWheel(wrapDays);
        mDayNumberPicker.invalidate();
    }

    public void wrapMonths(boolean wrapMonths) {
        mMonthNumberPicker.setWrapSelectorWheel(wrapMonths);
        mMonthNumberPicker.invalidate();
    }

    public void wrapYears(boolean wrapYears) {
        mYearNumberPicker.setWrapSelectorWheel(wrapYears);
        mYearNumberPicker.invalidate();
    }

    public Calendar getCurDate() {
        return curDate;
    }

    public Calendar getMinDate() {
        return minDate;
    }

    public void setMinDate(Calendar date) {
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(maxDate) > 0) {
            return;
        }
        if (date.compareTo(curDate) > 0) {
            curDate.setTime(date.getTime());
            if (onDateChangedListener != null) {
                onDateChangedListener.onDateChanged(curDate);
            }
        }
        minDate.setTime(date.getTime());
        setCorrectRangeToScroller();
    }

    public Calendar getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Calendar date) {
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(minDate) < 0) {
            return;
        }
        if (date.compareTo(curDate) < 0) {
            curDate.setTime(date.getTime());
            if (onDateChangedListener != null) {
                onDateChangedListener.onDateChanged(curDate);
            }
        }
        maxDate.setTime(date.getTime());
        setCorrectRangeToScroller();
    }

    private void setCorrectRangeToScroller() {
        int month = curDate.get(Calendar.MONTH);
        int year = curDate.get(Calendar.YEAR);
        mDayNumberPicker.setMinValue(viewModel.getDaysStart(minDate, month, year));
        mDayNumberPicker.setMaxValue(viewModel.getDaysEnd(maxDate, month, year));
        mMonthNumberPicker.setDisplayedValues(viewModel.getMonths(minDate, curDate));
        mMonthNumberPicker.setMinValue(viewModel.getMonthsStart(minDate, year));
        mMonthNumberPicker.setMaxValue(viewModel.getMonthsEnd(maxDate, year));
        mYearNumberPicker.setMinValue(minDate.get(Calendar.YEAR));
        mYearNumberPicker.setMaxValue(maxDate.get(Calendar.YEAR));
    }

    public interface OnDateChangedListener {
        void onDateChanged(Calendar newDate);
    }

    public class DateRangeException extends Exception {
        @Override
        public String toString() {
            return "Date object out of range";
        }
    }
}
