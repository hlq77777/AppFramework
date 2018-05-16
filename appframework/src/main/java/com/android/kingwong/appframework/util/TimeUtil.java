package com.android.kingwong.appframework.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by KingWong on 2017/9/6.
 * 日期格式转换
 */

public class TimeUtil {
    private static final String DETAIL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FEN_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String MAIN_FORMAT = "yyyy年MM月dd";
    private static final String DOT_FORMAT = "yyyy.MM.dd";
    //    private static final long TIME_OFFSET=13*60*60*1000;//世上最好的语言PHP与java的时间戳转换相差13个小时

    public static String getTimeFormMillis(long mill){
        Date date = new Date(mill*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MAIN_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getTimeFormMillis(String mill){
        if(mill == null || mill.trim().equals("")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MAIN_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getTimeFormMillisToDot(long mill){
        Date date = new Date(mill*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DOT_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getTimeFormMillisToDot(String mill){
        if(mill == null || mill.trim().equals("")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DOT_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getPrecisionTimeFormMillis(String mill){
        if(mill == null || mill.trim().equals("")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DETAIL_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getFenTimeFormMillis(String mill){
        if(mill == null || mill.trim().equals("")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FEN_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String getPrecisionTimeFormMillis(long mills){
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DETAIL_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getMainTimeFormMillis(String mill){
        if(mill == null || mill.trim().equals("") || mill.trim().equals("0")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MAIN_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    public static String getSimpleTimeFormMillis(String mill){
        if(mill == null || mill.trim().equals("")){
            return "";
        }
        long mills = Long.parseLong(mill);
        Date date = new Date(mills*1000L);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(MAIN_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String timeChange(int time){
        int h = 0;
        int m = 0;
        int s = 0;
        int temp = time % 3600;
        if (time > 3600){
            h = time / 3600;
            if(temp != 0){
                if (temp > 60){
                    m = temp / 60;
                    if (temp % 60 != 0 ){
                        s = temp / 60;
                    }
                }else {
                    s = temp;
                }
            }
        }else {
            m = time / 60;
            if(time % 60 != 0){
                s = time % 60;
            }
        }
        return h +"时" + m +"分" + s + "秒";
    }

}
