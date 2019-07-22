package com.example.admin.testdemo2;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.relativeLayout);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setTag("main");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 110);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        DragScaleViewInner dragScaleViewInner = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dragScaleViewInner.setLayoutParams(layoutParamsInner);
        dragScaleViewInner.addView(imageView);
        dragScaleViewInner.setTag("dragScaleViewInner");
        dragScaleViewInner.setOnDragScaleCallBackListener(this);
        relativeLayout.addView(dragScaleViewInner);

        TextView textView = new TextView(this);
        textView.setText("世界如此美好");
        textView.setTag("main");
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(400, 400);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams2);
        textView.setBackgroundColor(Color.RED);
        DragScaleViewInner dragScaleViewInner2 = new DragScaleViewInner(this);
        RelativeLayout.LayoutParams layoutParamsInner2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsInner2.setMargins(20, 0, 0, 0);
        dragScaleViewInner2.setLayoutParams(layoutParamsInner2);
        dragScaleViewInner2.addView(textView);
        dragScaleViewInner2.setTag("dragScaleViewInner2");
        dragScaleViewInner2.setOnDragScaleCallBackListener(this);
        relativeLayout.addView(dragScaleViewInner2);

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
