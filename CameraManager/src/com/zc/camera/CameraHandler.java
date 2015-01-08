package com.zc.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zc.camera.callback.CameraOperate;

/**
 * different intent open different camer or gallery, intent give builder
 * 
 * @author zhaocheng
 * 
 */
public class CameraHandler implements CameraOperate {
    private static final String TAG = CameraHandler.class.getSimpleName();

    /**
     * open android camera
     */
    private static final int OPEN_CAMERA_CODE = 0x00000122;

    /**
     * open android gallery
     */
    private static final int OPEN_GALLERY_CODE = 0x00000123;

    /**
     * crop the custom
     */
    private static final int CROP_PHOTO_CODE = 0x00000124;

    private static final int OPEN_USER_ALBUM = 0x00000125;

    private CameraManager manager;

    private Context mContext;

    public CameraHandler(Context mContext) {
        this.mContext = mContext;
        manager = new CameraManager(mContext);
        manager.setCameraOperate(this);
    }

    public void setCameraOptions(CameraOptions cameraOptions) {
        manager.setCameraOptions(cameraOptions);
        cameraOptions.init(mContext);
    }

    public void setImageSelcetListernAsy(ImageSelcetListernAsy mAsyListern) {
        if (manager != null)
            manager.setImageSelcetListernAsy(mAsyListern);
    }

    public void start() {
        try {
            manager.process();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void dispatchCamera(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
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

        }
    }

    @Override
    public void onOpenCamera(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    OPEN_CAMERA_CODE);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }

    }

    @Override
    public void onOpenGallery(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    OPEN_GALLERY_CODE);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }
    }

    @Override
    public void onOpenCrop(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    CROP_PHOTO_CODE);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }
    }

    @Override
    public void onOpenUserAlbum(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    OPEN_USER_ALBUM);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }

    }
}
