package com.example.puzzlegame;

import android.graphics.Bitmap;

/**
 * Created by liuxian on 2016/8/21.
 */

public class ItemBeam {
    private int itemId;
    private int bitmapId;
    private Bitmap bitmap;

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public int getItemId(){
        return itemId;
    }

    public void setBitmapId(int bitmapId){
        this.bitmapId = bitmapId;
    }

    public int getBitmapId(){
        return bitmapId;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public ItemBeam(int mItemId , int mBitmapId , Bitmap mBitmap){
        this.itemId = mItemId;
        this.bitmapId = mBitmapId;
        this.bitmap = mBitmap;
    }
    public ItemBeam(){}
}
