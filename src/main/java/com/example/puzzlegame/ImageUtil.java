package com.example.puzzlegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxian on 2016/8/21.
 * 图像工具：实现图像的分割，与自适应
 */

public class ImageUtil {

    public ItemBeam itemBeam;
    /**
     * @param type 游戏的种类（困难程度）
     * @param picSelected 选择的图片
     * @param context context
     */

    public void createInitBitmap(int type , Bitmap picSelected , Context context){
        Bitmap bitmap = null;
        List<Bitmap>bitmapItems = new ArrayList<Bitmap>();
        //每个item的宽高
        int itemWidth = picSelected.getWidth()/type;
        int itemHeight = picSelected.getHeight()/type;
        for(int i = 1 ; i <= type ; i++){
            for(int j = 1 ; j <= type ; j++){
                bitmap = Bitmap.createBitmap(picSelected , (j-1)*itemWidth , (i-1)*itemHeight , itemWidth , itemHeight);
                bitmapItems.add(bitmap);
                itemBeam = new ItemBeam((i-1)*type+j , (i-1)*type+j , bitmap);
                GameUtil.itemBeams.add(itemBeam);
            }
        }
        PuzzleMain.lastBitmap = bitmapItems.get(type * type - 1);
        bitmapItems.remove(type * type -1);
        GameUtil.itemBeams.remove(type * type - 1);
        Bitmap blankBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_blank);
        blankBitmap = Bitmap.createBitmap(blankBitmap , 0 , 0 , itemWidth , itemHeight);
        bitmapItems.add(blankBitmap);
        GameUtil.itemBeams.add(new ItemBeam(type * type, 0 , blankBitmap));
        GameUtil.blankItemBean = GameUtil.itemBeams.get(type * type - 1);
    }

    public Bitmap resizeBitmap(float newWidth , float newHeight , Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth() , newHeight / bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap , 0 , 0  , bitmap.getWidth() , bitmap.getHeight() , matrix , true);
        return newBitmap;
    }

}
