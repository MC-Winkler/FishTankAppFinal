package com.example.mwinkler3.fishtankapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.content.Intent;

public class MainActivity extends Activity {

    int otherId;
    int id;
    public static int background;
    public static String fishColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBackgroundRadioButtonClicked(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.backgroundGroup);
        id = radioGroup.getCheckedRadioButtonId();
        switch(id) {
            case R.id.skrunken_bikini_bottom:
                background = R.drawable.bikini_bottom;
                break;

            case R.id.skrunken_bikini_bottom_2:
                background = R.drawable.bikini_bottom_2;
                break;

            case R.id.skrunken_little_mermaid:
                background = R.drawable.little_mermaid;
                break;

            case R.id.skrunken_little_mermaid_2:
                background = R.drawable.little_mermaid_2;
                break;
        }
    }

    public void onFishRadioButtonClicked (View view) {
        RadioGroup secondRadioGroup = (RadioGroup)findViewById(R.id.fishGroup);
        otherId = secondRadioGroup.getCheckedRadioButtonId();
        switch(otherId) {
            case R.id.one_fish:
                fishColor = "Yellow";
                break;

            case R.id.two_fish:
                fishColor = "Green";
                break;

            case R.id.orange_fish:
                fishColor="Orange";
                break;

            case R.id.blue_fish:
                fishColor="Blue";
                break;
        }
    }

    public void onStartClick (View view) {
        Intent intent = new Intent(this, FishTankActivity.class);
        startActivity(intent);
    }

}
