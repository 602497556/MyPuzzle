package com.zengjie.mypuzzle.util;

import com.zengjie.mypuzzle.activity.PuzzleMain;
import com.zengjie.mypuzzle.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class GameUtil {

    //游戏信息单元格Bean
    public static List<ItemBean> mItemBeans = new ArrayList<>();
    //空单元格
    public static ItemBean mBlankItemBean = new ItemBean();


    /**
     * 生成随机的Item
     */
    public static void getPuzzleGenerator(){
        int index = 0;
        //随机打乱顺序
        for(int i=0; i<mItemBeans.size(); i++){
            index = (int) (Math.random()*
                    PuzzleMain.TYPE * PuzzleMain.TYPE);
            swapItems(mItemBeans.get(index), GameUtil.mBlankItemBean);
        }
        List<Integer> data = new ArrayList<>();
        for(int i=0; i<mItemBeans.size(); i++){
            data.add(mItemBeans.get(i).getBitmapId());
        }
        //判断生成是否有解
        if(canResolve(data)){
            return;
        } else {
            getPuzzleGenerator();
        }
    }

    /**
     * 交换空格与点击Item的位置
     *
     * @param from 交换图
     * @param blank 空白图
     */
    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItemBean = new ItemBean();
        //交换BitmapId
        tempItemBean.setBitmapId(from.getBitmapId());
        from.setBitmapId(blank.getBitmapId());
        blank.setBitmapId(tempItemBean.getBitmapId());
        //交换Bitmap
        tempItemBean.setBitmap(from.getBitmap());
        from.setBitmap(blank.getBitmap());
        blank.setBitmap(tempItemBean.getBitmap());
        //设置新的blank
        GameUtil.mBlankItemBean = from;
    }

    /**
     * 判断该数据是否有解
     *
     * @param data 拼图数组数据
     * @return 是否有解
     */
    public static boolean canResolve(List<Integer> data) {
        //获取空格id
        int blankId = GameUtil.mBlankItemBean.getItemId();
        //可行性原则
        if(data.size() % 2 == 1){
            return getInversions(data)%2 == 0;
        } else {
            //从底往上数，空格位于奇数行
            if(((blankId-1)/PuzzleMain.TYPE)%2 == 1){
                return getInversions(data)%2 == 0;
            } else {
                //从底往上数，空格位于偶数行
                return getInversions(data)%2 == 1;
            }
        }
    }

    /**
     * 计算倒置和算法
     *
     * @param data 拼图数据数组
     * @return 该序列的倒置和
     */
    public static int getInversions(List<Integer> data) {
        int inversions = 0;
        int inversionCount = 0;
        for(int i=0; i<data.size(); i++){
            for(int j=i+1; j<data.size(); j++){
                int index = data.get(i);
                if(data.get(j) != 0 && data.get(j)<index){
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

    /**
     * 判断点击的Item是否可以移动
     *
     * @param position position
     * @return 能否移动
     */
    public static boolean isMovable(int position){
        int type = PuzzleMain.TYPE;
        // 获取空格Item
        int blankId = GameUtil.mBlankItemBean.getItemId()-1;
        // 不同行，相差为type
        if(Math.abs(blankId - position) == type){
            return true;
        }
        //相同行，相差为1
        if((blankId / type == position / type) &&
                Math.abs(blankId - position) == 1){
            return true;
        }
        return false;
    }

    /**
     * 是否拼图成功
     *
     * @return 是否拼图成功
     */
    public static boolean isSuccess(){
        for(ItemBean tempBean : GameUtil.mItemBeans){
            if(tempBean.getBitmapId() != 0 &&
                    tempBean.getItemId() == tempBean.getBitmapId()){
                continue;
            } else if(tempBean.getBitmapId() == 0 &&
                    tempBean.getItemId() == PuzzleMain.TYPE * PuzzleMain.TYPE){
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

}
