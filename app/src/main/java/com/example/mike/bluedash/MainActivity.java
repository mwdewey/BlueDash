package com.example.mike.bluedash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup mainLayout = (ViewGroup)findViewById(R.id.mainLayout);
        Context mainContext = getApplicationContext();

        GuiComponent bm = new GuiComponent(mainContext,100,100,200,300,0,300,0,Color.GREEN);
        bm.setId(5000);
        GuiComponent bm2 = new GuiComponent(mainContext,400,100,200,300,0,300,0,Color.RED);
        bm2.setId(6000);
        GuiComponent bm3 = new GuiComponent(mainContext,700,100,200,300,0,300,0,Color.BLUE);
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


        CircleComponent circle = new CircleComponent(mainContext,Math.round(1080/2),Math.round(1920/2),200,0,300,0,Color.GREEN);
        circle.setId(8000);

        SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                CircleComponent bm = (CircleComponent)findViewById(8000);
                if(bm != null) {
                    bm.progress = progress;
                    bm.invalidate();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
