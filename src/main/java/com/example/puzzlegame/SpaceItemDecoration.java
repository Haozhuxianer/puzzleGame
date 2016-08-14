package com.example.puzzlegame;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.example.puzzlegame.R.styleable.View;

/**
 * Created by liuxian on 2016/8/13.
 * 瀑布流item间距
 *
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space){
        this.space = space;
    }

    public void getItemOffsets(Rect outRect , android.view.View view , RecyclerView parent , RecyclerView.State state){
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if(parent.getChildPosition(view) == 0){
            outRect.top = space;
        }
    }
}
