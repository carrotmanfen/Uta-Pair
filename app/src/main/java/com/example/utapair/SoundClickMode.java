package com.example.utapair;

public class SoundClickMode {
    private static SoundClickMode instance;

    public static SoundClickMode getInstance(){
        if (instance == null)
            instance = new SoundClickMode();
        return instance;
    }

    private String mode = "NOT_SOUND";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
