package com.polygalov.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameView extends SurfaceView implements Runnable {

    private OnGameFinishListener onGameFinishListener;

    volatile boolean playing;
    Thread gameThread = null;

    static long startFrameTime;
    static long timeThisFrame;
    long fps;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private CollisionChecker collisionChecker;

    private int screenSizeX;
    private int screenSizeY;

    CopyOnWriteArrayList<DrawableObjects> drawableObjects = new CopyOnWriteArrayList<DrawableObjects>();

    private Player player;
    private Player enemy;
    private Ball ball;

    private int playerPoints;
    private int enemyPoints;

    public GameView(Context context, int x, int y) {
        super(context);

        screenSizeX = x;
        screenSizeY = y;

        surfaceHolder = getHolder();
        paint = new Paint();
        collisionChecker = new CollisionChecker();

        player = new Player(context, R.drawable.player, screenSizeX / 2, screenSizeY / 2, screenSizeX, screenSizeY);
        player.setY(screenSizeY - player.getBitmap().getHeight() - 100);
        drawableObjects.add(player);

        enemy = new Player(context, R.drawable.enemy, screenSizeX / 2, screenSizeY / 2, screenSizeX, screenSizeY);
        enemy.setY(100);
        drawableObjects.add(enemy);

        ball = new Ball(context, R.drawable.ball, screenSizeX / 2, screenSizeY / 2, screenSizeX, screenSizeY);
        drawableObjects.add(ball);
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (playing) {
            startFrameTime = System.currentTimeMillis();
            update();
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
            control();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                player.setTargetX(player.getX());
                break;
            case MotionEvent.ACTION_DOWN:
                player.setTargetX((int) motionEvent.getX() - player.getBitmap().getWidth() / 2);
                break;
            case MotionEvent.ACTION_MOVE:
                player.setTargetX((int) motionEvent.getX() - player.getBitmap().getWidth() / 2);
                break;
        }
        return true;
    }


    private void update() {
        enemy.setTargetX(ball.getX() - enemy.getBitmap().getWidth() / 2);
        if (drawableObjects != null) {
            for (DrawableObjects object : drawableObjects) {
                object.update();
            }
        }

        collisionChecker.checkCollision(ball, player);
        collisionChecker.checkCollision(ball, enemy);

        //Verify if ball is out of the screen
        if (ball.getX() < 0) {
            ball.setX(0);
            ball.inverseSpeedX();
        }
        if (ball.getX() > screenSizeX - ball.getBitmap().getWidth()) {
            ball.setX(screenSizeX - ball.getBitmap().getWidth());
            ball.inverseSpeedX();
        }

        //Player Point
        if (ball.getY() < 0) {
            ball.setY(0);
            playerPoints++;
            resetBall();
        }
        //Enemy Point
        if (ball.getY() > screenSizeY - ball.getBitmap().getHeight()) {
            ball.setY(screenSizeY - ball.getBitmap().getHeight());
            enemyPoints++;
            resetBall();
        }

        if (playerPoints >= 5 || enemyPoints >= 5) {
            playing = false;
            onGameFinishListener.onGameFinish((playerPoints >= 5 ? "WIN" : "LOSE"));
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 20, 24, 20));

            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(100);
            canvas.drawText("" + enemyPoints, screenSizeX - 20, screenSizeY / 2 - 20, paint);
            canvas.drawText("" + playerPoints, screenSizeX - 20, screenSizeY / 2 + 90, paint);
            paint.setColor(Color.argb(255, 30, 35, 30));
            canvas.drawRect(0, screenSizeY / 2 - 4, screenSizeX, screenSizeY / 2 + 4, paint);

            if (drawableObjects != null) {
                for (Iterator<DrawableObjects> iterator = drawableObjects.iterator(); iterator.hasNext(); ) {
                    DrawableObjects object = iterator.next();
                    canvas.drawBitmap(
                            object.getBitmap(),
                            object.getX(),
                            object.getY(),
                            paint);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }
    }

    private void resetBall() {
        ball.setX(screenSizeX / 2 - ball.getBitmap().getWidth() / 2);
        ball.setY(screenSizeY / 2 - ball.getBitmap().getHeight() / 2);

        ball.resetSpeed();
    }

    public interface OnGameFinishListener {
        void onGameFinish(String var1);
    }

    public void setGameFinishListener(OnGameFinishListener listener) {
        onGameFinishListener = listener;
    }
}
