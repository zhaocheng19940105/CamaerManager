package com.zc.camera;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.zc.crop.CropImage;
import com.zc.type.OpenType;

/**
 * camera util Used to invoke the android camera image compression processing
 * 
 * @author zhaocheng
 */
public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    /**
     * open android camera
     */
    public static final int OPEN_CAMERA_CODE = 0x00000002;

    /**
     * open android gallery
     */
    public static final int OPEN_GALLERY_CODE = 0x00000003;

    /**
     * crop the custom
     */
    public static final int CROP_PHOTO_CODE = 0x00000004;

    /**
     * get photo for local
     */
    public static final int LOCAL_IMAGE = 0x00000005;

    private Activity mActivity;

    public static CameraOptions mBuilder;

    private Bitmap compressImage;

    private ImageSelcetListernAsy mAsyListern;

    public CameraManager(Activity activity, ImageSelcetListernAsy listernAsy)
            throws ClassNotFoundException {
        super();
        this.mActivity = activity;
        this.mAsyListern = listernAsy;
        if (mBuilder == null) {
            mBuilder = CameraOptions.getInstance(activity);
        }
        distingOn();
    }

    private void distingOn() throws ClassNotFoundException {
        switch (mBuilder.getmOpenType()) {
        case OPEN_CAMERA:
        case OPEN_CAMERA_CROP:
            openCamera();
            break;
        case OPEN_GALLERY:
        case OPEN_GALLERY_CROP:
            openGallery();
            break;
        default:
            break;
        }
    }

    /**
     * open android camera
     * 
     * @throws ClassNotFoundException
     *             Camera damaged or cannot be found
     */
    public void openCamera() throws ClassNotFoundException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent intent_camera = mActivity.getPackageManager()
                .getLaunchIntentForPackage("com.android.camera");
        if (intent_camera != null) {
            intent.setPackage("com.android.camera");
        }
        if (mBuilder.getFileUri() != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mBuilder.getFileUri());
        else {
            Log.e(TAG, "fileUri is empty");
            return;
        }

        mActivity.startActivityForResult(intent, OPEN_CAMERA_CODE);
    }

    /**
     * According to different using a different version number Action
     * 
     * @throws ClassNotFoundException
     *             Camera damaged or cannot be found
     */
    public void openGallery() throws ClassNotFoundException {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(intent, OPEN_GALLERY_CODE);

    }

    /**
     * Because individual android call system will cut out the problem so this
     * is crop is this custom
     * 
     */
    public void openCrop() {
        Intent intent = new Intent(mActivity, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mBuilder.getFileUri().getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, mBuilder.getmCropBuilder().getX());
        intent.putExtra(CropImage.ASPECT_Y, mBuilder.getmCropBuilder().getY());
        intent.putExtra(CropImage.OUTPUT_X, mBuilder.getmCropBuilder()
                .getWeight());
        intent.putExtra(CropImage.OUTPUT_Y, mBuilder.getmCropBuilder()
                .getHeight());
        mActivity.startActivityForResult(intent, CROP_PHOTO_CODE);
    }

    /**
     * open camer result
     */
    public void cameraResult() {
        compressImage = PhotoUtil.getPhoto(mBuilder);
        if (mBuilder.getmOpenType() == OpenType.OPEN_CAMERA_CROP
                && null != compressImage) {
            writePhotoFile(mBuilder.getFileUri().getPath(), compressImage);
            openCrop();
        } else {
            compressPhoto();

        }
    }

    private void writePhotoFile(String str, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        try {
            FileOutputStream outputStream = new FileOutputStream(str);
            baos.writeTo(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void galleryResult(Intent data) {
        ContentResolver resolver = mActivity.getContentResolver();
        if (null != data && null != data.getData()) {
            try {

                InputStream is = resolver.openInputStream(data.getData());
                FileOutputStream os = new FileOutputStream(mBuilder
                        .getFileUri().getPath());
                PhotoUtil.copyStream(is, os);
                os.close();
                is.close();
                if (mBuilder.getmOpenType() == OpenType.OPEN_GALLERY_CROP) {
                    openCrop();
                } else {
                    compressPhoto();
                }
            } catch (Exception e) {

            }

        }
    }

    /**
     * The compressed image
     */
    public void compressPhoto() {
        new Thread() {
            @Override
            public void run() {
                compressImage = PhotoUtil.getPhoto(mBuilder);
                PhotoUtil.depositInDisk(compressImage, mBuilder);
                getImagePath();
                super.run();
            }

        }.start();
    }

    private void getImagePath() {
        mBuilder.delUri();
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mAsyListern.onSelectedAsy(mBuilder.getmPhotoUri().getTempFile()
                        .getPath());
            }
        });

    }

}
