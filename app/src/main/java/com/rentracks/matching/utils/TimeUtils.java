package com.rentracks.matching.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by HuuLoc on 5/30/17.
 */


public final class TimeUtils {


    public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);
    public static final SimpleDateFormat FORMAT_HOUR = new SimpleDateFormat("HH:mm", Locale.JAPAN);
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd-MM-yyyy", Locale.JAPAN);

    private TimeUtils() {

    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return FORMAT_DATE_TIME.format(calendar.getTime());
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        return format.format(calendar.getTime());
    }

    public static Calendar getCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(FORMAT_DATE_TIME.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static Calendar getCalendar(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static String getHour(Calendar calendar) {
        return FORMAT_HOUR.format(calendar.getTime());
    }

    public static String getDate(Calendar calendar) {
        return FORMAT_DATE.format(calendar.getTime());
    }

    public static boolean isToday(Calendar calendar) {
        Calendar calendarToday = Calendar.getInstance();
        return calendarToday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && calendarToday.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }
    public static Date createDate(String time, String timeformat){
        if(time == null){
            return null;
        }
        Date result = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(timeformat, Locale.JAPAN);
            result = format1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String getTimeBetweenTwoday(String starttime, String endtime){
        if(starttime == null || endtime == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+09:00'", Locale.JAPAN);
        Date fromdate = null,todate = null;
        try{
            fromdate = sdf.parse(starttime);
            todate = sdf.parse(endtime);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return getTimeBetweenTwoday(fromdate, todate);
    }
    public static String getTimeBetweenTwoday(Date starttime, Date endtime){
        long duration  = endtime.getTime() - starttime.getTime();
//        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration)%60;
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        if(diffInHours > 0){
            if(diffInMinutes > 0) {
                return diffInHours + "時間" + diffInMinutes+"分";
            }else{
                return diffInHours+"時間";
            }
        }else{
            return diffInMinutes+"分";
        }
    }
    public static String changeDateFormat(Date date, String newFormat){
        SimpleDateFormat format2 = new SimpleDateFormat(newFormat);
        return format2.format(date);
    }
    //return false if the time is still before current time
    public static boolean compareWithCurrentTime(Date time){
        Calendar calendar = Calendar.getInstance();
        Date curTime=calendar.getTime();
        long duration  = curTime.getTime() - time.getTime();
        if(duration>=0)
            return false;
        else
            return true;

    }
}