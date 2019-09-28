package com.example.assignment8;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GamePlay extends AppCompatActivity {

    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play);

        //Toast.makeText(this, String.valueOf(screenHeight), Toast.LENGTH_SHORT).show();


        //Set up full screen display
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintlayoutGamePlay);

        GraphicsView graphicsView = new GraphicsView(this);

        constraintLayout.addView(graphicsView);
    }

    //Draw the ball and set to initial position
    public class GraphicsView extends View {
        int lifeCount, score, stage;
        PlayerBall playerBall;
        TargetBall targetBall;
        LifeBall[] lifeBalls;
        int playerBallRadius, targetBallRadius, lifeBallRadius;
        boolean start;

        //throwing player ball related
        View.OnTouchListener onTouchGraphicsView;
        float startX, startY, stopX, stopY, length, theta;
        Paint linePaint;
        boolean touchAllowed;


        public GraphicsView(Context context) {
            super(context);

            lifeCount = 10;
            score = 0;
            stage = 1;
            start = true;
            playerBallRadius = 40;
            targetBallRadius = 40;
            lifeBallRadius = 20;
            lifeBalls = new LifeBall[10];

            linePaint = new Paint();
            linePaint.setColor(getColor(R.color.colorLine));
            touchAllowed = true;

            onTouchGraphicsView = new View.OnTouchListener(){

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(touchAllowed == false)
                        return false;
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            if(motionEvent.getY() + playerBallRadius < startY){
                                theta = (float) Math.atan((motionEvent.getY() - startY) / (motionEvent.getX() - startX));
                                if(theta < 0){
                                    stopX = (float) (startX + length * Math.cos(theta));
                                    stopY = (float) (startY + length * Math.sin(theta));
                                }
                                else{
                                    stopX = (float) (startX - length * Math.cos(theta));
                                    stopY = (float) (startY + length * Math.sin(-theta));
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            touchAllowed = false;
                            stopX = startX;
                            stopY = startY;
                            playerBall.setVel(20, theta);
                            break;
                    }
                    return true;
                }
            };

            this.setOnTouchListener(onTouchGraphicsView);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(start == true){
                width = canvas.getWidth();
                height = canvas.getHeight();
                playerBall = new PlayerBall(width/2, height - playerBallRadius, playerBallRadius);
                targetBall = new TargetBall(width/2, targetBallRadius, targetBallRadius);
                for(int i=0; i < lifeCount; i++){
                    lifeBalls[i] = new LifeBall(lifeBallRadius * (2* (i % 5) + 1), height - lifeBallRadius * (i / 5 * 2 + 1), lifeBallRadius);
                }
                startX = playerBall.getX();
                startY = playerBall.getY();
                stopX = startX;
                stopY = startY;
                length = 150;

                start = false;
            }

            canvas.drawCircle(playerBall.getX(), playerBall.getY(), playerBall.getRadius(), playerBall.getPaint());
            canvas.drawCircle(targetBall.getX(), targetBall.getY(), targetBall.getRadius(), targetBall.getPaint());
            for(int i = 0; i < lifeCount - 1; i++){
                canvas.drawCircle(lifeBalls[i].getX(), lifeBalls[i].getY(), lifeBalls[i].getRadius(), lifeBalls[i].getPaint());
            }
            playerBall.changePosition();
            targetBall.changePosition();

            canvas.drawLine(startX, startY, stopX, stopY, linePaint);

            if(playerBall.getY() + playerBall.getRadius() < 0){
                lifeCount--;
                playerBall.setPositionToInitial();
                playerBall.setVel(0,0);
                touchAllowed = true;
            }

            invalidate();
        }


    }

    public class Ball{
        protected float x;
        protected float y;
        protected int radius;
        protected Paint paint;

        public Ball(float x, float y, int r){
            this.x = x;
            this.y = y;
            radius = r;
        }

        public float getX(){
            return x;
        }

        public float getY(){
            return y;
        }

        public int getRadius(){
            return radius;
        }

        public Paint getPaint(){
            return paint;
        }
    }

    public class TargetBall extends Ball{
        private float v_x;

        public TargetBall(float x, float y, int r){
            super(x, y, r);
            paint = new Paint();
            paint.setColor(getColor(R.color.colorTarget));
            v_x = 5;
        }

        public void changePosition(){
            if(x + radius + v_x > width){
                x = width - radius;
                v_x = -v_x;
            }
            else if(x - radius + v_x < 0){
                x = radius;
                v_x = -v_x;
            }
            else {
                x += v_x;
            }
        }
    }

    public class PlayerBall extends Ball{
        private float v_x;
        private float v_y;

        public PlayerBall(float x, float y, int r){
            super(x, y, r);
            paint = new Paint();
            paint.setColor(getColor(R.color.colorPlayer));
            v_x = 0;
            v_y = 0;
        }

        public void changePosition(){
            if(x + radius + v_x > width){
                x = width - radius;
                v_x = -v_x;
            }
            else if(x - radius + v_x < 0){
                x = radius;
                v_x = -v_x;
            }
            else {
                x += v_x;
            }

            y += v_y;
        }

        public void setPositionToInitial(){
            x = width / 2;
            y = height - radius;
        }

        public void setVel(float vel, float theta){
            if(theta < 0){
                v_x = (float) (vel * Math.cos(theta));
                v_y = (float) (vel * Math.sin(theta));
            }
            else{
                v_x = (float) (-vel * Math.cos(theta));
                v_y = (float) (vel * Math.sin(-theta));
            }
        }
    }

    public class LifeBall extends Ball{
        public LifeBall(float x, float y, int r){
            super(x, y, r);
            paint = new Paint();
            paint.setColor(getColor(R.color.colorLife));
        }
    }
}