package camera.zc.com.camera.camera;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

public class FileConstants {
    /** app 图片文件目录 */
    public static final String IMG_DIR = "imgs";
    /**
     * app图片缓存目录路径
     * 
     * @return
     */
    public static final String getImageDirPath(Context ctx) {
        String path = SDCardUtil.getDefaultCachePath(ctx);
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        return path + File.separator + IMG_DIR + File.separator;
    }
}
