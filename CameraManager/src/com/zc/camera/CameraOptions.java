package com.zc.camera;

import java.io.File;
import java.io.Serializable;

import com.zc.type.OpenType;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * camera builder
 *
 * @author zhaocheng
 */
public class CameraOptions implements Parcelable {

    public static final String INTENT_ACTION="com.zc.camera.CameraOptions.ACTION";

    private static final String PERF_CONFIG_NAME="setting";
    private static final String URI_KEY = "PHOTO_URI";
    private Context mContext;
    private PhotoUri mPhotoUri;
    private CropBuilder mCropBuilder;
    private OpenType mOpenType;

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

    public CameraOptions(Context mContext) {
        super();
        this.mContext = mContext;
    }

    void init(Context context){
        this.mContext=context;
    }

    public static final Parcelable.Creator<CameraOptions> CREATOR = new Parcelable.Creator<CameraOptions>() {

        @Override
        public CameraOptions[] newArray(int size) {
            return new CameraOptions[size];
        }

        @Override
        public CameraOptions createFromParcel(Parcel source) {
            // TODO Auto-generated method stub

            return new CameraOptions(source);
        }

    };

    private CameraOptions(Parcel source){
        mOpenType= (OpenType) source.readSerializable();
        mCropBuilder= (CropBuilder) source.readSerializable();
        mPhotoUri= (PhotoUri) source.readSerializable();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mOpenType);
        dest.writeSerializable(mCropBuilder);
        dest.writeSerializable(mPhotoUri);
    }

    public Intent builder(Context context){
        Intent intent=new Intent(context,CameraActivity.class);
        intent.putExtra(CameraOptions.INTENT_ACTION,this);
        return intent;
    }

    public CameraOptions(OpenType openType,CropBuilder cropBuilder,PhotoUri photoUri){
        this.mOpenType=openType;
        this.mCropBuilder=cropBuilder;
        this.mPhotoUri=photoUri;
    }

    public CameraOptions(OpenType openType){
        this(openType,null,null);
    }

    public CameraOptions(OpenType openType,PhotoUri photoUri){
        this(openType,null,photoUri);
    }

    public CameraOptions(OpenType openType,CropBuilder cropBuilder){
        this(openType,cropBuilder,null);
    }

    public static CameraOptions creatOptions(OpenType openType){
        return new CameraOptions(openType);
    }

}
