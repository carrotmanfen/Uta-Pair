package com.example.utapair;

public class BlindMode {
    private static BlindMode instance;

    public static BlindMode getInstance(){
        if (instance == null)
            instance = new BlindMode();
        return instance;
    }

    private String mode = "NOT_BLIND";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
