package camera.zc.com.camera.camera.options;

import android.content.Context;
import android.net.Uri;

import com.pajk.hm.sdk.android.util.MD5Utils;
import com.pingan.papd.camera.FileConstants;
import com.pingan.papd.camera.FileUtil;
import com.pingan.papd.camera.type.OpenType;

import java.io.File;

public class DefaultOptions {
    public static final int X = 1;
    public static final int Y = 1;
    public static final int width = 300;
    public static final int height = 300;
    public static final int maxSelect = 5;
    public static final OpenType OPEN_TYPE = OpenType.OPEN_CAMERA;
    public static final String IMG_PREFIX_DEFAULT = "IMG";
    public static final String IMG_TEMP_DEFAULT = "TMP";
    public static final String THUMBNAIL_FILE_FORMAT = MD5Utils.toMD5("_300x300");
    public static float GROWSMALLSCALE = 0.15f;
    public static float WIDESMALLSCALE = 10.5f;
    private static DefaultOptions options;
    private Context mContext;

    private DefaultOptions(Context context) {
        this.mContext = context;
    }

    public static DefaultOptions getInstance(Context context) {
        if (options == null) {
            options = new DefaultOptions(context);
        }

        return options;
    }

    public int getMaxWidth() {
        return dip2px(300);
    }

    public int getMaxHeight() {
        return dip2px(300);
    }

    public int getSmallWidth() {
        return dip2px(150);
    }

    public int getSmallHeigth() {
        return dip2px(150);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public File getDefaultTempFile() {
        StringBuffer buffer = new StringBuffer(
                FileConstants.getImageDirPath(mContext));
        FileUtil.mkdirs(buffer.toString());
        buffer.append(MD5Utils.toMD5(IMG_TEMP_DEFAULT));
        buffer.append(MD5Utils.toMD5(System.currentTimeMillis()+""));
        return FileUtil.createFile(buffer.toString());
    }

    public File getDefaultThumbnailFile(String tempfile) {
        String replace = tempfile+THUMBNAIL_FILE_FORMAT;
        return FileUtil.createFile(replace);
    }

    public Uri getDefaultFileUri() {
        StringBuffer buffer = new StringBuffer(
                FileConstants.getImageDirPath(mContext));
        FileUtil.mkdirs(buffer.toString());
        buffer.append(MD5Utils.toMD5(IMG_PREFIX_DEFAULT));
        buffer.append(MD5Utils.toMD5(System.currentTimeMillis()+""));
        return Uri.fromFile(FileUtil.createFile(buffer.toString()));
    }
}
