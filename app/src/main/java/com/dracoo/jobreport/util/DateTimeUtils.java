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
    }

    public static String InaNameOfDay(String englishDay){
        try{
            if (englishDay.trim().equals("Sunday")){return "Minggu";
            }else if (englishDay.trim().equals("Monday")){ return "Senin";
            }else if (englishDay.trim().equals("Tuesday")){ return "Selasa";
            }else if (englishDay.trim().equals("Wednesday")){ return "Rabu";
            }else if (englishDay.trim().equals("Thursday")){ return "Kamis";
            }else if (englishDay.trim().equals("Friday")){ return "Jumat";
            }else if (englishDay.trim().equals("Saturday")){ return "Sabtu"; }
        }catch(Exception e){
            Log.d("###",""+e.toString());
            return englishDay;
        }
        return englishDay;
    }

    public static String nameOfMonth(String stMonth){

        if (stMonth.equals("01")){ return "Januari";
        }else if (stMonth.equals("02")){ return "Februari"; }
        else if (stMonth.equals("03")){ return "Maret"; }
        else if (stMonth.equals("04")){ return "April"; }
        else if (stMonth.equals("05")){ return "Mei"; }
        else if (stMonth.equals("06")){ return "Juni"; }
        else if (stMonth.equals("07")){ return "Juli"; }
        else if (stMonth.equals("08")){ return "Agustus"; }
        else if (stMonth.equals("09")){ return "September"; }
        else if (stMonth.equals("10")){ return "Oktober"; }
        else if (stMonth.equals("11")){ return "November"; }
        else if (stMonth.equals("12")){ return "Desember"; }

        return "";
    }

    public static String TerbilangKonvert(long lngTerbilang){
        String[] bilangan = {" ", "Satu", "Dua", "Tiga", "Empat", "Lima",
                "Enam", "Tujuh", "Delapan", "Sembilan", "Sepuluh", "Sebelas"};

        if (lngTerbilang < 12){
            return "" + bilangan[(int)lngTerbilang];
        }else if(lngTerbilang < 20){
            return TerbilangKonvert(lngTerbilang - 10) + " Belas ";
        }else if(lngTerbilang < 100){
            return (TerbilangKonvert(lngTerbilang / 10) + " Puluh ") +  TerbilangKonvert(lngTerbilang % 10);
        }else if(lngTerbilang < 200 ){
            return "Seratus" + TerbilangKonvert(lngTerbilang - 100);
        } else if(lngTerbilang < 1000){
            return (TerbilangKonvert(lngTerbilang / 100) + " Ratus " ) + TerbilangKonvert(lngTerbilang % 100);
        } else if(lngTerbilang < 2000){
            return "Seribu" + TerbilangKonvert(lngTerbilang - 1000);
        } else if(lngTerbilang < 1000000){
            return (TerbilangKonvert(lngTerbilang /1000) + " Ribu ") + TerbilangKonvert(lngTerbilang % 1000);
        }
        return "";
    }







}
