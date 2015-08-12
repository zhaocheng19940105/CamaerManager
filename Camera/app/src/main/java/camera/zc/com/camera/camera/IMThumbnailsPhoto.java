package camera.zc.com.camera.camera;

import android.content.Context;

import com.pingan.papd.camera.options.DefaultOptions;

import java.io.File;
import java.io.Serializable;

public class IMThumbnailsPhoto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6473013579572982830L;
    private Context mContext;
    private File tempFileThumbnail = null;
    private int maxWidth;
    private int maxHeight;

    private int smallWidth;
    private int smallHeight;

    private float growSmallScale;
    private float widthSmallScale;

    private boolean isCreateThunmbnail;

    public boolean getIsCreateThunmbnail() {
        return isCreateThunmbnail;
    }

    public IMThumbnailsPhoto setIsCreateThunmbnail(boolean isCreate) {
        this.isCreateThunmbnail = isCreate;
        return this;
    }

    public IMThumbnailsPhoto setCreateThunmbnail(boolean isCreateThunmbnail) {
        this.isCreateThunmbnail = isCreateThunmbnail;
        return this;
    }

    public File getTempFileThumbnail() {
        return tempFileThumbnail;
    }

    public void setTempFileThumbnail(File tempFileThumbnail) {
        this.tempFileThumbnail = tempFileThumbnail;
    }

    public int getMaxWidth() {
        return maxWidth != 0 ? maxWidth : DefaultOptions.getInstance(mContext)
                .getMaxWidth();
    }

    public IMThumbnailsPhoto setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public int getMaxHeight() {
        return maxHeight != 0 ? maxHeight : DefaultOptions
                .getInstance(mContext).getMaxHeight();
    }

    public IMThumbnailsPhoto setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public int getSmallWidth() {
        return smallWidth != 0 ? smallWidth : DefaultOptions.getInstance(
                mContext).getSmallWidth();
    }

    public IMThumbnailsPhoto setSmallWidth(int smallWidth) {
        this.smallWidth = smallWidth;
        return this;
    }

    public int getSmallHeight() {
        return smallHeight != 0 ? smallHeight : DefaultOptions.getInstance(
                mContext).getSmallHeigth();
    }

    public IMThumbnailsPhoto setSmallHeight(int smallHeight) {
        this.smallHeight = smallHeight;
        return this;
    }

    public float getGrowSmallScale() {
        return growSmallScale != 0f ? growSmallScale
                : DefaultOptions.GROWSMALLSCALE;
    }

    public IMThumbnailsPhoto setGrowSmallScale(float growSmallScale) {
        this.growSmallScale = growSmallScale;
        return this;
    }

    public float getWidthSmallScale() {
        return widthSmallScale != 0f ? widthSmallScale
                : DefaultOptions.WIDESMALLSCALE;
    }

    public IMThumbnailsPhoto setWidthSmallScale(float widthSmallScale) {
        this.widthSmallScale = widthSmallScale;
        return this;
    }

    public IMThumbnailsPhoto(File tempFileThumbnail, int maxWidth,
                             int maxHeight, int smallWidth, int smallHeight,
                             float growSmallScale, float widthSmallScale,
                             boolean isCreateThunmbnail, Context mContext) {
        super();
        this.mContext = mContext;
        this.tempFileThumbnail = tempFileThumbnail;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.smallWidth = smallWidth;
        this.smallHeight = smallHeight;
        this.growSmallScale = growSmallScale;
        this.widthSmallScale = widthSmallScale;
        this.isCreateThunmbnail = isCreateThunmbnail;
    }

    public IMThumbnailsPhoto(File tempFileThumbnail,
                             boolean isCreateThunmbnail, Context mContext) {
        super();
        this.mContext = mContext;
        this.tempFileThumbnail = tempFileThumbnail;
        this.isCreateThunmbnail = isCreateThunmbnail;
    }

    public IMThumbnailsPhoto(boolean isCreateThunmbnail, Context mContext) {
        super();
        this.mContext = mContext;
        this.isCreateThunmbnail = isCreateThunmbnail;
    }

}
