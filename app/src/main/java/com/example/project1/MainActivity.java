package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private int clock = 0;
    private int numTimer = 0;
    private int numFlag = 4;
    private TextView numFlagTextView;
    private ArrayList<TextView> cell_tvs; // store all text views
    private boolean[][] mineIndex;
    private boolean modeFlag = false;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random rand = new Random();
        cell_tvs = new ArrayList<TextView>();
        mineIndex = new boolean[12][10];
        for(int i = 0; i < 12; i++)
            for(int j = 0; j < 10; j++)
                mineIndex[i][j] = false;
        // set random index
        for (int i = 0; i<numFlag; i++) {
            int col = rand.nextInt(10);
            int row  = rand.nextInt(12);
            if(!mineIndex[row][col])  // haven't set as mine yet
                mineIndex[row][col] = true;
        }
        grid();
        numFlagTextView = findViewById(R.id.numFlag);
        updateNumFlag(numFlag);
    }

    private void updateNumFlag(int num){
        numFlagTextView.setText(Integer.toString(num));
    }


    protected void grid(){
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);

        int[][] index = new int[numFlag][2];

        // create grid
        for (int i = 0; i<=11; i++) {
            for (int j=0; j<=9; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(25) );
                tv.setWidth( dpToPixel(25) );
                tv.setTextSize( 20 );//dpToPixel(32) );
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);
                cell_tvs.add(tv);
            }
        }
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int row = n/COLUMN_COUNT;
        int col = n%COLUMN_COUNT;
        if(numTimer==0) {
            numTimer++;
            runTimer();
        }
        if(modeFlag){ // modeflg
            if(numFlag > 0){
                tv.setText(getResources().getString(R.string.flag));
                numFlag--;
                updateNumFlag(numFlag);
            }
        }
        else {  // mode pick
            if (mineIndex[row][col]) {  // is mine
                tv.setText(R.string.mine);
                tv.setBackgroundColor(Color.LTGRAY);
            } else {  // regular
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    public void changeMode(View view){
        TextView selection = (TextView) view;
        if(!modeFlag) { // currently in pick mode
            selection.setText(getResources().getString(R.string.flag));  // set to flag mode
            modeFlag = true;
        }else{ // currently in modeFlag
            selection.setText(getResources().getString(R.string.pick));
            modeFlag = false;
        }


    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.Timer);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                String time = Integer.toString(seconds);

                timeView.setText(time);

                if (numTimer != 0) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}