package com.zc.camera;

import android.os.Parcel;
import android.os.Parcelable;

import com.zc.type.OpenType;

import java.io.Serializable;


public class OperaAction implements Parcelable {

    public static final String ACTION="com.zc.intent.ACTION";

    private OpenType openType;
    private CropBuilder cropBuilder;
    private PhotoUri photoUri;

    public OperaAction(OpenType openType,CropBuilder cropBuilder,PhotoUri photoUri){
        this.openType=openType;
        this.cropBuilder=cropBuilder;
        this.photoUri=photoUri;
    }

    public OperaAction(OpenType openType){
        this(openType,null,null);
    }

    public OperaAction(OpenType openType,PhotoUri photoUri){
        this(openType,null,photoUri);
    }

    public OperaAction(OpenType openType,CropBuilder cropBuilder){
        this(openType,cropBuilder,null);
    }

    public OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(OpenType openType) {
        this.openType = openType;
    }

    public CropBuilder getCropBuilder() {
        return cropBuilder;
    }

    public void setCropBuilder(CropBuilder cropBuilder) {
        this.cropBuilder = cropBuilder;
    }

    public PhotoUri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(PhotoUri photoUri) {
        this.photoUri = photoUri;
    }


    public static final Parcelable.Creator<OperaAction> CREATOR = new Parcelable.Creator<OperaAction>() {

        @Override
        public OperaAction[] newArray(int size) {
            return new OperaAction[size];
        }

        @Override
        public OperaAction createFromParcel(Parcel source) {
            // TODO Auto-generated method stub

            return new OperaAction(source);
        }

    };

     OperaAction(Parcel source){
         openType= (OpenType) source.readSerializable();
         cropBuilder= (CropBuilder) source.readSerializable();
         photoUri= (PhotoUri) source.readSerializable();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(openType);
        dest.writeSerializable(cropBuilder);
        dest.writeSerializable(photoUri);

    }
}
