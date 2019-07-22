package com.example.admin.testdemo2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    public final static int MIN_INTERVAL = 500; //最小间隔时间

    private Bitmap bitmap_drag;
    private Bitmap bitmap_delete;
    private Context context;
    private OnDragScaleCallBackListener onDragScaleCallBackListener;
    private boolean selectStatus = false;
    private Paint paint_rect, paint_icon;
    private RectF rectF_delete, rectF_scale, rectF_drag;//删除、缩放图标 以及拖动的区域范围
    private long firstClick;//记录第一次单击事件
    private int option;
    private float oldDistance, newDistance , textViewOldDistance; //缩放比例
    private float lastX, lastY;
    private float offset, icon_bitmap_width;
    private int aspectRatio; //子view的宽高比
    private float originalTextSize = 10; //textView原始的尺寸,在zhangu的时候需要重新设置

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
        paint_rect = new Paint();
        paint_rect.setStrokeWidth(1);
        paint_rect.setStyle(Paint.Style.STROKE);
        paint_rect.setAntiAlias(true);
        paint_rect.setPathEffect(new DashPathEffect(new float[]{7, 3}, 1));
        paint_rect.setColor(Color.parseColor("#838587"));
        paint_icon = new Paint();
        paint_icon.setAntiAlias(true);
        Resources resources = getResources();
        bitmap_delete = BitmapFactory.decodeResource(resources, R.mipmap.icon_delete);
        bitmap_drag = BitmapFactory.decodeResource(resources, R.mipmap.icon_scale);
        offset = bitmap_delete.getWidth() / 2;
        icon_bitmap_width = bitmap_delete.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //必须要测量子布局的宽高
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //经过这个方法(或者 measureChild) ， child 的宽高都是有值的
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        int maxHeight = 0;
        int maxWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            maxHeight = view.getMeasuredHeight() > maxHeight ? view.getMeasuredHeight() : maxHeight;
            maxWidth = view.getMeasuredWidth() > maxWidth ? view.getMeasuredWidth() : maxWidth;
        }
        aspectRatio = maxWidth / maxHeight;//计算宽高比
        setMeasuredDimension(maxWidth + bitmap_delete.getWidth(), maxHeight + bitmap_delete.getHeight());
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
                    view.layout((int) offset, (int) offset, (int) (view.getWidth() + offset), (int) (view.getHeight() + offset));
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
            canvas.drawRect(offset - 1, offset - 1, getWidth() - offset + 1, getHeight() - offset + 1, paint_rect);//选中线框
            canvas.drawBitmap(bitmap_delete, 0, 0, paint_icon);
            canvas.drawBitmap(bitmap_drag, getWidth() - icon_bitmap_width, getHeight() - icon_bitmap_width, paint_icon);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rectF_delete = new RectF(0, 0, icon_bitmap_width, icon_bitmap_width);
        rectF_scale = new RectF(getWidth() - icon_bitmap_width, getHeight() - icon_bitmap_width, getWidth(), getHeight());
        rectF_drag = new RectF(offset, offset, getWidth() - offset, getHeight() - offset);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                if (selectStatus) {
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    oldDistance = caculateDistance(lastX, lastY);
                    textViewOldDistance = oldDistance;
                    handleDown(event);
                    if (option == OPTION_DRAG) {
                        long interval = System.currentTimeMillis() - firstClick;
                        if (interval < MIN_INTERVAL) {//如果两次点击间隔小于最小间隔时间，那就判定双击

                        }
                        firstClick = System.currentTimeMillis();//重新记录点击时间
                    }
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                if (onDragScaleCallBackListener != null) {
                    onDragScaleCallBackListener.dragScaleCallBack(getTag() + "");
                }
                if(option == OPTION_SCALE){
                    //抬起的时候 ，如果当前子view是TextView的话，记录一下测试的textView的大小
                    //防止下一次缩放textView，字体大小混乱
                    View child = getChildAt(0);
                    if(child instanceof TextView){
                        originalTextSize = ((TextView) child).getTextSize();//getTextSize单位是px
                        originalTextSize = DensityUtil.px2sp(context,originalTextSize);//setTextSize单位是sp
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE://移动
                if (selectStatus) {
                    if (option == OPTION_DRAG) {
                        //拖动
                        moveView(event.getRawX(), event.getRawY());
                    } else if (option == OPTION_SCALE) {
                        newDistance = caculateDistance(event.getRawX(), event.getRawY());
                        zoomImage();
                        lastX = event.getRawX();//重新刷新上次移动点X的位置
                        lastY = event.getRawY();//重新刷新上次移动点Y的位置
                        oldDistance = caculateDistance(lastX, lastY);
                    }
                    lastX = event.getRawX();//重新刷新上次移动点X的位置
                    lastY = event.getRawY();//重新刷新上次移动点Y的位置
                    oldDistance = caculateDistance(lastX, lastY);
                }
                break;
            default:
                break;
        }
        return true;//消费事件
    }

    /**
     * 移动 ，移动的是此view的下位置
     *
     * @param rawX
     * @param rawY
     */
    private void moveView(float rawX, float rawY) {
        //重新设置位置
        layout(getLeft() + (int) (rawX - lastX), getTop() + (int) (rawY - lastY), getRight() + (int) (rawX - lastX), getBottom() + (int) (rawY - lastY));
        //设置此view在父布局的位置
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.leftMargin = getLeft();
        layoutParams.topMargin = getTop();
        setLayoutParams(layoutParams);
    }

    /**
     * 缩放
     * 我这个viewgroup设置的是wrap_content包裹内容，所以缩放的时候只需要缩放imageView的宽高即可
     */
    private void zoomImage() {
        float scaleRatio = newDistance / oldDistance;
        float newWidthParent = getWidth() * scaleRatio;
        float newHeightParent = newWidthParent / aspectRatio;
        if (newWidthParent > bitmap_drag.getWidth() * 2 || newHeightParent > bitmap_drag.getHeight() * 2) {
            //重新设置此view的布局位置
            LayoutParams layoutParamsParent = (LayoutParams) getLayoutParams();
            layoutParamsParent.width = (int) newWidthParent;
            layoutParamsParent.height = (int) newHeightParent;
            layoutParamsParent.leftMargin = getLeft();
            layoutParamsParent.topMargin = getTop();
            setLayoutParams(layoutParamsParent);

            //重新设置子view的宽高
            View view = getChildAt(0);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float textSizeRatio = newDistance / textViewOldDistance;
                textView.setTextSize(originalTextSize * textSizeRatio);
            }
            float newWidth = view.getWidth() * scaleRatio;
            float newHeight = newWidth / aspectRatio;
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.width = (int) newWidth;
            layoutParams.height = (int) newHeight;
            view.setLayoutParams(layoutParams);
        }
    }

    private float caculateDistance(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
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
