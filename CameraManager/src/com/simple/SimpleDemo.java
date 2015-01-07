package com.simple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zc.camera.CameraHandler;
import com.zc.camera.CameraOptions;
import com.zc.camera.CropBuilder;
import com.zc.camera.ImageSelcetListernAsy;
import com.zc.camera.PhotoUtil;
import com.zc.cameramanager.R;
import com.zc.type.OpenType;

public class SimpleDemo extends Activity implements OnClickListener,ImageSelcetListernAsy {

    private ImageView imageView;
    private CameraHandler cameraHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView1);
        cameraHandler=new CameraHandler(this);
        cameraHandler.setImageSelcetListernAsy(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.button1:
            cameraHandler.setCameraOptions(CameraOptions.creatOptions(OpenType.OPEN_CAMERA));
            break;
        case R.id.button2:
            cameraHandler.setCameraOptions(CameraOptions.creatOptions(OpenType.OPEN_CAMERA_CROP).setCropBuilder(new CropBuilder(2, 3, 300, 450)));
            break;
        case R.id.button3:
            cameraHandler.setCameraOptions(CameraOptions.creatOptions(OpenType.OPEN_GALLERY));
            break;
        case R.id.button4:
            cameraHandler.setCameraOptions(CameraOptions.creatOptions(OpenType.OPEN_GALLERY_CROP).setCropBuilder(new CropBuilder(2, 3, 300, 450)));
            break;
        default:
            break;
        }
        cameraHandler.start();

    }

    public void setImageView(String path) {
        Bitmap decodeFile = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(decodeFile);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(cameraHandler != null){
            cameraHandler.dispatchCamera(requestCode,resultCode,data);
        }
        

    }

    @Override
    public void onSelectedAsy(String imgPath) {
        setImageView(imgPath);
    }
}
