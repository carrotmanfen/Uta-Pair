package com.example.utapair;


public class TimeScore {
    private int time;

    public void TimeScore(){
        time = 0;
    }
    /* reset time for new counting */
    public   void resetTime(){
        time = 0;
    }

    /* time running */
    public void tickTime(){
        time = time+1;
    }

    /* method to change time to millisecond, second and minute */
    public String getTimerText() {
        /* Change unite and put in to formatTime that we want */
        int milliSec = time%100;
        int second = (time/1000)%60;
        int minute = (time/1000)/60;
        return formatTime(milliSec,second,minute);
    }

    /* method get time and set in to string format that we want */
    public  String formatTime(int milliSec,int second, int minute){
        /* set string format */
        return String.format("%02d",minute)+" : "+ String.format("%02d",second)+" : "+ String.format("%02d",milliSec);
    }
}
