package com.example.utapair;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;

/* this class is about button pair in game
* it will have 2 face and it  can flip */
public class MemoryButton extends AppCompatButton {
    /* attributes */
    protected int row;
    protected int col;
    protected int columnsNum;
    protected int rowsNum;
    protected double resolutionSize;
    protected int frontDrawableId;
    protected int mode;
    protected int buttonSize;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    /* for accessibility */
    protected String symbol;
    protected String position;

    /* constructor */
    public MemoryButton(Context context, int r, int c, int frontImageId,int mode, int columnsNum,int rowsNum ,boolean blindMode){    /* give position(r,c) and id of image in drawable source of gridlayout first */
        super(context);

        this.mode = mode;
        row = r;
        col = c;
        this.rowsNum = rowsNum;
        this.columnsNum = columnsNum;
        frontDrawableId = frontImageId;     /* declare id picture for known when they matching */

        if(blindMode) {       /* if BlindMode in back is pair item disable */
            back = context.getDrawable(R.drawable.custom_pair_item_disable);
            front = context.getDrawable(R.drawable.custom_pair_item_disable);      /* set drawable in front */
        }
        else {      /* if BlindMod off in back is pair item */
            back = context.getDrawable(R.drawable.custom_pair_item);
            front = context.getDrawable(frontImageId);      /* set drawable in front */
        }
        setBackground(back);    /* set background in to back*/

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c)); /* it tell position of grid */

        /* set dimension and density*/

        /*send context of activity to find the display */
        DisplayMetricsHelper.getInstance().setHeightPixels(context);
        DisplayMetricsHelper.getInstance().setWidthPixels(context);

        /*calculate the size of card up to display of user's phone*/
        int widthButton = (int) (Math.round(DisplayMetricsHelper.getInstance().getWidthPixels() / columnsNum) - DisplayMetricsHelper.getInstance().getPixelsFromDp(20,context));;
        int heightButton = (int) (Math.round(DisplayMetricsHelper.getInstance().getHeightPixels() / rowsNum) - DisplayMetricsHelper.getInstance().getPixelsFromDp(40,context));
        /* make card square*/
        checkWidthEqualsToHeight(widthButton,heightButton);
        /*set the size for a card*/
        tempParams.width = checkWidthEqualsToHeight(widthButton,heightButton);; // (getResources().getDisplayMetrics().density * 5)
        tempParams.height = checkWidthEqualsToHeight(widthButton,heightButton);; // (getResources().getDisplayMetrics().density * 70)


        setLayoutParams(tempParams);

    }

    private int checkWidthEqualsToHeight(int widthPixels, int heightPixels){

        if(widthPixels >= heightPixels){
            return heightPixels;
        }else{
            return widthPixels;
        }
    }

    /* method to check Matched */
    public boolean isMatched(){
        return  isMatched;
    }

    /* method to setMatched */
    public void setMatched(boolean isMatched){
        this.isMatched = isMatched;
    }

    /* method to get drawable from attribute */
    public int getFrontDrawableId(){
        return frontDrawableId;
    }

    /* method to flip face of pair item */
    public void flipped(){
        /* if Matched return */
        if(isMatched){
            return;
        }
        /* isFlipped was true flipped */
        if(isFlipped){
            setBackground(back);     /* set background to back */
            isFlipped = false;    /* set flip to false */
        }
        else { /* false then flipped */
            setBackground(front);     /* set background to font */
            isFlipped = true;    /* set flip to true */
        }
    }

    /* method to set symbol */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /* method to get data from attribute symbol */
    public String getSymbol(){
        return this.symbol;
    }

    /* method to set position row and position column */
    public void setPosition(int row, int col) {
        this.position = "row " + String.valueOf(row) + " column " + String.valueOf(col);
    }

    /* method to get data from attribute position */
    public String getPosition(){
        return this.position;
    }

    /* method to set background */
    public void setBackGroundButton(Drawable background){
        setBackground(background);
    }


}
