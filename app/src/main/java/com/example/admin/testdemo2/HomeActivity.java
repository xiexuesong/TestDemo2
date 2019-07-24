package com.example.admin.testdemo2;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomeActivity extends Activity {

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.relativeLayout);

        DragScaleViewInner2 dragScaleViewInner2 = new DragScaleViewInner2(this);
        RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dragScaleViewInner2.initView();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(dragScaleViewInner2,layoutParams);

        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams layoutParamsImage = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParamsImage);
        imageView.setImageResource(R.mipmap.ic_launcher);
        dragScaleViewInner2.addView(imageView);

    }
}
