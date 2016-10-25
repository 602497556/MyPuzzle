package com.zengjie.mypuzzle.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zengjie.mypuzzle.R;
import com.zengjie.mypuzzle.util.ImagesUtil;
import com.zengjie.mypuzzle.util.ScreenUtil;

public class PuzzleMain extends Activity {

    //拼图完成时显示的最后一个图片
    public static Bitmap mLastBitmap;
    //设置为N*N显示
    public static int TYPE = 2;
    //选择的图片
    private Bitmap mPicSelected;
    private int mResId;
    private String mPicPath;
    private GridView mGvPuzzleMainDetail;
    //Button
    private Button mBtnBack;
    private Button mBtnRestart;
    private Button mBtnImg;
    //Flag 是否已显示原图
    private boolean mIsShowImg;
    //显示步数
    private TextView mTvPuzzleMainCounts;
    //计时器
    private TextView mTvTimer;
    //步数显示
    public static int COUNT_INDEX = 0;
    //计时显示
    public static int TIMER_INDEX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_main);
        //获取选择的图片
        Bitmap picSelectedTemp;
        //选择默认图片还是自定义图片
        mResId = getIntent().getExtras().getInt("picSelectedId");
        mPicPath = getIntent().getExtras().getString("mPicPath");
        if(mResId != 0){
            picSelectedTemp = BitmapFactory.decodeResource(
                    getResources(),mResId);
        } else {
            picSelectedTemp = BitmapFactory.decodeFile(mPicPath);
        }
        TYPE = getIntent().getExtras().getInt("mType",2);
        // 对图片处理
        handlerImage(picSelectedTemp);
        //初始化Views
        initViews();

    }

    /**
     * 初始化Views
     */
    private void initViews() {
        //Button
        mBtnBack = (Button) findViewById(R.id.btn_puzzle_main_back);
        mBtnImg = (Button) findViewById(R.id.btn_puzzle_main_img);
        mBtnRestart = (Button) findViewById(R.id.btn_puzzle_main_restart);
        //Flag 是否已显示原图
        mIsShowImg = false;
        //GridView
        mGvPuzzleMainDetail = (GridView) findViewById(R.id.gv_puzzle_main);
        mGvPuzzleMainDetail.setNumColumns(TYPE);
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(
                mPicSelected.getWidth(),mPicSelected.getHeight());
        //水平居中
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //其他格式属性
        gridParams.addRule(RelativeLayout.BELOW,R.id.ll_puzzle_main_spinner);
        //GridView显示
        mGvPuzzleMainDetail.setLayoutParams(gridParams);
        mGvPuzzleMainDetail.setHorizontalSpacing(0);
        mGvPuzzleMainDetail.setVerticalSpacing(0);
        //TV步数
        mTvPuzzleMainCounts = (TextView) findViewById(R.id.tv_puzzle_main_counts);
        mTvPuzzleMainCounts.setText("" + COUNT_INDEX);
        //TV计时器
        mTvTimer = (TextView) findViewById(R.id.tv_puzzle_main_time);
        mTvTimer.setText("0秒");
        //添加显示原图的View
        addImgView();
    }

    /**
     * 添加显示原图的View
     */
    private void addImgView() {
    }

    /**
     * 对图片处理 自适应大小
     *
     * @param bitmap bitmap
     */
    private void handlerImage(Bitmap bitmap) {
        //将图片放大到固定尺寸
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeight = ScreenUtil.getScreenSize(this).heightPixels;
        mPicSelected = new ImagesUtil().resizeBitmap(
                screenWidth * 0.8f, screenHeight * 0.6f, bitmap);
    }


}
