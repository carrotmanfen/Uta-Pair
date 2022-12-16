package com.example.utapair;

import java.util.Random;

public class ButtonGraphics {
    private int[] temp;
    private String[] tempString;
    private int[] buttonGraphicLocation;

    /* set buttonGraphic level easy */
    private int[] getButtonGraphicEasy(){
        temp = new int[3];
        temp[0] = R.drawable.custom_pair_brownies;
        temp[1] = R.drawable.custom_pair_cake;
        temp[2] = R.drawable.custom_pair_candy;
        return temp;

    }
    /* set text buttonGraphic level easy */
    private String[] getTextButtonEasy(){
        tempString = new String[3];
        tempString[0] = "brownies";
        tempString[1] = "cakes";
        tempString[2] = "candy";
        return tempString;

    }
    /* set buttonGraphic level normal */
    private int[] getButtonGraphicNormal(){
        temp = new int[6];
        temp[0] = R.drawable.custom_pair_brownies;
        temp[1] = R.drawable.custom_pair_cake;
        temp[2] = R.drawable.custom_pair_candy;
        temp[3] = R.drawable.custom_pair_chocolate;
        temp[4] = R.drawable.custom_pair_cookie;
        temp[5] = R.drawable.custom_pair_donut;
        return temp;

    }
    /* set text buttonGraphic level normal */
    private String[] getTextButtonNormal(){
        tempString = new String[6];
        tempString[0] = "brownies";
        tempString[1] = "cakes";
        tempString[2] = "candy";
        tempString[3] = "chocolate";
        tempString[4] = "cookies";
        tempString[5] = "donut";
        return tempString;

    }
    /* set buttonGraphic level hard */
    private int[] getButtonGraphicHard(){
        temp = new int[9];
        temp[0] = R.drawable.custom_pair_brownies;
        temp[1] = R.drawable.custom_pair_cake;
        temp[2] = R.drawable.custom_pair_candy;
        temp[3] = R.drawable.custom_pair_chocolate;
        temp[4] = R.drawable.custom_pair_cookie;
        temp[5] = R.drawable.custom_pair_donut;
        temp[6] = R.drawable.custom_pair_icecream;
        temp[7] = R.drawable.custom_pair_macaron;
        temp[8] = R.drawable.custom_pair_pancake;
        return temp;
    }
    /* set text buttonGraphic level hard */
    private String[] getTextButtonHard(){
        tempString = new String[9];
        tempString[0] = "brownies";
        tempString[1] = "cakes";
        tempString[2] = "candy";
        tempString[3] = "chocolate";
        tempString[4] = "cookies";
        tempString[5] = "donut";
        tempString[6] = "ice cream";
        tempString[7] = "macaroon";
        tempString[8] = "pancake";
        return tempString;

    }

    /* method set buttonGraphic all level */
    public int[] getButtonGraphic(int mode){
        switch (mode){
            case -1: /* easy mode */
                /*set easy graphic of a card*/
                return getButtonGraphicEasy();
            case 0: /* normal mode */
                /*set normal graphic of a card*/
                return getButtonGraphicNormal();
            case 1: /* hard mode */
                /*set hard graphic of a card*/
                return getButtonGraphicHard();
        }
        return new int[0];
    }
    /* method set text for a card*/
    public String[] getButtonGraphicText(int mode){
        switch (mode){
            case -1: /* easy mode */
                /*set text for easy card*/
                return getTextButtonEasy();
            case 0: /* normal mode */
                /*set text for normal card*/
                return getTextButtonNormal();

            case 1: /* hard mode */
                /*set text for hard card*/
                return getTextButtonHard();
        }
        return new String[0];
    }

    /* method to random location */
    public int[] shuffleButtonGraphics(int numberOfElements){
        Random rand = new Random();
        buttonGraphicLocation = new int[numberOfElements];
        for (int i = 0 ; i < numberOfElements ; i++){
            /* mod by unique value because it have different picture (3 picture and  6 pair) */
            buttonGraphicLocation[i] = i % (numberOfElements/2);
        }
        for(int i = 0; i < numberOfElements ; i++){
            int temp = buttonGraphicLocation[i];
            int randIndex = rand.nextInt(6); /* 0-5 random int number */

            /* swap number of buttonGraphic */
            buttonGraphicLocation[i] = buttonGraphicLocation[randIndex];
            buttonGraphicLocation[randIndex] = temp;

        }
        return buttonGraphicLocation;
    }


}
