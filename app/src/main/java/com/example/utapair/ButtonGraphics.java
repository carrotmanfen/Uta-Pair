package com.example.utapair;

public class ButtonGraphics {
    private int[] temp;
    private  String[] tempString;

    /* set buttonGraphic level easy */
    public int[] getButtonGraphicEasy(){
        temp = new int[3];
        temp[0] = R.drawable.custom_pair_brownies;
        temp[1] = R.drawable.custom_pair_cake;
        temp[2] = R.drawable.custom_pair_candy;
        return temp;

    }
    /* set text buttonGraphic level easy */
    public String[] getTextButtonEasy(){
        tempString = new String[3];
        tempString[0] = "brownies";
        tempString[1] = "cakes";
        tempString[2] = "candy";
        return tempString;

    }
    /* set buttonGraphic level normal */
    public int[] getButtonGraphicNormal(){
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
    public String[] getTextButtonNormal(){
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
    public int[] getButtonGraphicHard(){
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
    public String[] getTextButtonHard(){
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
}
