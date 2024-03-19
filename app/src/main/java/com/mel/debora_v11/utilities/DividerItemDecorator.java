package com.mel.debora_v11.utilities;


import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecorator extends RecyclerView.ItemDecoration {
    private Drawable divider;
    public DividerItemDecorator(Drawable divider){
        this.divider = divider;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft() + 50;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - 50;

        int childCount = parent.getChildCount();
//        for(int i = 0; i <= childCount - 2; i++){
            View child = parent.getChildAt(childCount - 2);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();


            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(canvas);
//        }
    }
}