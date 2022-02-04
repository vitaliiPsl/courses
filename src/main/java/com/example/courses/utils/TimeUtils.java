package com.example.courses.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {
    public static String calculateDuration(LocalDateTime start, LocalDateTime end){

        long duration = Math.abs(Duration.between(start, end).toMinutes());
        if(duration < 60){
            return duration + " minutes";
        } else if(duration / 60 < 24){
            return duration / 60 + " hours";
        } else if(duration / 60 / 24 < 7){
            return duration / 60 / 24 + " days";
        } else if(duration / 60 / 24 < 30){
            return duration / 60 / 24 / 7 + " weeks";
        } else {
            return duration / 60 / 24 / 30 + " months";
        }
    }
}
