package com.example.naghmeh.extendedmemorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.naghmeh.extendedmemorygame.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;


public class Game extends AppCompatActivity{

    private int totalBoxes;
    private int[] listOfImageIds;
    private CustomButton[] listOfButtons = new CustomButton[20];
    private ArrayList<Integer> shuffledImgIds = new ArrayList<>();
    private CustomButton firstButtonClicked;
    private CustomButton secondButtonClicked;
    private boolean busy = false;
    private int score = 0;
    private GridLayout grid;
    int numberOfRows;
    int numberOfColumns;

    static final String SCORE_STATE = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState!= null)
        {
            score = savedInstanceState.getInt(SCORE_STATE);
        }
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setup();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    //This function creates a shuffled list of image IDs
    private void shuffle()
    {
        int n = 0, counter = 0;
        while(n != 20)
        {
            if(counter == 10)
                counter = 0;
            shuffledImgIds.add(listOfImageIds[counter]);
            counter++;
            n++;
        }

        Collections.shuffle(shuffledImgIds);

    }
    public void setup()
    {
        //Setting up Grid
        grid = ButterKnife.findById(this, R.id.memoryGrid);
        numberOfRows = grid.getRowCount();
        numberOfColumns = grid.getColumnCount();
        totalBoxes =  numberOfRows * numberOfColumns; // We have 20
        listOfImageIds = new int[totalBoxes/2]; // 10 elements


        // let's populate our list with the images' IDs
        listOfImageIds[0] = R.drawable.img1;
        listOfImageIds[1] = R.drawable.img2;
        listOfImageIds[2] = R.drawable.img3;
        listOfImageIds[3] = R.drawable.img4;
        listOfImageIds[4] = R.drawable.img5;
        listOfImageIds[5] = R.drawable.img6;
        listOfImageIds[6] = R.drawable.img7;
        listOfImageIds[7] = R.drawable.img8;
        listOfImageIds[8] = R.drawable.img9;
        listOfImageIds[9] = R.drawable.img10;

        shuffle();
        int index = 0;
        for(int r = 0; r < numberOfRows; r++) {
            for (int c = 0; c < numberOfColumns; c++) {
                CustomButton cb = new CustomButton(this, r, c, shuffledImgIds.get(index));

                cb.setId(View.generateViewId());
                listOfButtons[index] = cb;
                grid.addView(cb);
                index++;
                cb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CustomButton cb = (CustomButton) v;

                        if (busy)
                            return;
                        if (cb.isMatched())  // if the button has already been matched, ignore and do nothing
                            return;

                        if (firstButtonClicked == null) {
                            firstButtonClicked = cb;
                            firstButtonClicked.flip();
                            return;
                        }

                        if (firstButtonClicked.getId() == cb.getId()) {
                            return;
                        }
                        if (firstButtonClicked.getFrontImageId() == cb.getFrontImageId()) {
                            cb.flip();
                            cb.setMatched(true);

                            score++;
                            TextView tv = ButterKnife.findById(Game.this,R.id.scoreHolder);
                            tv.setText(score + " ");
                            firstButtonClicked.setMatched(true);
                            firstButtonClicked.setEnabled(false);
                            cb.setEnabled(false);

                            firstButtonClicked = null;
                            if (score == 10) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG);
                                toast.show();
                            }

                        } else {
                            secondButtonClicked = cb;
                            secondButtonClicked.flip();
                            busy = true;

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    secondButtonClicked.flip();
                                    firstButtonClicked.flip();
                                    secondButtonClicked = null;
                                    firstButtonClicked = null;
                                    busy = false;
                                }
                            }, 500);
                        }
                    }

                });

            }
        }
    }
    public void reset()
    {
        score = 0;
        TextView tv = ButterKnife.findById(Game.this, R.id.scoreHolder);
        tv.setText(score + "");
        firstButtonClicked = null;
        secondButtonClicked = null;
        grid.removeAllViews();
        shuffledImgIds = new ArrayList<>();
        shuffle();
        busy = false;
        setup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== getResources().getIdentifier("action_reset","id" ,getPackageName()))
            reset();
        if (item.getItemId()== getResources().getIdentifier("action_shuffle","id" ,getPackageName()))
            shuffleCards();
        return super.onOptionsItemSelected(item);
    }

    private void shuffleCards()
    {

        //grid.removeAllViews();
        firstButtonClicked = null;
        secondButtonClicked = null;
        ArrayList<CustomButton> matched = new ArrayList<>();
        ArrayList<CustomButton> notMatched = new ArrayList<>();

        int index= 0;

        for(int i = 0; i < listOfButtons.length; i++) {
            if(listOfButtons[i].isMatched() == false) {
                Log.i("isnotMatched",i +" ");
                notMatched.add(listOfButtons[i]);
            }
            else {
                matched.add(listOfButtons[i]);
            }

        }

        int r = 0, c = 0;
        for(int i = 0; i < notMatched.size(); i++)
        {

            if(c == 4){
                c=0;
                r++;
            }

            notMatched.get(i).setRow(r);
            notMatched.get(i).setColumn(c);
            //Log.i("aaa", i+"");
            notMatched.get(i).setParams(r,c);
            Log.i("aaa",notMatched.get(i).getRow()+"");


            c++;
            grid.removeAllViews();
            grid.addView(notMatched.get(i));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("onSave", ": here");
        outState.putInt(SCORE_STATE,score);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        score = savedInstanceState.getInt(SCORE_STATE);
        Log.i("Lifecycle", "Restore Instance State");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Game.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "onStop()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", "onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "onPause()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy()");
    }


}

