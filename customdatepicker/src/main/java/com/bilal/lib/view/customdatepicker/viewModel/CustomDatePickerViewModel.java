package com.bilal.lib.view.customdatepicker.viewModel;

import java.util.Calendar;

public class CustomDatePickerViewModel {

    private final int[] DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final int[] DAYS_LEAP_YEAR = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private final String[] MONTHS = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getDaysStart(Calendar minDate, int month, int year) {
        return minDate.get(Calendar.YEAR) == year && month == minDate.get(Calendar.MONTH) ?
                minDate.get(Calendar.DAY_OF_MONTH) : 1;
    }

    public int getDaysEnd(Calendar maxDate, int month, int year) {
        return maxDate.get(Calendar.YEAR) == year && month == maxDate.get(Calendar.MONTH) ?
                maxDate.get(Calendar.DAY_OF_MONTH) : (isLeapYear(year) ? DAYS_LEAP_YEAR[month] :
                DAYS[month]);
    }

    public int getMonthsStart(Calendar minDate, int year) {
        return minDate.get(Calendar.YEAR) == year ? minDate.get(Calendar.MONTH) : 0;
    }

    public int getMonthsEnd(Calendar maxDate, int year) {
        return maxDate.get(Calendar.YEAR) == year ? maxDate.get(Calendar.MONTH) : 11;
    }

    public String[] getMonths() {
        return MONTHS;
    }

    public String[] getMonths(Calendar minDate, int curYear) {
        if (curYear == minDate.get(Calendar.YEAR)) {
            return buildMonthsArray(minDate.get(Calendar.MONTH), 11);
        } else {
            return getMonths();
        }
    }

    private String[] buildMonthsArray(int start, int end) {
        String[] array = new String[12];
        for (int i = 0; i < 12; i++) {
            array[i] = MONTHS[(start + i) % 12];
        }
        return array;
    }
}