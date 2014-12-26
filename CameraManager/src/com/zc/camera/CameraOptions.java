package com.zc.camera;

import java.io.File;
import java.io.Serializable;

import com.zc.type.OpenType;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * camera builder
 * 
 * @author zhaocheng
 * 
 */
public class CameraOptions implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2336944222293048240L;
    private static final String URI_KEY = "PHOTO_URI";
    private Context mContext;
    private PhotoUri mPhotoUri;
    private CropBuilder mCropBuilder;
    private OpenType mOpenType;
    private static CameraOptions mOptions;

    public Uri getFileUri() {
        if (mPhotoUri == null) {
            getmPhotoUri();
        }
        if (mPhotoUri.getFileUri() != null) {
            return mPhotoUri.getFileUri();
        } else {
            if (mPhotoUri.getFileUri() == null) {
                SharedPreferences mPreferences = mContext.getSharedPreferences(
                        "setting", 0);
                String str = mPreferences.getString(URI_KEY, "");
                mPhotoUri.setFileUri(Uri.fromFile(FileUtil.createFile(str)));

            }
        }
        return mPhotoUri.getFileUri();
    }

    public PhotoUri getmPhotoUri() {
        return mPhotoUri != null ? mPhotoUri : (mPhotoUri = new PhotoUri(
                FileUtil.createFile(DefaultOptions.TEMPFILE),
                Uri.fromFile(FileUtil.createFile(DefaultOptions.FILEURI))));
    }

    public CameraOptions setmPhotoUri(PhotoUri mPhotoUri) {
        this.mPhotoUri = mPhotoUri;
        return this;
    }

    public OpenType getmOpenType() {
        return mOpenType != null ? mOpenType : DefaultOptions.OPEN_TYPE;
    }

    public CameraOptions setmOpenType(OpenType mOpenType) {
        this.mOpenType = mOpenType;
        return this;
    }

    public CropBuilder getmCropBuilder() {
        return mCropBuilder != null ? mCropBuilder
                : (mCropBuilder = new CropBuilder(DefaultOptions.X,
                        DefaultOptions.Y, DefaultOptions.width,
                        DefaultOptions.height));
    }

    public CameraOptions setmCropBuilder(CropBuilder mCropBuilder) {
        this.mCropBuilder = mCropBuilder;
        return this;
    }

    public void delUri() {
        try {
            if (mPhotoUri.getFileUri() != null) {
                String fileName = mPhotoUri.getFileUri().getEncodedPath();
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

    }

    /**
     * android compatibility Because individual mobile phone recycling Uri
     * 
     */
    public void saveUri() {
        SharedPreferences mPreferences = mContext.getSharedPreferences(
                "setting", 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(URI_KEY, getFileUri().getPath());
        editor.commit();
    }

    private CameraOptions(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public static CameraOptions getInstance(Context context) {
        if (mOptions == null) {
            mOptions = new CameraOptions(context);
        }
        return mOptions;
    }
}
