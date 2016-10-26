package com.zengjie.mypuzzle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zengjie.mypuzzle.R;
import com.zengjie.mypuzzle.adapter.GridPicListAdapter;
import com.zengjie.mypuzzle.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    //难度
    private TextView tvLevel;
    private GridView mGridView;

    private List<Bitmap> bitmaps;
    private int[] mResPicId;

    private PopupWindow popWindow;
    private View popupView;
    //游戏类型N*N
    private int mType = 2;

    private TextView mType2,mType3,mType4;

    private String[] mCustomItems = new String[]{"本地图册","相机拍照"};

    //返回码：系统图库
    private static final int  RESULT_IMAGE = 100;
    //返回码：相机
    private static final int  RESULT_CAMERA = 200;
    //IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    //Temp照片路径
    public static String TEMP_IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath()+"/temp.png";

        initViews();
        //数据适配器
        mGridView.setAdapter(new GridPicListAdapter(this,bitmaps));
        //Item点击监听
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == mResPicId.length-1){
                    //显示本地图片或相机
                    showDialogCustom();
                } else {
                    //选择默认图片
                    Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("picSelectedId",mResPicId[position]);
                    intent.putExtra("mType",mType);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 选择显示图册、相机对话框
     */
    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择：");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //本地图册
                    Intent intent = new Intent(Intent.ACTION_PICK,null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_TYPE);
                    startActivityForResult(intent,RESULT_IMAGE);
                } else if(which == 1){
                    //系统相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(intent,RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }

    /**
     * 调用图库、相机回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_IMAGE && data != null){
                //相册
                Cursor cursor = this.getContentResolver().query(data.getData(),
                        null,null,null,null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("picPath",imagePath);
                intent.putExtra("mType",mType);
                cursor.close();
                startActivity(intent);
            } else if(requestCode == RESULT_CAMERA){
                //相机
                Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("mPicPath",TEMP_IMAGE_PATH);
                intent.putExtra("mType",mType);
                startActivity(intent);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initViews() {
        tvLevel = (TextView) findViewById(R.id.tv_Level);
        mGridView = (GridView) findViewById(R.id.gv);

        bitmaps = new ArrayList<>();
        mResPicId = new int[]{R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,
                        R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,
                        R.drawable.pic9,R.drawable.pic10,R.drawable.pic11,R.drawable.pic12,
                        R.drawable.pic13,R.drawable.pic14,R.drawable.pic15,R.drawable.plus};
        for(int i=0;i<mResPicId.length;i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mResPicId[i]);
            bitmaps.add(bitmap);
        }
        //加载PopWindow的布局
        popupView = LayoutInflater.from(this).inflate(R.layout.level_select_popwindow,null);
        mType2 = (TextView) popupView.findViewById(R.id.level_2X2);
        mType3 = (TextView) popupView.findViewById(R.id.level_3X3);
        mType4 = (TextView) popupView.findViewById(R.id.level_4X4);
        mType2.setOnClickListener(this);
        mType3.setOnClickListener(this);
        mType4.setOnClickListener(this);
        tvLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"you clicked",Toast.LENGTH_SHORT).show();
                showPopWindow(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.level_2X2:
                mType = 2;
                tvLevel.setText("2 X 2");
                break;
            case R.id.level_3X3:
                mType = 3;
                tvLevel.setText("3 X 3");
                break;
            case R.id.level_4X4:
                mType = 4;
                tvLevel.setText("4 X 4");
                break;
        }
        popWindow.dismiss();
    }

    /**
     * 显示PopWindow
     * @param view
     */
    private void showPopWindow(View view) {
        Log.d("++++++++++++++++++++","showPopWindow!!!");
        int density = (int) ScreenUtil.getDeviceDensity(this);
        popWindow = new PopupWindow(popupView,200*density,50*density);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        //透明背景
        Drawable transparent = new ColorDrawable(Color.TRANSPARENT);
        popWindow.setBackgroundDrawable(transparent);
        //获取位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0]-40*density,
                location[1]+30*density);
    }


}
