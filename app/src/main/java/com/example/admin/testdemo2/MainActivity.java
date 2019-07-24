package com.example.admin.testdemo2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnDragScaleCallBackListener {

    DragScaleTextView dragScaleTextView;
    private RelativeLayout relativeLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.relativeLayout);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setTag("main");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        DragScaleViewInner dragScaleViewInner = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dragScaleViewInner.setLayoutParams(layoutParamsInner);
        dragScaleViewInner.addView(imageView);
        dragScaleViewInner.setTag("dragScaleViewInnerImageView");
        dragScaleViewInner.setOnDragScaleCallBackListener(this);
        relativeLayout.addView(dragScaleViewInner);

        TextView textView = new TextView(this);
        textView.setText("世界如此美好");
        textView.setTag("main");
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(400, 400);
        textView.setLayoutParams(layoutParams2);
        textView.setBackgroundColor(Color.RED);
        textView.setTextSize(25);
        DragScaleViewInner dragScaleViewInner2 = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsInner2.setMargins(550, 0, 0, 0);
        dragScaleViewInner2.setLayoutParams(layoutParamsInner2);
        dragScaleViewInner2.addView(textView);
        dragScaleViewInner2.setTag("dragScaleViewInnerTextView");
        dragScaleViewInner2.setOnDragScaleCallBackListener(this);
        relativeLayout.addView(dragScaleViewInner2);

       /* DragScaleViewInnerOtherWay dragScaleViewInnerOtherWay = new DragScaleViewInnerOtherWay(this);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(400, 400);
        layoutParamsOther.leftMargin = 100;
        layoutParamsOther.topMargin = 300;
        dragScaleViewInnerOtherWay.setLayoutParams(layoutParamsOther);
        relativeLayout.addView(dragScaleViewInnerOtherWay);
        ImageView imageViewOther = new ImageView(this);
        imageViewOther.setTag("main");
        imageViewOther.setImageResource(R.mipmap.ic_launcher);
        imageViewOther.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams layoutParamsImageViewOther = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dragScaleViewInnerOtherWay.addView(imageViewOther, layoutParamsImageViewOther);
        addIcon(dragScaleViewInnerOtherWay);//添加左上角删除按钮角标 右下角角标*/
    }

    private void addIcon(DragScaleViewInnerOtherWay dragScaleViewInnerOtherWay) {
     /*   ImageView imageView_delete = new ImageView(this);//删除
        imageView_delete.setImageResource(R.mipmap.icon_delete);
        imageView_delete.setTag("delete");
        RelativeLayout.LayoutParams layoutParams_delete = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dragScaleViewInnerOtherWay.addView(imageView_delete, layoutParams_delete);*/

   //     ImageView imageView_scale = new ImageView(this);//缩放
   //     imageView_scale.setTag("scale");
   //     imageView_delete.setImageResource(R.mipmap.icon_scale);
   //     RelativeLayout.LayoutParams layoutParams_scale = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  dragScaleViewInnerOtherWay.addView(imageView_scale, layoutParams_scale);
    }

    @Override
    public void dragScaleCallBack(String tag) {
        int count = relativeLayout.getChildCount();
  //      Log.i("MDL","点击的tag:" + tag + " count:" + count);
        for (int i = 0; i < count; i++) {
            View child = relativeLayout.getChildAt(i);
            String childTag = child.getTag() + "";
            Log.i("MDL","childTag:" + childTag + " i:" + i);
            if (child instanceof DragScaleViewInner) {
                DragScaleViewInner dragScaleViewInner = (DragScaleViewInner) child;
                if (childTag.equals(tag)) {
                    dragScaleViewInner.setSelectStatus(true);
                } else {
                    dragScaleViewInner.setSelectStatus(false);
                }
            }
        }
    }
}
