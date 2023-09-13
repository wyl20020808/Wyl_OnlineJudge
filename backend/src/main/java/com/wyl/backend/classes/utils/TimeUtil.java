package com.wyl.backend.classes.utils;

import cn.hutool.core.date.DateTime;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {
    public static String getNewTimeString(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    public static Timestamp getNewTimestamp(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }
    public static DateTime getNewDateTime(){
        Date date = new Date();
        return new DateTime(date.getTime());
    }
}
