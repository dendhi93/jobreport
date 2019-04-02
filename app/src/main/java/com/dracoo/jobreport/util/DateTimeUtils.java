package com.dracoo.jobreport.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
public class DateTimeUtils {

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
    }

    public static int getDateDiff(String beginDate, String endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;

        try{
            d1 = format.parse(beginDate);
            d2 = format.parse(endDate);

            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            int i = (int) diffDays;
            if (i == 0){
                i = i + 1;
            }
            return i;

        }catch (Exception e){
            Log.d("###","date Err "+e.toString());
        }
        return 0;
    }







}
