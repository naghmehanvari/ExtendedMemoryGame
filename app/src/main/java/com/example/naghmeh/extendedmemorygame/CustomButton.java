package com.example.naghmeh.extendedmemorygame;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.naghmeh.extendedmemorygame.R;

/**
 * Created by naghmeh on 9/28/16.
 */
public class CustomButton extends Button
{
    private int row;
    private int column;
    private int frontImageId;
    private boolean flipped = false;
    private boolean matched = false;
    private Drawable frontImage;
    private Drawable backImage;
    private GridLayout.LayoutParams tempParams;

    public CustomButton(Context context, int row, int column, int imageID) {
        super(context);
        this.row = row;
        this.column = column;
        frontImageId = imageID;
        flipped = false;
        matched = false;
        frontImage = AppCompatDrawableManager.get().getDrawable(context, imageID);
        backImage = AppCompatDrawableManager.get().getDrawable(context, R.drawable.question);

        setBackground(backImage);

        tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));

        tempParams.width = (int) getResources().getDisplayMetrics().density * 105;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 105;

        setLayoutParams(tempParams);
    }

    public void setParams(int r, int c)
    {
        tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 105;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 105;
        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public int getFrontImageId() {
        return frontImageId;
    }

    public void flip()
    {
        YoYo.with(Techniques.RubberBand)
                .duration(700)
                .playOn(this);
        if(matched)
            return;
        if(flipped)
        {
            setBackground(backImage);
            flipped = false;
        }
        else {
            setBackground(frontImage);
            flipped = true;
        }
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
