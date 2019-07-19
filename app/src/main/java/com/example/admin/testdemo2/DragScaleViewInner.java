package com.example.admin.testdemo2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 本来准备直接添加ImageView ，但是选中的边框线绘制的时候会显示在最上层，盖住了小图标，然后准备在dispatchDraw()方法里面重新的去动态添加ImageView
 * 然而没有走通(具体现象我就不描述了，比较费劲，但是 下次需要注意的是不要在自定义view的draw()或者dispatchDraw()方法里面动态添加view
 * 因为你无法显式的控制该方法的调用)
 * 然后想到了第二种方法，绘制，在dispatchDraw()方法里面绘制bitmap，然后通过onTouchEvent()方法去做监听事件
 */
public class DragScaleViewInner extends RelativeLayout {
    public final static int OPTION_DRAG = 1;//拖动
    public final static int OPTION_DELETE = 2;//删除
    public final static int OPTION_SCALE = 3;//缩放

    private Bitmap bitmap_drag;
    private Bitmap bitmap_delete;
    private Context context;
    private OnDragScaleCallBackListener onDragScaleCallBackListener;
    private boolean selectStatus = false;
    private Paint paint_rect, paint_icon;
    private RectF rectF_delete, rectF_scale, rectF_drag;//删除、缩放图标 以及拖动的区域范围
    private long firstClick;//记录第一次单击事件
    private ScaleGestureDetector mScaleGestureDetector = null;
    private int option;
    private MotionEvent event_down;//按下位置

    public void setSelectStatus(boolean selectStatus) {
        //如果当前view的默认的选中状态和选中的状态是不一样的，此时我们修改选中状态
        //反之，则不修改选中状态，防止多余的刷新操作
        if (this.selectStatus != selectStatus) {
            this.selectStatus = selectStatus;
            invalidate();
        }
    }

    public void setOnDragScaleCallBackListener(OnDragScaleCallBackListener onDragScaleCallBackListener) {
        this.onDragScaleCallBackListener = onDragScaleCallBackListener;
    }

    public DragScaleViewInner(Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public DragScaleViewInner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    public DragScaleViewInner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initData();
    }

    private void initData() {
        setClipChildren(true);
        paint_rect = new Paint();
        paint_rect.setStrokeWidth(1);
        paint_rect.setStyle(Paint.Style.STROKE);
        paint_rect.setAntiAlias(true);
        paint_rect.setPathEffect(new DashPathEffect(new float[]{7, 3}, 1));
        paint_rect.setColor(Color.parseColor("#838587"));
        paint_icon = new Paint();
        paint_icon.setAntiAlias(true);
        Resources resources = getResources();
        bitmap_delete = BitmapFactory.decodeResource(resources, R.mipmap.sticker_delete);
        bitmap_drag = BitmapFactory.decodeResource(resources, R.mipmap.sticker_drag);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //必须要测量子布局的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            String tag = view.getTag() + "";
            switch (tag) {
                case "main":
                    view.layout(20, 20, view.getWidth() + 20, view.getHeight() + 20);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (selectStatus) {
            canvas.drawRect(19, 19, getWidth() - 19, getHeight() - 19, paint_rect);//选中线框
            canvas.drawBitmap(bitmap_delete, 0, 0, paint_icon);
            canvas.drawBitmap(bitmap_drag, getWidth() - 40, getHeight() - 40, paint_icon);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rectF_delete = new RectF(0, 0, 40, 40);
        rectF_scale = new RectF(getWidth() - 40, getHeight() - 40, getHeight(), getHeight());
        rectF_drag = new RectF(20, 20, getWidth() - 20, getHeight() - 20);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                if (selectStatus) {
                    event_down = event;
                    handleDown(event);
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                if (onDragScaleCallBackListener != null) {
                    onDragScaleCallBackListener.dragScaleCallBack(getTag() + "");
                }
                break;
            case MotionEvent.ACTION_MOVE://拖动
                if (selectStatus) {
                    if (option == OPTION_DRAG) {
                        //拖动
                    } else if (option == OPTION_SCALE) {
                       scale(event);
                    }
                }
                break;
            default:
                break;
        }
        return true;//消费事件
    }

    private void scale(MotionEvent event) {
        Log.i("MDL","scale");
        float xDistance = event.getX() - event_down.getX();
        float yDistance = event.getY() -  event_down.getY();
        //float bevel = (float) Math.sqrt(Math.pow(xDistance , 2) + Math.pow(yDistance , 2));
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.width = getWidth() * 2;
        layoutParams.height = getHeight() * 2;
        setLayoutParams(layoutParams);
        View view = getChildAt(0);
        LayoutParams layoutParams1 = (LayoutParams) view.getLayoutParams();
        layoutParams1.width = layoutParams1.width * 2;
        layoutParams1.height = layoutParams1.height * 22;
        view.setLayoutParams(layoutParams1);
        postInvalidate();
    }

    /**
     * 处理拖动的事件
     */
    private void handleDrag() {
    }

    /**
     * 处理按下的事件
     */
    private void handleDown(MotionEvent event) {
        if (rectF_delete.contains(event.getX(), event.getY())) {
            //删除事件
            option = OPTION_DELETE;
            return;
        }
        if (rectF_scale.contains(event.getX(), event.getY())) {
            //缩放事件
            option = OPTION_SCALE;
            return;
        }
        option = OPTION_DRAG;
    }
}
