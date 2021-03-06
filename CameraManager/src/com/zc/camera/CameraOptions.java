package com.zc.camera;

import java.io.File;
import java.io.Serializable;

import com.zc.type.OpenType;
import com.zc.util.FileUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;

/**
 * camera builder
 *
 * @author zhaocheng
 */
public class CameraOptions implements Serializable {
    private static final long serialVersionUID = 6057732981925437783L;
    public static final String INTENT_ACTION = "com.zc.camera.CameraOptions.ACTION";
    private static final String PERF_CONFIG_NAME = "setting";
    private static final String URI_KEY = "PHOTO_URI";
    private Context mContext;
    private PhotoUri mPhotoUri;
    private OpenType mOpenType;
    private CropBuilder mCropBuilder;
    private int maxSelect;
    private IMThumbnailsPhoto mImThumbnailsPhoto;
    private String mPhotoForMat = PhotoFormat.PNG;

    public CameraOptions setPhotoForMat(String photoForMat) {
        this.mPhotoForMat = photoForMat;
        DefaultOptions.getInstance(mContext).photoForMat = photoForMat;
        return this;
    }

    public Uri getFileUri() {
        if (mPhotoUri == null) {
            getPhotoUri();
        }
        if (mPhotoUri.getFileUri() != null) {
            return mPhotoUri.getFileUri();
        } else {
            if (mPhotoUri.getFileUri() == null) {

                String str = getDefaultSharedPreferences().getString(URI_KEY,
                        "");
                mPhotoUri.setFileUri(Uri.fromFile(FileUtil.createFile(str)));

            }
        }

        return mPhotoUri.getFileUri();
    }

    private SharedPreferences getDefaultSharedPreferences() {
        return mContext.getSharedPreferences(PERF_CONFIG_NAME, 0);
    }

    public PhotoUri getPhotoUri() {
        if (mPhotoUri != null) {
            return mPhotoUri;
        } else {
            mPhotoUri = new PhotoUri(DefaultOptions.getInstance(mContext)
                    .getDefaultTempFile(), DefaultOptions.getInstance(mContext)
                    .getDefaultFileUri());
        }
        return mPhotoUri;
    }

    public CameraOptions setPhotoUri(PhotoUri mPhotoUri) {
        this.mPhotoUri = mPhotoUri;
        return this;
    }

    public OpenType getOpenType() {
        return mOpenType != null ? mOpenType : DefaultOptions.OPEN_TYPE;
    }

    public CameraOptions setOpenType(OpenType mOpenType) {
        this.mOpenType = mOpenType;
        return this;
    }

    public int getMaxSelect() {
        return maxSelect != 0 ? maxSelect : DefaultOptions.maxSelect;
    }

    public CameraOptions setMaxSelect(int maxSelect) {
        this.maxSelect = maxSelect;
        return this;
    }

    public CropBuilder getCropBuilder() {
        return mCropBuilder != null ? mCropBuilder
                : (mCropBuilder = new CropBuilder(DefaultOptions.X,
                DefaultOptions.Y, DefaultOptions.width,
                DefaultOptions.height));
    }

    public CameraOptions setCropBuilder(CropBuilder mCropBuilder) {
        this.mCropBuilder = mCropBuilder;
        return this;
    }

    public void delUri() {
        delUri(getPhotoUri().getFileUri());
    }

    public void delThumbnailFile() {
        delFile(getmImThumbnailsPhoto().getTempFileThumbnail());
    }

    private void delFile(File file) {
        if (file == null) {
            return;
        }
        try {
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    private void delUri(Uri uri) {
        try {
            if (uri != null) {
                String fileName = uri.getEncodedPath();
                File file = new File(fileName);
                if (file != null && file.exists()) {
                    file.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delErrorUri() {
        delUri(getPhotoUri().getFileUri());
        delFile(getPhotoUri().getTempFile());
        delFile(getmImThumbnailsPhoto().getTempFileThumbnail());
    }

    /**
     * android compatibility Because individual mobile phone recycling Uri
     */
    public void saveUri() {
        SharedPreferences mPreferences = getDefaultSharedPreferences();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(URI_KEY, getFileUri().getPath());
        editor.commit();
    }

    public CameraOptions(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public static CameraOptions createOptions(Context context, OpenType openType) {
        return new CameraOptions(context, openType);
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public IMThumbnailsPhoto getmImThumbnailsPhoto() {
        if (null == mImThumbnailsPhoto) {
            mImThumbnailsPhoto = new IMThumbnailsPhoto(DefaultOptions
                    .getInstance(mContext).getDefaultThumbnailFile(
                            getPhotoUri().getTempFile().getPath()), false,
                    mContext);
        }
        return mImThumbnailsPhoto;
    }

    public CameraOptions setImThumbnailsPhoto(
            IMThumbnailsPhoto mImThumbnailsPhoto) {
        this.mImThumbnailsPhoto = mImThumbnailsPhoto;
        return this;
    }

    public CameraOptions setImThumbnailsPhoto(boolean flage) {
        if (null == mImThumbnailsPhoto) {
            mImThumbnailsPhoto = new IMThumbnailsPhoto(DefaultOptions
                    .getInstance(mContext).getDefaultThumbnailFile(
                            getPhotoUri().getTempFile().getPath()), flage,
                    mContext);
        } else {
            mImThumbnailsPhoto.setIsCreateThunmbnail(flage);
        }
        return this;
    }

    public CameraOptions(Context context, OpenType openType, CropBuilder cropBuilder,
                         PhotoUri photoUri) {
        this.mContext = context;
        this.mOpenType = openType;
        this.mCropBuilder = cropBuilder;
        this.mPhotoUri = photoUri;
    }

    public CameraOptions(Context context, OpenType openType) {
        this(context, openType, null, null);
    }

    public CameraOptions(Context context, OpenType openType, PhotoUri photoUri) {
        this(context, openType, null, photoUri);
    }

    public CameraOptions(Context context, OpenType openType, CropBuilder cropBuilder) {
        this(context, openType, cropBuilder, null);
    }

}
