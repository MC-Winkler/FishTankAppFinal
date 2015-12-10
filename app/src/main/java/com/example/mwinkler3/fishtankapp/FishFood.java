package com.example.mwinkler3.fishtankapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Michael on 12/8/2015.
 */
public class FishFood {

    protected float x, y;
    private float width, height;
    private Bitmap bitmap;
    private Fish fish;

    private int screenWidth, screenHeight;

    private final float SCALE = 1f;

    public FishFood(Context context, float touchX, float touchY, Fish fish) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fish_food);

        // scale the size
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start wherever the user touched
        x = touchX;
        y = touchY;

        this.fish = fish;
    }

    public void doDraw(Canvas canvas) {
        // draw the food
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public float[] doUpdate() {

        float[] foodCoordinates = new float[2];
        //food drifts downward
        if((y+2) < screenHeight)
            y += 2;

        if(fish.isFacingRight &&((fish.x+(fish.frameWidth*.4)) < x + width/2 && (fish.x + fish.frameWidth*.4) > x - width/2)
            && (fish.y < y + height/2) && (fish.y > y - height/2)) {
            foodCoordinates[0] = -1;
            foodCoordinates[1] = -1;
        }
        else if (!fish.isFacingRight &&((fish.x) < x + width/2 && (fish.x) > x - width/2)
                && (fish.y < y + height/2) && (fish.y > y - height/2)) {
            foodCoordinates[0] = -1;
            foodCoordinates[1] = -1;
        }
        else if (((fish.x) < x + width/2 && (fish.x) > x - width/2)
                && (fish.y < y + height/2) && (fish.y > y - height/2)) {
            foodCoordinates[0] = -1;
            foodCoordinates[1] = -1;
        }
        else {
            foodCoordinates[0] = x;
            foodCoordinates[1] = y+ (float) (height*.75);
        }
        return foodCoordinates;

    }
}
