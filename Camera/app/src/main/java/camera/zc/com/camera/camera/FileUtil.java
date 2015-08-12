package camera.zc.com.camera.camera;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

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

    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
