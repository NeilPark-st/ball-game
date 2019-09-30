package com.example.assignment8;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class GamePlay extends AppCompatActivity {

    int width, height;
    TextView textViewStage, textViewScore, textViewGameOver, textViewResultScore;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play);

        textViewStage = (TextView)findViewById(R.id.textviewStage);
        textViewScore = (TextView)findViewById(R.id.textviewScore);
        textViewGameOver = (TextView)findViewById(R.id.textviewGameOver);
        textViewResultScore = (TextView)findViewById(R.id.textviewResultScore);
        button = (Button)findViewById(R.id.buttonGoToHome);

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
        int lifeCount, score, currentStage;
        PlayerBall playerBall;
        TargetBall targetBall;
        LifeBall[] lifeBalls;
        int playerBallRadius, targetBallRadius, lifeBallRadius, obstacleRadius;
        boolean start;

        //throwing player ball related
        View.OnTouchListener onTouchGraphicsView;
        float startX, startY, stopX, stopY, length, theta;
        Paint linePaint;
        boolean touchAllowed;
        boolean isEnd, isVic;

        //obstacles
        ArrayList<Obstacle>[] stage;
        ArrayList<Obstacle> currentStageObstacle;

        public GraphicsView(Context context) {
            super(context);

            isEnd = false;
            isVic = false;



            lifeCount = 10;
            score = 0;
            currentStage = 1;
            start = true;
            playerBallRadius = 40;
            targetBallRadius = 50;
            lifeBallRadius = 20;
            obstacleRadius = 40;
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
                            playerBall.setVel(30, theta);
                            break;
                    }
                    return true;
                }
            };

            this.setOnTouchListener(onTouchGraphicsView);

            stage = new ArrayList[5];
            for(int i = 0; i < 5; i++){
                stage[i] = new ArrayList<>();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(!isEnd){
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

                    //Making Obstacles for each stage
                    //stage2
                    stage[1].add(new Obstacle(width/4, height/2, obstacleRadius, 0, 0, 0, height ));
                    stage[1].add(new Obstacle(width/2, height/4, obstacleRadius, 0, 0, 0, height ));
                    stage[1].add(new Obstacle(width*3/4, height/2, obstacleRadius, 0, 0, 0, height ));
                    //stage3
                    stage[2].add(new Obstacle(width * 3 / 8, height / 2, obstacleRadius * 2, 0, 0, 0, height));
                    stage[2].add(new Obstacle(width / 2, height * 3 / 8, obstacleRadius * 2, 0, 0, 0, height));
                    stage[2].add(new Obstacle(width * 5 / 8, height / 2, obstacleRadius * 2, 0, 0, 0, height));
                    //stage4
                    for(int i=1; i<=1; i++){
                        stage[3].add(new Obstacle(width * i / 2, height / 4, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(180 * i), 0, height));
                    }
                    for(int i=1; i<=2; i++){
                        stage[3].add(new Obstacle(width * i / 3, height / 2, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(180 * i), 0, height));
                    }
                    for(int i=1; i<=1; i++){
                        stage[3].add(new Obstacle(width * i / 2, height * 3 / 4, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(180 * i), 0, height));
                    }
                    //stage5
                    for(int i=1; i<=1; i++){
                        stage[4].add(new Obstacle(width * i / 2, height / 4, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(Math.random() * 360), targetBallRadius * 6, height - playerBallRadius * 4));
                    }
                    for(int i=1; i<=3; i++){
                        stage[4].add(new Obstacle(width * i / 4, height / 2, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(Math.random() * 360), targetBallRadius * 6, height - playerBallRadius * 4));
                    }
                    for(int i=1; i<=1; i++){
                        stage[4].add(new Obstacle(width * i / 2, height * 3 / 4, obstacleRadius, (float)(Math.random() * 5 + 5),
                                (float)Math.toRadians(Math.random() * 360), targetBallRadius * 6, height - playerBallRadius * 4));
                    }
                    //stage[4].add(new Obstacle(width/2, (float)(targetBallRadius * (2 + 1.5)), (int)(targetBallRadius * 1.5), (float)7.5, 0, 0, height));

                    button.setVisibility(INVISIBLE);
                    start = false;
                }

                textViewStage.setText("Stage: " + currentStage);
                textViewScore.setText("Score: " + score);

                if(lifeCount <= 0){
                    isEnd = true;
                    if(score >= 10)
                        isVic = true;
                }

                switch (score){
                    case 0:
                        currentStage = 1;
                        break;
                    case 1:
                    case 2:
                        currentStage = 2;
                        break;
                    case 3:
                    case 4:
                        currentStage = 3;
                        break;
                    case 5:
                    case 6:
                    case 7:
                        currentStage = 4;
                        break;
                    case 8:
                    case 9:
                        currentStage = 5;
                        break;
                }

                canvas.drawCircle(playerBall.getX(), playerBall.getY(), playerBall.getRadius(), playerBall.getPaint());
                canvas.drawCircle(targetBall.getX(), targetBall.getY(), targetBall.getRadius(), targetBall.getPaint());
                for(int i = 0; i < lifeCount - 1; i++){
                    canvas.drawCircle(lifeBalls[i].getX(), lifeBalls[i].getY(), lifeBalls[i].getRadius(), lifeBalls[i].getPaint());
                }
                playerBall.changePosition();
                targetBall.changePosition();

                canvas.drawLine(startX, startY, stopX, stopY, linePaint);

                //when the ball is out from the view
                if(playerBall.getY() + playerBall.getRadius() < 0){
                    lifeCount--;
                    playerBall.setPositionToInitial();
                    playerBall.setVel(0,0);
                    touchAllowed = true;
                }

                //drawing obstacles for the right stage
                currentStageObstacle = stage[currentStage - 1];
                for(int i = 0; i < currentStageObstacle.size(); i++){
                    canvas.drawCircle(currentStageObstacle.get(i).getX(), currentStageObstacle.get(i).getY(),
                            currentStageObstacle.get(i).getRadius(), currentStageObstacle.get(i).getPaint());
                    currentStageObstacle.get(i).changePosition();
                }

                //if the player ball collides with the target
                if(playerBall.isCollideWith(targetBall)){
                    playerBall.setPositionToInitial();
                    playerBall.setVel(0,0);
                    lifeCount--;
                    score++;
                    touchAllowed = true;
                }

                //if the player ball collides with obstacles
                for(int i=0; i<currentStageObstacle.size(); i++){
                    if(playerBall.isCollideWith(currentStageObstacle.get(i))){
                        playerBall.setPositionToInitial();
                        playerBall.setVel(0,0);
                        lifeCount--;
                        currentStageObstacle.remove(i);
                        touchAllowed = true;
                    }
                }

                invalidate();
            }
            else{ // Game Over

                if(isVic){ // Victory
                    textViewGameOver.setTextColor(getColor(R.color.colorClear));
                    textViewGameOver.setText("Clear!!");
                }
                else{
                    textViewGameOver.setTextColor(getColor(R.color.colorGameOver));
                    textViewGameOver.setText("Game Over");
                }

                textViewResultScore.setText("Score: " + score);
                button.setVisibility(VISIBLE);
                textViewStage.setText("");
                textViewScore.setText("");
            }



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

        public boolean isCollideWith(Ball b){
            if(Math.pow(x - b.getX(), 2) + Math.pow(y - b.getY(), 2) < Math.pow(radius + b.getRadius(), 2))
                return true;
            else
                return false;
        }
    }

    public class LifeBall extends Ball{

        public LifeBall(float x, float y, int r){
            super(x, y, r);
            paint = new Paint();
            paint.setColor(getColor(R.color.colorLife));
        }
    }

    public class Obstacle extends Ball{
        float velocity, theta, v_x, v_y;
        float min_y, max_y;

        public Obstacle(float x, float y, int r, float vel, float th, float minY, float maxY){
            super(x, y, r);
            paint = new Paint();
            paint.setColor(getColor(R.color.colorObstacle));

            velocity = vel;
            theta = th;
            v_x = (float) (vel * Math.cos(theta));
            v_y = (float) (vel * Math.sin(theta));

            min_y = minY;
            max_y = maxY;
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

            if(y + radius + v_y > max_y){
                y = max_y - radius;
                v_y = -v_y;
            }
            else if(y - radius + v_y < min_y){
                y = min_y + radius;
                v_y = -v_y;
            }
            else {
                y += v_y;
            }
        }
    }
}