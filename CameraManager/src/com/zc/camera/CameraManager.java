package com.zc.camera;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import com.zc.camera.callback.CameraOperate;
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
     * get photo for local
     */
    public static final int LOCAL_IMAGE = 0x00000005;

    private static final String FORK_CAMERA_PACKAGE = "com.android.camera";

    private Context mContext;

    public static CameraOptions mBuilder;

    private Bitmap compressImage;

    private ImageSelcetListernAsy mAsyListern;

    private CameraOperate mCameraOperate;

    private Handler mHandler;

    public CameraManager(Context context, ImageSelcetListernAsy listernAsy)
            throws ClassNotFoundException {
        super();
        this.mContext = context;
        this.mAsyListern = listernAsy;

        if (mBuilder == null) {
            mBuilder = CameraOptions.getInstance(context);
        }
        distingOn();
    }

    public void setCameraOperate(CameraOperate cameraOperate) {
        this.mCameraOperate = cameraOperate;
    }

    private void distingOn() throws ClassNotFoundException {
        switch (mBuilder.getmOpenType()) {
            case OPEN_CAMERA:
            case OPEN_CAMERA_CROP:
                if (mCameraOperate != null)
                    mCameraOperate.openCamera(getOpenCameraOpera());
                break;
            case OPEN_GALLERY:
            case OPEN_GALLERY_CROP:
                if (mCameraOperate != null)
                    mCameraOperate.openGallery(getOpenGalleryOpera());
                break;
            default:
                break;
        }
    }

    /**
     * open android camera
     *
     * @throws ClassNotFoundException Camera damaged or cannot be found
     */
    private Intent getOpenCameraOpera() throws ClassNotFoundException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent intent_camera = mContext.getPackageManager()
                .getLaunchIntentForPackage(FORK_CAMERA_PACKAGE);
        if (intent_camera != null) {
            intent.setPackage(FORK_CAMERA_PACKAGE);
        }
        if (mBuilder.getFileUri() != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mBuilder.getFileUri());
        else {
            Log.e(TAG, "fileUri is empty");
            return null;
        }
        return intent;
    }

    /**
     * According to different using a different version number Action
     *
     * @throws ClassNotFoundException Camera damaged or cannot be found
     */
    private Intent getOpenGalleryOpera() throws ClassNotFoundException {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    /**
     * Because individual android call system will cut out the problem so this
     * is crop is this custom
     */
    private Intent getOpenCropOpera() {
        Intent intent = new Intent(mContext, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mBuilder.getFileUri().getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, mBuilder.getmCropBuilder().getX());
        intent.putExtra(CropImage.ASPECT_Y, mBuilder.getmCropBuilder().getY());
        intent.putExtra(CropImage.OUTPUT_X, mBuilder.getmCropBuilder()
                .getWeight());
        intent.putExtra(CropImage.OUTPUT_Y, mBuilder.getmCropBuilder()
                .getHeight());
        return intent;
    }

    /**
     * open camer result
     */
    public void cameraResult() {
        compressImage = PhotoUtil.getPhoto(mBuilder);
        if (mBuilder.getmOpenType() == OpenType.OPEN_CAMERA_CROP
                && null != compressImage) {
            writePhotoFile(mBuilder.getFileUri().getPath(), compressImage);
            if (mCameraOperate != null)
                mCameraOperate.openCrop(getOpenCropOpera());
        } else {
            compressPhoto();

        }
    }

    private void writePhotoFile(String str, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream outputStream = null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            outputStream = new FileOutputStream(str);
            baos.writeTo(outputStream);
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           FileUtil.closeQuietly(baos,outputStream);
        }

    }



    public void galleryResult(Intent data) {
        ContentResolver resolver = mContext.getContentResolver();
        if (null != data && null != data.getData()) {
            try {

                InputStream is = resolver.openInputStream(data.getData());
                FileOutputStream os = new FileOutputStream(mBuilder
                        .getFileUri().getPath());
                PhotoUtil.copyStream(is, os);
                FileUtil.closeQuietly(os,is);
                if (mBuilder.getmOpenType() == OpenType.OPEN_GALLERY_CROP) {
                    //openCrop();
                    if (mCameraOperate != null)
                        mCameraOperate.openCrop(getOpenCropOpera());
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
        if (mHandler == null) {
            mHandler = new Handler(mContext.getMainLooper());
        }
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mAsyListern.onSelectedAsy(mBuilder.getmPhotoUri().getTempFile()
                        .getPath());
            }
        });
    }

}
