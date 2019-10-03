package com.example.assignment8;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HiScore extends AppCompatActivity {

    int[] listArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hi_score);

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


        SharedPreferences prefs = getSharedPreferences(GamePlay.PREF_NAME, Context.MODE_PRIVATE);
        int dataNum = prefs.getInt("dataNum", 0);
        int[] scores = new int[5];
        for(int i = 1; i <= dataNum; i++){
            scores[i-1] = prefs.getInt(""+i, 0);
        }
        TextView[] textViews = new TextView[5];
        textViews[0] = findViewById(R.id.first);
        textViews[1] = findViewById(R.id.second);
        textViews[2] = findViewById(R.id.third);
        textViews[3] = findViewById(R.id.forth);
        textViews[4] = findViewById(R.id.fifth);

        for(int i = 1; i <= dataNum; i++){
            textViews[i-1].setText(i + ". " + scores[i-1]);
        }

        /*
        ArrayAdapter<String> adapter;
        listArr = getResources().getIntArray((R.array.scoreArray));
        //Toast.makeText(this, String.valueOf(listArr[1]), Toast.LENGTH_SHORT).show();
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int s:listArr) {
            arrayList.add(String.valueOf(s));
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ListView lv = (ListView)findViewById(R.id.scoreView);
        lv.setAdapter(adapter);

         */

    }

    // Onclick method to view Hi Score

    public void onclickButtonHome(View v) {

        finish();

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
}