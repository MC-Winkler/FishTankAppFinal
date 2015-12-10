package com.example.mwinkler3.fishtankapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


/**
 * Contains both the surface to draw to and the game loop.
 *
 * @author J. Hollingsworth, modified by Michael Winkler & Rachel McGovern on 12/08/15
 */

public class TankView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private int screenWidth,screenHeight;

    private Bitmap backgroundBitmap;

    private FishFood fishFood;

    private int i;

    private float[] whereToSwim;
    // the fish sprite
    protected Fish fish;

    public TankView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // remember the context for finding resources
        this.context = context;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // game loop thread
        thread = new GameLoopThread();

        setBackgroundBitmap(0);

        whereToSwim = new float[2];
        whereToSwim[0] = -1;
        whereToSwim[1] = -1;


    }

    public void setBackgroundBitmap(int backgroundId){
        if(backgroundId ==0){
            backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bikini_bottom);
        }
        else {
            backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), backgroundId);
        }
    }

    // SurfaceHolder.Callback methods:
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // thread exists, but is in terminated state
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread();
        }

        // start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    // touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //ensures that only one fish food exists at a time
        if (fishFood == null) {
            fishFood = new FishFood(context, event.getX(), event.getY(), fish);
            System.out.println("created new fishfood with coordinates " + event.getX() + " " + event.getY());
        }
        return true;
    }

    // Game Loop Thread
    private class GameLoopThread extends Thread {

        private boolean isRunning = false;
        private long lastTime;

        private float[] foodCoordinates = new float[2];



        // frames per second calculation
        private int frames;
        private long nextUpdate;

        public GameLoopThread() {
            fish = new Fish(context);
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {

            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);

                    //updateFPS(now);
                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // an approximate frames per second calculation
        private void updateFPS(long now) {
            float fps = 0.0f;
            ++frames;
            float overtime = now - nextUpdate;
            if (overtime > 0) {
                fps = frames / (1 + overtime/1000.0f);
                frames = 0;
                nextUpdate = System.currentTimeMillis() + 1000;
                System.out.println("FPS: " + (int) fps);
            }
        }

        /* THE GAME */

        // move all objects in the game
        private void doUpdate(double elapsed) {
            if (fishFood != null) {
                foodCoordinates = fishFood.doUpdate();
                if (foodCoordinates[0] == -1){
                    fishFood = null;
                }
                else {
                    fish.doUpdate(elapsed, foodCoordinates[0], foodCoordinates[1],"Chasing Food");
                }
            } else {
                if (whereToSwim[0] == -1){
                    whereToSwim[0] = (float) Math.random()*(screenWidth- fish.frameWidth);
                    whereToSwim[1] = (float) Math.random()*(screenHeight - fish.frameHeight);
                }
                if (fish.doUpdate(elapsed,whereToSwim[0],whereToSwim[1],"Chilling") == -1){
                    whereToSwim[0] = -1;
                }
                fish.doUpdate(elapsed, whereToSwim[0], whereToSwim[1], "Chilling");
            }

        }

        // draw all objects in the game
        private void doDraw(Canvas canvas) {



            canvas.drawBitmap(backgroundBitmap,null,new Rect(0,0,screenWidth,screenHeight),null);
            fish.doDraw(canvas);
            if(fishFood != null) {
                fishFood.doDraw(canvas);
            }
        }
    }
}