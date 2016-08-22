package com.example.puzzlegame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxian on 2016/8/21.
 * 拼图工具类：实现拼图的交换与生成算法
 *
 */

public class GameUtil {
    public static List<ItemBeam> itemBeams = new ArrayList<ItemBeam>();

    public static ItemBeam blankItemBean = new ItemBeam();

    public static boolean isMoveable(int position){
        int type = PuzzleMain.type;
        int blankId = GameUtil.blankItemBean.getItemId() - 1;
        if(Math.abs(blankId - position) == type){
            return true;
        }
        if((blankId / type == position / type) && Math.abs(blankId - position) == 1){
            return true;
        }
        return false;
    }
    /**
     * 交换空格与点击Item的位置
     *
     */
    public static void swapItem(ItemBeam from , ItemBeam blank){
        ItemBeam tempItemBeam = new ItemBeam();
        //交换BitmapId
        tempItemBeam.setBitmapId(from.getBitmapId());
        from.setBitmapId(blank.getBitmapId());
        blank.setBitmapId(tempItemBeam.getBitmapId());
        //交换Bitmap
        tempItemBeam.setBitmap(from.getBitmap());
        from.setBitmap(blank.getBitmap());
        blank.setBitmap(tempItemBeam.getBitmap());
        GameUtil.blankItemBean = from;
    }
    /**
     * 生成随机的Item
     */
    public static void getPuzzleGenerator(){
        int index = 0;
        for (int i = 0 ; i < itemBeams.size() ; i++){
            index = (int)(Math.random() * PuzzleMain.type * PuzzleMain.type);
            swapItem(itemBeams.get(index) , GameUtil.blankItemBean);
        }
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0 ; i < itemBeams.size() ; i++){
            data.add(itemBeams.get(i).getBitmapId());
        }
        if(canSolve(data)){
            return;
        }else {
            getPuzzleGenerator();
        }
    }
    public static boolean canSolve(List<Integer> data){
        int blankId = GameUtil.blankItemBean.getItemId();
        if (data.size() % 2 == 1){
            return getInversions(data) % 2 == 0;
        }else{
            if(((int)(blankId - 1) / 2) % 2 == 1){
                return getInversions(data) % 2 == 0;
            }else{
                return getInversions(data) % 2 == 1;
            }
        }
    }

    public static int getInversions(List<Integer> data){
        int inversions = 0;
        int inversionCount = 0;
        for(int i = 0 ; i < data.size() ; i++){
            for(int j = i + 1 ; j < data.size() ; j++){
                int index = data.get(i);
                if(data.get(j) != 0 && data.get(j) < index ){
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

    public static boolean isSuccess(){
        for(ItemBeam tempBeam : GameUtil.itemBeams){
            if(tempBeam.getBitmapId() != 0 && (tempBeam.getBitmapId()) == tempBeam.getBitmapId()){
                continue;
            }else if(tempBeam.getBitmapId() == 0 && tempBeam.getItemId() == 2 * 2){
                continue;
            }else{
                return  false;
            }
        }
        return true;
    }
}
