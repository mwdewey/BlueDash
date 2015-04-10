package com.example.mike.bluedash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String address = getIntent().getStringExtra("address");
        if(address == null) return;
        else connect(address);

        ViewGroup mainLayout = (ViewGroup)findViewById(R.id.mainLayout);
        Context mainContext = getApplicationContext();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Handler mHandler = new Handler(Looper.getMainLooper());

        int bmWidth = (int)(width * 0.185185);
        int bmHeight = (int)(height * 0.168918);
        int bmY = (int)(height * (100.0/1776.0));

        int bm1X = (int)(width * (100.0/1080.0));
        GuiComponent bm = new GuiComponent(mainContext,bm1X,bmY,bmWidth,bmHeight,0,300,0,Color.GREEN);
        bm.setId(5000);
        int bm2X = (int)(width * (400.0/1080.0));
        GuiComponent bm2 = new GuiComponent(mainContext,bm2X,bmY,bmWidth,bmHeight,0,300,0,Color.RED);
        bm2.setId(6000);
        int bm3X = (int)(width * (700.0/1080.0));
        GuiComponent bm3 = new GuiComponent(mainContext,bm3X,bmY,bmWidth,bmHeight,0,300,0,Color.BLUE);
        bm3.setId(7000);

        mainLayout.addView(bm);
        mainLayout.addView(bm2);
        mainLayout.addView(bm3);

        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                GuiComponent bm = (GuiComponent)findViewById(5000);
                if(bm != null) {
                    bm.progress = progress;
                    bm.invalidate();
                }

                GuiComponent bm2 = (GuiComponent)findViewById(6000);
                if(bm2 != null) {
                    bm2.progress = progress * 2;
                    bm2.invalidate();
                }

                GuiComponent bm3 = (GuiComponent)findViewById(7000);
                if(bm3 != null) {
                    bm3.progress = progress * 3;
                    bm3.invalidate();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        int circX = (int)((double)width*0.925);
        int circY = height;
        int radius = (int)(width * (200.0/1080.0));
        CircleComponent circle = new CircleComponent(mainContext,Math.round(circX/2),Math.round(circY/2),radius,0,300,0,Color.GREEN);
        circle.setId(8000);
        mainLayout.addView(circle);


        SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CircleComponent dial = (CircleComponent) findViewById(8000);
                if (dial != null) {
                    dial.updateLine(progress);
                    dial.invalidate();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect :
                Intent intent = new Intent(this, ConnectActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void connect(String address){
        AsyncUpdate au = new AsyncUpdate(this,address);
        au.execute("");
    }


}
