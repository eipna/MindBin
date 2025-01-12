package com.eipna.mindbin.ui.adapters;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class NoteItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public NoteItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space / 2;
        outRect.top = space / 2;

        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanIndex = params.getSpanIndex();

            if (spanIndex == 0) {
                outRect.left = space;
                outRect.right = space / 2;
            } else {
                outRect.right = space;
                outRect.left = space / 2;
            }
        }
    }
}