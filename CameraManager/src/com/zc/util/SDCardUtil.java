package com.zc.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class SDCardUtil {
    private static final String DIR_WORK = "/pajk/papd/";

    /**
     * 检测sd卡是否可用
     * @return
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取sd卡路径
     * @return
     */
    public static String getPath() {
        if (checkSDCard()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }

    /**
     * 获取sd卡默认缓存文件路径
     * @param ctx
     * @return eg:/storage/emulated/0/Android/data/项目包名/files
     */
    public static String getDefaultCachePath(Context ctx){
        if (checkSDCard()) {
            File file =  ctx.getExternalFilesDir(null);
            if (null ==  file) {
                return Environment.getExternalStorageDirectory() + DIR_WORK;
            }
            return file.getAbsolutePath();
        }
        return "";
    }
}
