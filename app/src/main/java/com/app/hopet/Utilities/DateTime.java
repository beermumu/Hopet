package com.app.hopet.Utilities;

import org.joda.time.DateMidnight;
import org.joda.time.Months;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int checkMonthData(String input){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String[] slide = input.split("at");
        try {
            Date date = dateFormat.parse(slide[0]);
            dateFormat.applyPattern("yyyy-MM-dd");

            DateMidnight start = new DateMidnight(dateFormat.format(date));
            DateMidnight end = new DateMidnight(new Date());

            // Get months between these dates.
            int months = Months.monthsBetween(start, end).getMonths();

            return months;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
