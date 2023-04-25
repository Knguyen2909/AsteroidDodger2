package com.example.asteroiddodger;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;


import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class gameView extends SurfaceView {

    // initialises the variables
    private Bitmap bitmap;
    private Paint bitmapPaint;
    private Ship ship;
    private Bitmap sprite;
    private SurfaceHolder holder;
    private int dy = 0;
    private int millis = 0;
    private asteroidPair asteroids;
    private Bitmap bottomAsteroid;
    private Bitmap topAsteroid;
    private int score = -1;
    private Paint textPaint;
    private RectF spriteRect;
    private RectF topAsteroidRect = new RectF();
    private RectF bottomAsteroidRect = new RectF();
    private boolean spriteScaled = false;
    private boolean paused;
    private boolean responded = false;


    // draws the ship, asteroid and the background for the game
    public gameView(final Context context, AttributeSet attributes) {
        super(context, attributes);
        setFocusable(true);
        setFocusableInTouchMode(true);
        ship = new Ship();
        sprite = BitmapFactory.decodeResource(getResources(), R.drawable.ship);

        bottomAsteroid = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
        Matrix transform = new Matrix();
        transform.preScale(1.0f, -1.0f);
        topAsteroid = Bitmap.createBitmap(bottomAsteroid, 0, 0, bottomAsteroid.getWidth(), bottomAsteroid.getHeight(), transform, true);

        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(200);


        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            // draws the canvas
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

        });


        Thread updateThread = new Thread() {
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!paused) {
                            try {
                                // scales the asteroid and shipsprite
                                if (!spriteScaled) {
                                    sprite = Bitmap.createScaledBitmap(sprite, (int) (Math.floor(getWidth() * .15)), (int) (Math.floor(getHeight() * .08)), false); //getWidth and getHeight aren't initialized until later
                                    bottomAsteroid = Bitmap.createScaledBitmap(bottomAsteroid, (int) (Math.floor(getWidth() * .3)), (int) (Math.floor(getHeight() * .67)), false);
                                    topAsteroid = Bitmap.createScaledBitmap(topAsteroid, (int) (Math.floor(getWidth() * .3)), (int) (Math.floor(getHeight() * .67)), false);
                                    spriteScaled = true;
                                }
                                if (millis % 120 == 0 && millis >= 80) {
                                    if (asteroids == null)
                                        asteroids = new asteroidPair(getHeight(), getWidth());
                                        // resets the asteroid positions
                                    else
                                        asteroids.reset();
                                    // score increases when ship goes past the asteroid pair
                                    if (ship.isAlive()) {
                                        score++;
                                    }
                                }

                                update.run();
                            } catch (Exception e) {
                            }
                            millis++;
                        }
                    }
                }, 0, 16);
            }
        };

        updateThread.start();
    }


    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ship.isAlive())
                    dy = -25;
                break;
        }
        postInvalidate();
        return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
    }


    //draws and updates bird and pipes
    Runnable update = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void run() {
            try {
                if (millis >= 130 && millis % 2 == 0)
                    dy += 3;
                ship.update(dy);
                asteroids.moveX();


                Canvas canvas = holder.lockCanvas();
                canvas.drawPaint(textPaint);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bottomAsteroid, asteroids.getX(), asteroids.getBottomY(), bitmapPaint);
                canvas.drawBitmap(topAsteroid, asteroids.getX(), asteroids.getTopY(), bitmapPaint);
                canvas.drawBitmap(sprite, ship.getX(), ship.getY(), bitmapPaint);
                if (!responded)
                    canvas.drawText("Score: " + score, 5, 200, textPaint);
                else {
                    canvas.drawText("Score: " + score, 5, 200, textPaint);
                    TextPaint tp = new TextPaint();
                    tp.setTextSize(50);
                }
                holder.unlockCanvasAndPost(canvas);


                spriteRect = new RectF(ship.getX(), ship.getY(), ship.getX() + sprite.getWidth(), ship.getY() + sprite.getHeight());
                topAsteroidRect = new RectF(asteroids.getX(), asteroids.getTopY(), asteroids.getX() + topAsteroid.getWidth(), asteroids.getTopY() + topAsteroid.getHeight());
                bottomAsteroidRect = new RectF(asteroids.getX(), asteroids.getBottomY(), asteroids.getX() + bottomAsteroid.getWidth(), getHeight());


                if (spriteRect.intersect(topAsteroidRect) || spriteRect.intersect(bottomAsteroidRect) || ship.getY() > getHeight()) {
                    ship.setStatus(false);
                    if (!responded)
                        Log.i("Score", "" + score);
                }


            } catch (Exception e) {
            }
        }
    };

    public void togglePause() {
        paused = !paused;
    }

}
