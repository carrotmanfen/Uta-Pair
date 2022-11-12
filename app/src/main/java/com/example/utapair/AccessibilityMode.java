package com.example.utapair;

public class AccessibilityMode {
    private static AccessibilityMode instance;

    public static AccessibilityMode getInstance(){
        if (instance == null)
            instance = new AccessibilityMode();
        return instance;
    }

    private String mode = "NOT_ACCESSIBILITY";

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
