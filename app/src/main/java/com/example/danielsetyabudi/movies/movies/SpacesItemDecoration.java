package com.example.danielsetyabudi.movies.movies;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Daniel Setyabudi on 30/06/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpacesItemDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
//
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
//            outRect.top = space;
//        } else {
//            outRect.top = 0;
//        }
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(space, space, space, space);
    }
}
