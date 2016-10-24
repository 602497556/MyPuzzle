package com.zengjie.mypuzzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zengjie.mypuzzle.R;
import com.zengjie.mypuzzle.util.ScreenUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class GridPicListAdapter extends BaseAdapter {

    private List<Bitmap> pics;
    private Context mContext;

    public GridPicListAdapter(Context context, List<Bitmap> pics){
        this.mContext = context;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        return pics.size();
    }

    @Override
    public Object getItem(int position) {
        return pics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv_item = null;
        int density = (int) ScreenUtil.getDeviceDensity(mContext);
        if(convertView == null){
            iv_item = new ImageView(mContext);
            iv_item.setLayoutParams(new GridView.LayoutParams(80*density,100*density));
            //设置显示比例类型
            iv_item.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            iv_item = (ImageView) convertView;
        }
        iv_item.setImageBitmap(pics.get(position));
        return iv_item;
    }

}
