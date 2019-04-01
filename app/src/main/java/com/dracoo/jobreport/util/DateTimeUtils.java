package com.dracoo.jobreport.util;

import java.text.SimpleDateFormat;
import java.util.Date;
public class DateTimeUtils {

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
    }







}
