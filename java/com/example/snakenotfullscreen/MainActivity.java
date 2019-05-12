package com.example.snakenotfullscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private double xDown;
    private double yDown;
    private double xUp;
    private double yUp;

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new DrawView(this);
        drawView.setOnTouchListener(this);
        setContentView(drawView);
    }

    private void defineSwipe() {
        double dx = xDown - xUp;
        double dy = yDown - yUp;
        if (Math.abs(dx) - Math.abs(dy) > 0) {
            if (dx > 0) {
                drawView.getDrawThread().setDir(4);
            }
            else if (dx < 0) {
                drawView.getDrawThread().setDir(2);
            }
        }
        else if (Math.abs(dx) - Math.abs(dy) < 0) {
            if (dy > 0) {
                drawView.getDrawThread().setDir(1);
            }
            else if (dy < 0) {
                drawView.getDrawThread().setDir(3);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getX();
                yUp = event.getY();
                defineSwipe();
                break;
        }
        return true;
    }
}
