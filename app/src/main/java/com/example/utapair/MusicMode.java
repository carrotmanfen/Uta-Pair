package com.example.utapair;

public class MusicMode {
    private static MusicMode instance;

    public static MusicMode getInstance(){
        if (instance == null)
            instance = new MusicMode();
        return instance;
    }

    private String mode = "NOT_MUSIC";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
