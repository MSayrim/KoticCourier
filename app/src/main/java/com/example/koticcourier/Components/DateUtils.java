package com.example.koticcourier.Components;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtils {

    //Sipariş oluşturulunca otomatik olarak doldurulan tarihi burada alıyoruz
    public final static String getDateString(String dateStr){
        Date strDate = getDateFromString(dateStr);
        if (strDate != null){
            return getDateString(strDate);
        }
        return "";
    }

    //Aynı gün içerisindeyse farklı formatlarda tarihi dönüyoruz.
    public final static String getDateString(Date date){
        String dateFormat = "";

        if (isSameDay(date, Calendar.getInstance().getTime())){
            dateFormat = "HH:mm";
        }else{
            dateFormat = "HH:mm dd/MM/yyyy";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        String dateStr = df.format(date);
        return dateStr;
    }

    //Stackoverflowdan implement.

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static Date getDateFromString(String dateStr){
        SimpleDateFormat formatter =new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date date = formatter.parse(dateStr);
            return date;
        }catch (ParseException parseException){

        }
        return null;
    }
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(cal.getTime());
    }
}