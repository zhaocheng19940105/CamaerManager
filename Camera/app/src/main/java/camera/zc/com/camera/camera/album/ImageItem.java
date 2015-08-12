package camera.zc.com.camera.camera.album;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem implements Parcelable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public long  imageCreateTime;
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
        dest.writeLong(imageCreateTime);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public ImageItem(Parcel source) {
        imageId = source.readString();
        thumbnailPath = source.readString();
        imagePath = source.readString();
        imageCreateTime=source.readLong();
        isSelected = source.readByte() != 0;
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {

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
