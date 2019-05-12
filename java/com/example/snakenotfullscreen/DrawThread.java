package com.example.snakenotfullscreen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

public class DrawThread extends Thread {

    private boolean running = false;
    private SurfaceHolder surfaceHolder;

    private Integer size = null;
    private Integer height = 10;
    private Integer width = 10;

    private double time = 0;
    private int x = 5;
    private int y = 5;
    private ArrayList<Integer> xs;
    private ArrayList<Integer> ys;
    private int appleX;
    private int appleY;

    private int dir = 1;

    private Random random;

    private Canvas canvas;
    private Paint paint;
    private Rect rect;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        paint = new Paint();
        rect = new Rect();
        random = new Random();
        xs = new ArrayList<>();
        ys = new ArrayList<>();
        xs.add(5); xs.add(5); xs.add(5);
        ys.add(6); ys.add(7); ys.add(8);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setDir(int dir) {
        if (Math.abs(this.dir - dir) != 2) {
            this.dir = dir;
        }
    }
    private void drawBoard() {
        canvas.drawARGB(80, 102, 204, 255);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        if (size == null) {
            size = canvas.getWidth() / 10;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                canvas.drawRect(j * size, i * size, (j + 1) * size, (i + 1) * size, paint);
            }
        }
    }

    private void checkForCollision() {
        if (x == appleX && y == appleY) {
            xs.add(-1);
            ys.add(-1);
            boolean f = false;
            while (!f) {
                appleX = random.nextInt(width);
                appleY = random.nextInt(height);
                f = true;
                for (int i = 0; i < xs.size(); i++) {
                    if (appleX == xs.get(i) && appleY == ys.get(i)) {
                        f = false;
                        break;
                    }
                }
            }
        }
    }

    private void checkForDeath() {
        for (int i = 0; i < xs.size(); i++) {
            if (x == xs.get(i) && y == ys.get(i)) {
                running = false;
            }
        }
    }

    private void drawSnake() {
        time += 0.17;
        if (time > 1.6) {
            for (int i = xs.size() - 1; i >= 1; i--) {
                xs.set(i, xs.get(i - 1));
                ys.set(i, ys.get(i - 1));
            }
            xs.set(0, x);
            ys.set(0, y);
            switch (dir) {
                case 1:
                    y--;
                    if (y < 0) { y = height - 1; }
                    break;
                case 2:
                    x++;
                    if (x >= width) { x = 0; }
                    break;
                case 3:
                    y++;
                    if (y >= height) { y = 0; }
                    break;
                case 4:
                    x--;
                    if (x < 0) { x = width - 1; }
                    break;
            }
            time = 0;
        }
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < xs.size(); i++) {
            canvas.drawRect(xs.get(i) * size + 10, ys.get(i) * size + 10, (xs.get(i) + 1) * size - 10, (ys.get(i) + 1) * size - 10, paint);
        }
        canvas.drawRect(x * size + 10, y * size + 10, (x + 1) * size - 10, (y + 1) * size - 10, paint);
        checkForCollision();
        checkForDeath();
    }

    private void drawApple() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(appleX * size + 10, appleY * size + 10, (appleX + 1) * size - 10, (appleY + 1) * size - 10, paint);
    }

    private void draw() {
        drawBoard();
        drawSnake();
        drawApple();
    }

    @Override
    public void run() {
        //super.run();
        while (running) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas == null) {
                    continue;
                }
                draw();
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
