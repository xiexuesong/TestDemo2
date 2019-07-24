package com.example.admin.testdemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DragScaleViewInner2 extends RelativeLayout {

    private ImageView imageView_delete ;
    private ImageView imageView_scale;
    private Context context;


    public DragScaleViewInner2(Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public DragScaleViewInner2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    public DragScaleViewInner2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initData();
    }

    private void initData() {
        setBackgroundColor(Color.RED);
    }

    public void initView(){
        Bitmap bitmap_delete = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_delete);
        Bitmap bitmap_scale = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_scale);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(bitmap_delete.getWidth(), bitmap_delete.getHeight());
        imageView_delete = new ImageView(context);
        imageView_delete.setImageBitmap(bitmap_delete);
        imageView_scale = new ImageView(context);
        imageView_scale.setImageBitmap(bitmap_scale);

        addView(imageView_delete,layoutParams);
        addView(imageView_scale,layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0 ; i < count ; i++){

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
