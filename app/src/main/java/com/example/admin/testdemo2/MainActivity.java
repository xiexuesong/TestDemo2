package com.example.admin.testdemo2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnDragScaleCallBackListener {

    private RelativeLayout relativeLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.relativeLayout);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag("main");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        DragScaleViewInner dragScaleViewInner = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner = new RelativeLayout.LayoutParams(300, 300);
        dragScaleViewInner.setLayoutParams(layoutParamsInner);
        layoutParamsInner.addRule(RelativeLayout.CENTER_IN_PARENT);
        dragScaleViewInner.addView(imageView);
        dragScaleViewInner.setTag("dragScaleViewInnerImageView");
        dragScaleViewInner.setOnDragScaleCallBackListener(this);
        dragScaleViewInner.initView();
        relativeLayout.addView(dragScaleViewInner);

        TextView textView = new TextView(this);
        textView.setText("世界如此美好");
        textView.setTag("main");

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams2);
        textView.setBackgroundColor(Color.RED);
        textView.setTextSize(25);
        DragScaleViewInner dragScaleViewInner2 = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner2 = new RelativeLayout.LayoutParams(400, 200);
        layoutParamsInner2.setMargins(550, 20, 0, 0);
        dragScaleViewInner2.setLayoutParams(layoutParamsInner2);
        dragScaleViewInner2.addView(textView);
        dragScaleViewInner2.setTag("dragScaleViewInnerTextView");
        dragScaleViewInner2.setOnDragScaleCallBackListener(this);
        dragScaleViewInner2.initView();
        relativeLayout.addView(dragScaleViewInner2);
        relativeLayout.setClipChildren(false);

        relativeLayout.setClickable(false);

    }

    @Override
    public void dragScaleCallBack(String tag) {
        int count = relativeLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = relativeLayout.getChildAt(i);
            String childTag = child.getTag() + "";
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
