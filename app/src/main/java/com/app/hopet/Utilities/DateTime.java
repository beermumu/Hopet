package com.app.hopet.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
