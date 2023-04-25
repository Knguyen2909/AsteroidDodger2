package com.example.asteroiddodger;
import android.util.Log;


public class asteroidPair {
    // initialised asteroidPair's x, y coordinates, and height and width
    private int dist;
    private int topY, bottomY;
    private int x;
    private int height, width;
    private double dx;

    // calculates where the asteroid obstacles with be generated and changes the height and width
    public asteroidPair(int screenHeight, int screenWidth){
        dist = (int) (screenHeight/1.1);
        bottomY = (int) Math.floor(Math.random() * (screenHeight/2) + .3 * screenHeight);
        topY = bottomY - dist;

        x = screenWidth;
        dx = .014 * screenWidth;
        height = screenHeight;
        width = screenWidth;
    }

    // moves the asteroid
    public void moveX(){
        x -= dx;
    }

    public int getX(){
        return x;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    // resets the asteroids position
    public void reset(){
        x = width;
        bottomY = (int) Math.floor(Math.random() * (height/2) + .3 * height);
        topY = bottomY - dist;
    }
}
