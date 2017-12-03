package com.xjh.gin.image;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by Gin on 2017/11/26.
 */

public class ColorMatrix extends BaseActivity implements View.OnClickListener {
    private static final int PICK_CODE = 0X110;
    private ImageView mImageView;
    private GridLayout mGroup;
    private String mCurreatPhotoStr;
    private Bitmap bitmap;//就是单独的bitmap，之后的修改不会覆盖这个Bitmap
    private Button btn_Change, btn_Reset, btn_ChangeImage;
    private int mEtWidth, mEtHeight;
    private EditText[] mEts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_matrix);
        isPermissionAllGranted(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        initView();
        initEvent();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.imageview);
        mGroup = (GridLayout) findViewById(R.id.group);
        btn_Change = (Button) findViewById(R.id.btnChange);
        btn_Reset = (Button) findViewById(R.id.btnReset);
        btn_ChangeImage = (Button) findViewById(R.id.btnChangeImage);
    }

    private void initEvent() {
        btn_Change.setOnClickListener(this);
        btn_Reset.setOnClickListener(this);
        btn_ChangeImage.setOnClickListener(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        mImageView.setImageBitmap(bitmap);
        mGroup.post(new Runnable() {//在onCreate方法中因为控件还没有绘制完毕，所以我们无法获得控件的高和宽，所以当控件绘制完毕以后，我们就在线程中获取，因为这时候控件一定绘制完毕了
            @Override
            public void run() {
                mEtWidth = mGroup.getWidth() / 5;
                mEtHeight = mGroup.getHeight() / 4;
                addEts();
                initMatrix();
            }
        });
    }

    private void addEts() {//添加EditText
        for (int i = 0; i < 20; i++) {
            EditText editText = new EditText(ColorMatrix.this);
            mEts[i] = editText;
            mGroup.addView(editText, mEtWidth, mEtHeight);//让editText动态的加载进mGroup中去
        }
    }

    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }

    private void getMatrix(){
        for(int i=0;i<20;i++){
            mColorMatrix[i]=Float.valueOf(mEts[i].getText().toString());
        }
    }

    private void setImageMatrix(){
        Bitmap bmp=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);//一定要新建一个Bitmap，因为传进来的Bitmap是不能修改的，需要重新进行绘制
        android.graphics.ColorMatrix colorMatrix = new android.graphics.ColorMatrix();
        colorMatrix.set(mColorMatrix);//将颜色矩阵放入ColorMatrix中去
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,0,0,paint);
        mImageView.setImageBitmap(bmp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChange:
                getMatrix();
                setImageMatrix();
                break;

            case R.id.btnReset:
                initMatrix();
                getMatrix();
                setImageMatrix();
                break;

            case R.id.btnChangeImage:
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
        mImageView.setImageBitmap(bitmap);//压缩图片,mPhoto是压缩过后的Bitmap
    }
}
