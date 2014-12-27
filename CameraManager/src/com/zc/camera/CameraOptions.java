package com.zc.camera;

import java.io.File;
import java.io.Serializable;

import com.zc.type.OpenType;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * camera builder
 *
 * @author zhaocheng
 */
public class CameraOptions implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2336944222293048240L;

    private static final String PERF_CONFIG_NAME="setting";
    private static final String URI_KEY = "PHOTO_URI";
    private Context mContext;
    private PhotoUri mPhotoUri;
    private CropBuilder mCropBuilder;
    private OpenType mOpenType;
    //private static CameraOptions mOptions;

    public Uri getFileUri() {
        if (mPhotoUri == null) {
            getPhotoUri();
        }
        if (mPhotoUri.getFileUri() != null) {
            return mPhotoUri.getFileUri();
        } else {
            if (mPhotoUri.getFileUri() == null) {

                String str = getDefaultSharedPreferences().getString(URI_KEY, "");
                mPhotoUri.setFileUri(Uri.fromFile(FileUtil.createFile(str)));

            }
        }

        return mPhotoUri.getFileUri();
    }

    private SharedPreferences getDefaultSharedPreferences(){
        return mContext.getSharedPreferences(
                PERF_CONFIG_NAME, 0);
    }

    public PhotoUri getPhotoUri() {
        return mPhotoUri != null ? mPhotoUri : (mPhotoUri = new PhotoUri(
                FileUtil.createFile(DefaultOptions.TEMPFILE),
                Uri.fromFile(FileUtil.createFile(DefaultOptions.FILEURI))));
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
     */
    public void saveUri() {
        SharedPreferences mPreferences =getDefaultSharedPreferences();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(URI_KEY, getFileUri().getPath());
        editor.commit();
    }

    public static Intent getCameraIntent(Context context){
        return new Intent(context,CameraActivity.class);
    }

    public CameraOptions(Context mContext,OperaAction action) {
        super();
        this.mContext = mContext;
        this.mOpenType=action.getOpenType();
        this.mCropBuilder=action.getCropBuilder();
        this.mPhotoUri=action.getPhotoUri();
    }
}
