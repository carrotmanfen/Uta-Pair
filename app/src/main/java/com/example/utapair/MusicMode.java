package com.example.utapair;
/* this class is about MusicMode
 * this class will collect mode
 * and in can have only one object
 * (singleton pattern) */


public class MusicMode {
    private static MusicMode instance;
    /* if do not have MusicMode object new MusicModeMode object
     * but if have MusicMode object it will use that object */
    public static MusicMode getInstance(){
        if (instance == null)
            instance = new MusicMode();
        return instance;
    }

    /* status of mode */
    private String mode = "MUSIC";

    /* method to get mode attribute */
    public String getMode() {
        return mode;
    }

    /* method to edit mode attribute */
    public void setMode(String mode) {
        this.mode = mode;
    }
}
