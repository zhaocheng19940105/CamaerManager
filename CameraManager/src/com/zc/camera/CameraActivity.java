package com.zc.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * different intent open different camer or gallery, intent give builder
 * 
 * @author zhaocheng
 * 
 */
public class CameraActivity extends Activity implements ImageSelcetListernAsy {
    private static final String TAG = CameraActivity.class.getSimpleName();
    private CameraManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        try {
            manager = new CameraManager(this, this);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "no find camera");
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedAsy(String imgPath) {
        Intent intent = new Intent();
        intent.putExtra(PhotoUtil.INTENT_PATH, imgPath);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
            case CameraManager.OPEN_CAMERA_CODE:
                manager.cameraResult();
                break;
            case CameraManager.CROP_PHOTO_CODE:
                manager.compressPhoto();
                break;
            case CameraManager.OPEN_GALLERY_CODE:
                manager.galleryResult(data);
                break;
            default:
                break;
            }

        } else {
            setResult(0);
            this.finish();
        }
    }
}
