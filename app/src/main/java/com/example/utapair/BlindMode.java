package com.example.utapair;

/* this class is about BlindMode
 * this class will collect mode
 * and in can have only one object
 * (singleton pattern) */
public class BlindMode {
    private static BlindMode instance;
    /* if do not have BlindMode object new BlindModeMode object
     * but if have BlindMode object it will use that object */
    public static BlindMode getInstance(){
        if (instance == null)
            instance = new BlindMode();
        return instance;
    }

    /* status of mode */
    private String mode = "NOT_BLIND";

    /* method to get mode attribute */
    public String getMode() {
        return mode;
    }

    /* method to edit mode attribute */
    public void setMode(String mode) {
        this.mode = mode;
    }
}
