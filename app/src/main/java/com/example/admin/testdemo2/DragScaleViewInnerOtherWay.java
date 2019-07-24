package com.example.admin.testdemo2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DragScaleViewInnerOtherWay extends RelativeLayout {

    private Context context;
    private Paint paint_rect;

    public DragScaleViewInnerOtherWay(Context context) {
        super(context);
        initData(context);
    }

    public DragScaleViewInnerOtherWay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public DragScaleViewInnerOtherWay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        this.context = context;
        paint_rect = new Paint();
        paint_rect.setStrokeWidth(5);
        paint_rect.setStyle(Paint.Style.STROKE);
        paint_rect.setAlpha(255);
        paint_rect.setAntiAlias(true);
        paint_rect.setPathEffect(new DashPathEffect(new float[]{7, 3}, 1));
        paint_rect.setColor(Color.parseColor("#838587"));

        ImageView imageView_delete = new ImageView(context);//删除
        imageView_delete.setImageResource(R.mipmap.icon_delete);
        imageView_delete.setTag("delete");
        RelativeLayout.LayoutParams layoutParams_delete = new RelativeLayout.LayoutParams(60, 60);
        addView(imageView_delete, layoutParams_delete);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //经过这个方法(或者 measureChild) ， child 的宽高都是有值的
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        int maxWidth = 0 ;
        int maxHeight = 0;
        for(int i = 0 ; i < count ;i ++){
            View view = getChildAt(i);
            Log.i("MDL", "tag:" + view.getTag() + " height:" + view.getMeasuredHeight() + " width:" + view.getMeasuredWidth());
            maxHeight = view.getMeasuredHeight() > maxHeight ? view.getMeasuredHeight() : maxHeight;
            maxWidth = view.getMeasuredWidth() > maxWidth ? view.getMeasuredWidth() : maxWidth;
        }
        Log.i("MDL","maxWidth:" + maxWidth + " height:" + maxHeight);
        setMeasuredDimension(maxWidth + 30, maxHeight + 30);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            String tag = view.getTag() + "";
            switch (tag) {
                case "delete":
                    view.layout(-view.getWidth() / 2, -view.getHeight() / 2, view.getWidth() - view.getWidth() / 2, view.getHeight() - view.getHeight() / 2);
                    break;
                case "scale":
                    view.layout(getWidth() - view.getWidth() / 2, getHeight() - view.getHeight() / 2, getWidth() + view.getWidth() / 2, getHeight() + view.getHeight() / 2);
                    break;
            }
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint_rect);
    }
}
