package com.simple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zc.camera.CameraActivity;
import com.zc.camera.CameraOptions;
import com.zc.camera.CropBuilder;
import com.zc.camera.PhotoUtil;
import com.zc.cameramanager.R;
import com.zc.type.OpenType;

public class SimpleDemo extends Activity implements OnClickListener {

    private ImageView imageView;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView1);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SimpleDemo.this, CameraActivity.class);
        switch (v.getId()) {
        case R.id.button1:
            CameraOptions.getInstance(this).setmOpenType(OpenType.OPEN_CAMERA);
            break;
        case R.id.button2:
            CameraOptions.getInstance(this)
                    .setmCropBuilder(new CropBuilder(2, 3, 300, 450))
                    .setmOpenType(OpenType.OPEN_CAMERA_CROP);
            break;
        case R.id.button3:
            CameraOptions.getInstance(this).setmOpenType(OpenType.OPEN_GALLERY);
            break;
        case R.id.button4:
            CameraOptions.getInstance(this)
                    .setmCropBuilder(new CropBuilder(2, 3, 300, 450))
                    .setmOpenType(OpenType.OPEN_GALLERY_CROP);
            break;

        default:
            break;
        }
        startActivityForResult(intent, 100);
    }

    public void setImageView(String path) {
        Bitmap decodeFile = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(decodeFile);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                String stringExtra = data.getExtras().getString(
                        PhotoUtil.INTENT_PATH);
                setImageView(stringExtra);
            }

        }
    }

}
