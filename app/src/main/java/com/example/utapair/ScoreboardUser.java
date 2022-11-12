package com.example.utapair;

/* this class is about ScoreboardUser
 * it have attribute that we want to
 * show in recyclerView inn ScoreboardActivity */
public class   ScoreboardUser {
    private int number;
    private String username;
    private String time;

    /* constructor */
    public ScoreboardUser(int number ,String username,String time){
        this.number=number;
        this.username=username;
        this.time=time;
    }

    /* method to get data from attribute number */
    public int getNumber() {
        return number;
    }

    /* method to get data from attribute username */
    public String getUsername() {
        return username;
    }

    /* method to get data from attribute time */
    public String getTime() {
        return time;
    }

    /* method to set data from attribute number */
    public void setNumber(int number) {
        this.number = number;
    }

    /* method to set data from attribute username */
    public void setUsername(String username) {
        this.username = username;
    }

    /* method to set data from attribute time */
    public void setTime(String time) {
        this.time = time;
    }
}
