package com.xjh.gin.image;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

/**
 * Created by Gin on 2017/11/25.
 */

public class PrimaryColor extends BaseActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    private static final int PICK_CODE = 0X110;
    private ImageButton mImage;
    private SeekBar mSeekbarHue, mSeekbarSaturation, mSeekbarLum;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private float mHue, mSaturation, mLum;
    private String mCurreatPhotoStr;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primary_color);
        //isPermissionAllGranted(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        initView();
        initEvent();
    }

    private void initEvent() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        mImage.setOnClickListener(this);
        mSeekbarHue.setOnSeekBarChangeListener(this);
        mSeekbarSaturation.setOnSeekBarChangeListener(this);
        mSeekbarLum.setOnSeekBarChangeListener(this);
        mSeekbarHue.setMax(MAX_VALUE);
        mSeekbarSaturation.setMax(MAX_VALUE);
        mSeekbarLum.setMax(MAX_VALUE);
        mSeekbarHue.setProgress(MID_VALUE);
        mSeekbarSaturation.setProgress(MID_VALUE);
        mSeekbarLum.setProgress(MID_VALUE);
        mImage.setImageBitmap(bitmap);
    }

    private void initView() {
        mImage = (ImageButton) findViewById(R.id.id_image);
        mSeekbarHue = (SeekBar) findViewById(R.id.seekbarHue);
        mSeekbarSaturation = (SeekBar) findViewById(R.id.seekbarSaturation);
        mSeekbarLum = (SeekBar) findViewById(R.id.seekbarLum);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbarHue:
                mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180;
                break;
            case R.id.seekbarSaturation:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.seekbarLum:
                mLum = progress * 1.0f / MID_VALUE;
                break;
        }
        mImage.setImageBitmap(ImageHelper.handleImageEffect(bitmap, mHue, mSaturation, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_CODE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurreatPhotoStr = cursor.getString(idx);
                cursor.close();
                resizePhoto();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void resizePhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//true为不加载图片，只有宽高这类的数据
        BitmapFactory.decodeFile(mCurreatPhotoStr, options);

        double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);//求出压缩比例
        options.inSampleSize = (int) Math.ceil(ratio);//把压缩比例传入options中
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(mCurreatPhotoStr, options);
        mImage.setImageBitmap(bitmap);//压缩图片,mPhoto是压缩过后的Bitmap
    }
}
