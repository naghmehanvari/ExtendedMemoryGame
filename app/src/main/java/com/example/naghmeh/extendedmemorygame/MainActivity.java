package com.example.naghmeh.extendedmemorygame;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void displayRules(View view)
    {
        FragmentManager fm = getSupportFragmentManager();
        RulesFragment mf = new RulesFragment();
        mf.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        mf.show(fm, "The Rules");
    }

    public void play(View view)
    {
        Intent gameIntent = new Intent(this, Game.class);
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(gameIntent);
    }
}
