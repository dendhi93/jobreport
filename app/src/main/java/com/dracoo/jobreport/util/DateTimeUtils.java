package com.dracoo.jobreport.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
public class DateTimeUtils {

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
    }

    public static String getChangeDateFormat(String dateTime){
        try {
            SimpleDateFormat source = new SimpleDateFormat("yyy-MM-dd, HH:mm:ss", java.util.Locale.getDefault());
            Date dateSource = source.parse(dateTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy; HH:mm", java.util.Locale.getDefault());
            if (dateSource != null){
                return dateFormat.format(dateSource);
            }else{
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static String getChangeMonthFormat(String dateTime){
        try {
            SimpleDateFormat source = new SimpleDateFormat("yyy-MM-dd, HH:mm:ss", java.util.Locale.getDefault());
            Date dateSource = source.parse(dateTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MMMM-dd, HH:mm:ss", java.util.Locale.getDefault());
            if (dateSource != null){
                return dateFormat.format(dateSource);
            }else{
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static int getDateDiff(String beginDate, String endDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

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

    public static String nameOfDay(String datePick){
        try{
            SimpleDateFormat inFormat = new SimpleDateFormat("yyy-MM-dd, HH:mm:ss", java.util.Locale.getDefault());
            Date date = inFormat.parse(datePick);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", java.util.Locale.getDefault());
            return outFormat.format(date);
        }catch (Exception e){
            Log.d("###",""+e.toString());
            return "";
        }
        //TO DO HRS DI CEK
    }





}
