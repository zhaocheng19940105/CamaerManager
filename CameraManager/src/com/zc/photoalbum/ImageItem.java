package com.zc.photoalbum;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem implements Parcelable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;

    @Override
    public int describeContents() {
        return 0;
    }

    public ImageItem() {
        super();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(thumbnailPath);
        dest.writeString(imagePath);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public ImageItem(Parcel source) {
        imageId = source.readString();
        thumbnailPath = source.readString();
        imagePath = source.readString();
        isSelected = source.readByte() != 0;
    }

    public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }

        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

    };

}
