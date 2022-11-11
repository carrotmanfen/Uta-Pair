package com.example.utapair;

public class VibrationMode {
    private static VibrationMode instance;

    public static VibrationMode getInstance(){
        if (instance == null)
            instance = new VibrationMode();
        return instance;
    }

    private String mode = "NOT_VIBRATION";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
