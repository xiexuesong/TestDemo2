package com.example.admin.testdemo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class DragScaleTextView extends TextView {

    private Paint paint = new Paint();

    private Context context;
    private Paint paint_storke = new Paint();
    private RectF rectFDelete ;

    public DragScaleTextView(Context context) {
        super(context);
        this.context = context;
        initBitmap();
    }

    public DragScaleTextView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initBitmap();
    }

    public DragScaleTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initBitmap();

    }

    private void initBitmap() {

        paint_storke.setStrokeWidth(1);
        paint_storke.setColor(Color.parseColor("#838587"));
        paint_storke.setStyle(Paint.Style.STROKE);
        paint_storke.setAntiAlias(true);
        setBackgroundColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint_storke);
    }
}
