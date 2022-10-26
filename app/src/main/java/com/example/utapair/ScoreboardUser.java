package com.example.utapair;

public class ScoreboardUser {
    private int number;
    private String username;
    private String time;

    public ScoreboardUser(int number ,String username,String time){
        this.number=number;
        this.username=username;
        this.time=time;
    }

    public int getNumber() {
        return number;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
