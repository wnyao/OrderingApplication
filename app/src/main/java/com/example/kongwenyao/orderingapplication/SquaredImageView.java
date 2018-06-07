package com.example.kongwenyao.orderingapplication;

import android.content.Context;

public class SquaredImageView extends android.support.v7.widget.AppCompatImageView {

    public SquaredImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
