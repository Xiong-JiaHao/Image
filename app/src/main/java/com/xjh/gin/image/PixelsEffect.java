package com.xjh.gin.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Gin on 2017/11/27.
 */

public class PixelsEffect extends BaseActivity {
    private ImageView imageView1,imageView2,imageView3,imageView4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pixels_effect);
        initView();
        initEvent();
    }

    private void initEvent() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.shin);
        imageView1.setImageBitmap(bitmap);
        imageView2.setImageBitmap(ImageHelper.handleImageNegative(bitmap));
        imageView3.setImageBitmap(ImageHelper.handleImagePixelsOldPhoto(bitmap));
        imageView4.setImageBitmap(ImageHelper.handleImagePixelsRelief(bitmap));
    }

    private void initView() {
        imageView1= (ImageView) findViewById(R.id.imageview1);
        imageView2= (ImageView) findViewById(R.id.imageview2);
        imageView3= (ImageView) findViewById(R.id.imageview3);
        imageView4= (ImageView) findViewById(R.id.imageview4);
    }
}
