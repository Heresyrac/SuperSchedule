package com.example.SuperSchedule.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;


public class LinearLayoutSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final int DEFAULT_HEIGHT = 10;
    private int mSpaceSize;
    private int mOrientation;

    private LinearLayoutSpaceItemDecoration(Builder builder) {
        mSpaceSize = builder.spaceSize;
        mOrientation = builder.orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        int size = SizeUtils.dp2px(mSpaceSize);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (position == 0) {//第一个
                outRect.set(0, size * 2, 0, size);
            } else if (position == itemCount - 1) {//最后一个不需要
                outRect.set(0, size, 0, size * 2);
            } else {
                outRect.set(0, size, 0, size);
            }
        } else {
            if (position == 0) {//第一个
                outRect.set(size * 2, 0, size, 0);
            } else if (position == itemCount - 1) {//最后一个不需要
                outRect.set(size, 0, size * 2, 0);
            } else {
                outRect.set(size, 0, size, 0);
            }
        }
    }

    public static class Builder {

        private int spaceSize = DEFAULT_HEIGHT;
        private int orientation = LinearLayoutManager.VERTICAL;

        public Builder setSpaceSize(int spaceSize) {
            this.spaceSize = spaceSize;
            return this;
        }

        public Builder setOrientation(@RecyclerView.Orientation int orientation) {
            this.orientation = orientation;
            return this;
        }

        public LinearLayoutSpaceItemDecoration build() {
            return new LinearLayoutSpaceItemDecoration(this);
        }
    }
}