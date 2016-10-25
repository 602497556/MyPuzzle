package com.zengjie.mypuzzle.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.zengjie.mypuzzle.R;

public class PuzzleMain extends Activity {

    //拼图完成时显示的最后一个图片
    public static Bitmap mLastBitmap;
    //设置为N*N显示
    public static int TYPE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_main);
    }













}
