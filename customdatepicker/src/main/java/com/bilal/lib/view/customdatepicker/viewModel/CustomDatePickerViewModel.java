package com.bilal.lib.view.customdatepicker.viewModel;

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

    public int getDays(int month, int year) {
        if (isLeapYear(year)) {
            return DAYS_LEAP_YEAR[month];
        } else {
            return DAYS[month];
        }
    }

    public String[] getMonths() {
        return MONTHS;
    }
}