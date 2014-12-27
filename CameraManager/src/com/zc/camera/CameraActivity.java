package com.zc.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zc.camera.callback.CameraOperate;

/**
 * different intent open different camer or gallery, intent give builder
 *
 * @author zhaocheng
 *
 */
public class CameraActivity extends Activity implements ImageSelcetListernAsy ,CameraOperate {
    private static final String TAG = CameraActivity.class.getSimpleName();

    /**
     * open android camera
     */
    private static final int OPEN_CAMERA_CODE = 0x00000002;

    /**
     * open android gallery
     */
    private static final int OPEN_GALLERY_CODE = 0x00000003;

    /**
     * crop the custom
     */
    private static final int CROP_PHOTO_CODE = 0x00000004;


    private CameraManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        try {
            manager = new CameraManager(this, this);
            manager.setCameraOperate(this);
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
                case OPEN_CAMERA_CODE:
                    manager.cameraResult();
                    break;
                case CROP_PHOTO_CODE:
                    manager.compressPhoto();
                    break;
                case OPEN_GALLERY_CODE:
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

    @Override
    public void openCamera(Intent intent) {
        startActivityForResult(intent, OPEN_CAMERA_CODE);
    }

    @Override
    public void openGallery(Intent intent) {
        startActivityForResult(intent, OPEN_GALLERY_CODE);
    }

    @Override
    public void openCrop(Intent intent) {
        startActivityForResult(intent, CROP_PHOTO_CODE);
    }
}
