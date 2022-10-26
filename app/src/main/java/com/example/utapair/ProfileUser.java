package com.example.utapair;

public class ProfileUser {
    private int number;
    private int scoreboard;
    private String time;

    public ProfileUser(int number ,int scoreboard,String time){
        this.number=number;
        this.scoreboard=scoreboard;
        this.time=time;
    }

    public int getNumber() {
        return number;
    }

    public int getScoreboard() {
        return scoreboard;
    }

    public String getTime() {
        return time;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setScoreboard(String username) {
        this.scoreboard = scoreboard;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
