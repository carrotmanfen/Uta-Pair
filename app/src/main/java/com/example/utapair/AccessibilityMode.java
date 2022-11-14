package com.example.utapair;

/* this class is about AccessibilityMode
* this class will collect mode
* and in can have only one object
* (singleton pattern) */
public class AccessibilityMode {
    private static AccessibilityMode instance;
    /* if do not have AccessibilityMode object new AccessibilityMode object
    * but if have AccessibilityMode object it will use that object */
    public static AccessibilityMode getInstance(){
        if (instance == null)
            instance = new AccessibilityMode();
        return instance;
    }
    /* status of mode */
    private String mode = "ACCESSIBILITY";

    /* method to get mode attribute */
    public String getMode() {
        return mode;
    }

    /* method to edit mode attribute */
    public void setMode(String mode) {
        this.mode = mode;
    }
}
