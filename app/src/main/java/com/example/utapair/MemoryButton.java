package com.example.utapair;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;


public class MemoryButton extends AppCompatButton {
    // attributes
    protected int row;
    protected int col;
    protected int frontDrawableId;

    protected boolean isFilpped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    // constructor
    public MemoryButton(Context context, int r, int c, int frontImageId){ // give position(r,c) and id of image in drawable source of gridlayout first
        super(context);

        row = r;
        col = c;
        frontDrawableId = frontImageId; // declare id picture for known when they matching

        front = context.getDrawable(frontImageId);
        back = context.getDrawable(R.drawable.custom_pair_item); // ตรงนี้ต้องใส่ไปเลยให้ findviewbyId ของรูป background

        setBackground(back);

        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r),GridLayout.spec(c)); // it tell position of grid

        tempParams.width = (int) getResources().getDisplayMetrics().density * 120;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 120;

        setLayoutParams(tempParams);
    }
    public boolean isMatched(){
        return  isMatched;
    }
    public void setMatched(boolean isMatched){
        this.isMatched = isMatched;
    }
    public int getFrontDrawableId(){
        return frontDrawableId;
    }
    public void filpped(){
        if(isMatched){
            return;
        }
        if(isFilpped){ // true -> filpped
            setBackground(back);
            isFilpped = false;
        }
        else { // false -> flipped
            setBackground(front);
            isFilpped = true;
        }
    }

}
