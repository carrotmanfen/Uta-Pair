package com.example.utapair;

/* this class is about ProfileUser
* it have attribute that we want to
* show in recyclerView in ProfileActivity */
public class ProfileUser {
    private int number;
    private int scoreboard;
    private String time;

    /* constructor */
    public ProfileUser(int number ,int scoreboard,String time){
        this.number=number;
        this.scoreboard=scoreboard;
        this.time=time;
    }

    /* method to get data from attribute number */
    public int getNumber() {
        return number;
    }

    /* method to get data from attribute scoreboard */
    public int getScoreboard() {
        return scoreboard;
    }

    /* method to get data from attribute time */
    public String getTime() {
        return time;
    }

    /* method to set data from attribute number */
    public void setNumber(int number) {
        this.number = number;
    }

    /* method to set data from attribute scoreboard */
    public void setScoreboard(String username) {
        this.scoreboard = scoreboard;
    }

    /* method to set data from attribute time */
    public void setTime(String time) {
        this.time = time;
    }
}
