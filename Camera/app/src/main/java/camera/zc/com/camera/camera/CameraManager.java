package camera.zc.com.camera.camera;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.eu.janmuller.android.simplecropimage.CropImage;
import com.pingan.papd.camera.album.ImageItem;
import com.pingan.papd.camera.album.SelectImageActivity;
import com.pingan.papd.camera.callback.CameraImageListener;
import com.pingan.papd.camera.callback.CameraOperate;
import com.pingan.papd.camera.callback.SelectMoreListener;
import com.pingan.papd.camera.options.CameraOptions;
import com.pingan.papd.camera.type.OpenType;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * camera util Used to invoke the android camera image compression processing
 *
 * @author zhaocheng
 */
public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();


    public static final String MAX_SELECT_ACTION = "com.photo.album.MAX_SELECT_ACTION";

    /**
     * open android camera
     */
    public static final int OPEN_CAMERA_CODE = 0x00000122;

    /**
     * open android gallery
     */
    public static final int OPEN_GALLERY_CODE = 0x00000123;

    /**
     * crop the custom
     */
    public static final int CROP_PHOTO_CODE = 0x00000124;

    public static final int OPEN_USER_ALBUM = 0x00000125;

    /**
     * get photo for local
     */
    public static final int LOCAL_IMAGE = 0x00000005;

    private static final String FORK_CAMERA_PACKAGE = "com.android.camera";

    private Context mContext;

    private CameraOptions mOptions;

    private Bitmap compressImage;

    private CameraOperate mCameraOperate;

    private CameraImageListener mImageListern;

    private SelectMoreListener mSelectMoreListener;

    public CameraManager(Context context, CameraOperate cameraOperate,
                         CameraOptions mBuilder, CameraImageListener imageListener, SelectMoreListener moreListener) {
        super();
        this.mContext = context;
        this.mCameraOperate = cameraOperate;
        this.mOptions = mBuilder;
        this.mImageListern = imageListener;
    }

    public CameraManager(Context mContext, CameraOperate mCameraOperate,
                         CameraImageListener imageListener, SelectMoreListener moreListener) {
        super();
        this.mContext = mContext;
        this.mCameraOperate = mCameraOperate;
        this.mImageListern = imageListener;
        this.mSelectMoreListener = moreListener;
    }

    public CameraOptions getOptions() {
        if (mOptions == null) {
            mOptions = new CameraOptions(mContext);
        }
        return mOptions;
    }

    public void setOptions(CameraOptions options) {
        this.mOptions = options;
    }

    public void setSelectMoreListener(SelectMoreListener moreListener) {
        this.mSelectMoreListener = moreListener;
    }

    public void process() throws ClassNotFoundException {
        distingOn();

    }

    private void distingOn() throws ClassNotFoundException {
        switch (mOptions.getOpenType()) {
            case OPEN_CAMERA:
            case OPEN_CAMERA_CROP:
                if (mCameraOperate != null)
                    mCameraOperate.onOpenCamera(getOpenCameraOpera());
                break;
            case OPEN_GALLERY:
            case OPEN_GALLERY_CROP:
                if (mCameraOperate != null)
                    mCameraOperate.onOpenGallery(getOpenGalleryOpera());
                break;
            case OPENN_USER_ALBUM:
                if (mCameraOperate != null)
                    mCameraOperate.onOpenUserAlbum(getOpenAlbum());
                break;
            default:
                break;
        }
    }

    private Intent getOpenAlbum() {
        Intent intent = new Intent(mContext, SelectImageActivity.class);
        intent.putExtra(MAX_SELECT_ACTION, mOptions.getMaxSelect());
        return intent;
    }

    /**
     * open android camera
     *
     * @throws ClassNotFoundException Camera damaged or cannot be found
     */
    private Intent getOpenCameraOpera() throws ClassNotFoundException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            final Intent intent_camera = mContext.getPackageManager()
                    .getLaunchIntentForPackage(FORK_CAMERA_PACKAGE);
            if (intent_camera != null) {
                intent.setPackage(FORK_CAMERA_PACKAGE);
            }
        } catch (Exception e) {
        }
        if (getOptions().getFileUri() != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOptions().getFileUri());
        else {
            Log.e(TAG, "fileUri is empty");
            return null;
        }
        Log.d(TAG, "try open camera success !");
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
        intent.putExtra(CropImage.IMAGE_PATH, getOptions().getFileUri()
                .getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, getOptions().getCropBuilder()
                .getX());
        intent.putExtra(CropImage.ASPECT_Y, getOptions().getCropBuilder()
                .getY());
        intent.putExtra(CropImage.OUTPUT_X, getOptions().getCropBuilder()
                .getWeight());
        intent.putExtra(CropImage.OUTPUT_Y, getOptions().getCropBuilder()
                .getHeight());
        return intent;
    }

    /**
     * open camer result
     */
    public void cameraResult() {
        compressImage = PhotoUtil.getPhoto(getOptions());
        if (getOptions().getOpenType() == OpenType.OPEN_CAMERA_CROP
                && null != compressImage) {
            writePhotoFile(getOptions().getFileUri().getPath(), compressImage);
            if (mCameraOperate != null)
                mCameraOperate.onOpenCrop(getOpenCropOpera());
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
            FileUtil.closeQuietly(baos, outputStream);
        }

    }

    public void galleryResult(Intent data) {
        ContentResolver resolver = mContext.getContentResolver();
        if (null != data && null != data.getData()) {
            try {

                InputStream is = resolver.openInputStream(data.getData());
                FileOutputStream os = new FileOutputStream(getOptions()
                        .getFileUri().getPath());
                PhotoUtil.copyStream(is, os);
                FileUtil.closeQuietly(os, is);
                if (getOptions().getOpenType() == OpenType.OPEN_GALLERY_CROP) {
                    if (mCameraOperate != null)
                        mCameraOperate.onOpenCrop(getOpenCropOpera());
                } else {
                    compressPhoto();
                }
            } catch (Exception e) {

            }

        }
    }

    public void forResult(int requestCode, Intent data) {
        switch (requestCode) {
            case OPEN_CAMERA_CODE:
                cameraResult();
                break;
            case CROP_PHOTO_CODE:
                // 裁剪图片 不生成缩略图
//                mOptions.setImThumbnailsPhoto(false);
                getOptions().setImThumbnailsPhoto(false);
                compressPhoto();
                break;
            case OPEN_GALLERY_CODE:
                galleryResult(data);
            case OPEN_USER_ALBUM:
                albumResult(data);
                break;
            default:
                break;
        }

    }

    public void albumResult(Intent data) {
        if (null != data && mSelectMoreListener != null) {
            List<ImageItem> pathList = data.getParcelableArrayListExtra(MAX_SELECT_ACTION);
            mSelectMoreListener.onSelectedMoreListener(pathList);
        } else {
            Log.e(TAG, "SelectMoreListener is null");
        }
    }

    /**
     * The compressed image
     */
    public void compressPhoto() {
        new CompressPhotoTask().execute();
    }

    private class CompressPhotoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            compressImage = PhotoUtil.getPhoto(getOptions());
            if (compressImage != null) {
                PhotoUtil.depositInDisk(compressImage, getOptions());
                getOptions().delUri();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (compressImage != null) {
                if (mImageListern != null)
                    mImageListern.onSelectedAsy(getOptions().getPhotoUri()
                            .getTempFile().getAbsolutePath());
                if (mSelectMoreListener != null) {
                    List<ImageItem> pathList = new ArrayList<ImageItem>();
                    ImageItem item = new ImageItem();
                    item.imagePath = getOptions().getPhotoUri()
                            .getTempFile().getAbsolutePath();
                    item.thumbnailPath = getOptions().getPhotoUri()
                            .getTempFile().getAbsolutePath();
                    pathList.add(item);
                    mSelectMoreListener.onSelectedMoreListener(pathList);
                }

            }
            setOptions(null);
        }
    }
}
