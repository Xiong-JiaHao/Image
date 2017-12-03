package com.xjh.gin.image;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isPermissionAllGranted(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    public void btn_PrimaryColor(View view) {
        startActivity(new Intent(this, PrimaryColor.class));
    }

    public void btn_ColorMatrix(View view) {
        startActivity(new Intent(this, ColorMatrix.class));
    }

    public void btn_PixelsEffect(View view) {
        startActivity(new Intent(this, PixelsEffect.class));
    }
}