package com.example.mwinkler3.fishtankapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FishTankActivity extends Activity {

    private TankView tankView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_tank);

        tankView = (TankView) findViewById(R.id.tankView);
    }

    public void onHomeClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // get the new pen width and tell the DoodleView
                int backgroundId = data.getIntExtra("background", 0);
                tankView.setBackgroundBitmap(backgroundId);
            }
        }
    }
}
