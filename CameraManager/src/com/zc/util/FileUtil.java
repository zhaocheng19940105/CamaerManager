package com.zc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.text.TextUtils;

public class FileUtil {

    /** app Í¼Æ¬ÎÄ¼þÄ¿Â¼ */
    public static final String IMG_DIR = "imgs";
    /**
     * appÍ¼Æ¬»º´æÄ¿Â¼Â·¾¶
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
    /**
     * Deprecated
     * see  android.text.format.Formatter.formatFileSize()
     * @param fileS
     * @return
     */
    @Deprecated
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = fileS + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " G";
        }
        return fileSizeString;
    }

    public static long getFolderSize(File folder) {
        long size = 0;
        java.io.File[] fileList = folder.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    public static String combinPath(String path, String fileName) {
        return path + (path.endsWith(File.separator) ? "" : File.separator)
                + fileName;
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static boolean copyFile(File src, File tar) throws Exception {
        if (src.isFile()) {
            InputStream is = new FileInputStream(src);
            OutputStream op = new FileOutputStream(tar);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(op);
            byte[] bt = new byte[1024 * 8];
            int len = bis.read(bt);
            while (len != -1) {
                bos.write(bt, 0, len);
                len = bis.read(bt);
            }
            bis.close();
            bos.close();
        }
        if (src.isDirectory()) {
            File[] f = src.listFiles();
            tar.mkdir();
            for (int i = 0; i < f.length; i++) {
                copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile()
                        + File.separator + f[i].getName()));
            }
        }
        return true;
    }

    public static boolean moveFile(File src, File tar) throws Exception {
        if (copyFile(src, tar)) {
            deleteFile(src);
            return true;
        }
        return false;
    }

    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }
        }
        f.delete();
    }


    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    public static String getPathExceptSDCard(String apkPath) {
        String tmp = "sdcard/";
        int startIndex = apkPath.indexOf(tmp) + tmp.length();
        return apkPath.substring(startIndex);
    }

    public static void mkdirs(String fileDir) {
        File dir = new File(fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static File createFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static boolean writeContent(String filePath, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readContent(String filePath) {
        try {
            byte[] buffer = new byte[(int) new File(filePath).length()];
            FileInputStream fis = new FileInputStream(new File(filePath));
            fis.read(buffer);
            fis.close();
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getFileName(String urlPath) {
        int start = urlPath.lastIndexOf("/");
        if (start != -1) {
            return urlPath.substring(start + 1);
        } else {
            return null;
        }
    }

    public static String getFileNameFromUrl(String url) {
        if (url == null) {
            return null;
        }
        String filename = url.substring(url.lastIndexOf("/") + 1);
        return filename;
    }

    public static void  closeQuietly(Closeable... closeables){
        if(closeables != null){
            for (Closeable closeable:closeables){
                try{
                    if(closeable!= null){
                        closeable.close();
                    }
                }catch (Exception e){
                }
            }
        }
    }
}
