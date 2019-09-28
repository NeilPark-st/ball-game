package com.example.assignment8;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public float screenWidth;
    public float screenHeight;
    public int lifeCount = 10;

    public class Ball{

        float x;
        float y;
        int radius;
        Paint paint;
        }

        public class TargetBall extends Ball{

        float targetBallx ;
        float targetBally ;
        int targetBallRadius ;
        Paint targetBallPaint;
        }

    public class PlayerBall extends Ball{

        float playerBallx ;
        float playerBally ;
        int playerBallRadius ;
        Paint playerBallPaint;
    }

    public class LifeBall extends Ball{

        float lifeBallx ;
        float lifeBally ;
        int lifeBallRadius ;
        Paint lifeBallPaint;
    }

    TargetBall target = new TargetBall();
    float tx = 540;
    float ty = 100;
    int tr = 100;
    Paint tp = new Paint();

    PlayerBall player = new PlayerBall();
    float px = 540;
    float py = 1844;
    int pr = 50;
    Paint pp = new Paint();

    LifeBall life = new LifeBall();
    float lx = 20;
    float ly = 1894;
    int lr = 20;
    Paint lp = new Paint();

    //Draw the ball and set to initial position
    public class GraphicsView extends View {

        float xt, xp, xl;
        float yt, yp, yl;
        int radiust, radiusp, radiusl;
        Paint paintt, paintp, paintl;

        public GraphicsView(Context context) {
            super(context);
            xt = tx;
            yt = ty;
            radiust = tr;
            paintt = tp;
            paintt.setColor(getColor(R.color.colorTarget));
            xp = px;
            yp = py;
            radiusp = pr;
            paintp = pp;
            paintp.setColor(getColor(R.color.colorPlayer));
            xl = lx;
            yl = ly;
            radiusl = lr;
            paintl = lp;
            paintl.setColor(getColor(R.color.colorLife));

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawCircle(xt, yt, radiust, paintt);
            canvas.drawCircle(xp, yp, radiusp, paintp);
            
            for (int i = 1; i < lifeCount; i++) {
                canvas.drawCircle(xl, yl, radiusl, paintl);

                 // xl += 40;
            }
            invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        //Toast.makeText(this, String.valueOf(screenHeight), Toast.LENGTH_SHORT).show();
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraintlayoutRoot);

        GraphicsView graphicsView = new GraphicsView(this);

        constraintLayout.addView(graphicsView);

        //Set up full screen display
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

}
