package camera.zc.com.camera.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.pajk.hm.sdk.android.util.MD5Utils;
import com.pingan.papd.camera.options.CameraOptions;
import com.pingan.papd.camera.options.DefaultOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PhotoUtil {
    public static final String INTENT_BUILDER = "INTENT_BUILDER";
    public static final String INTENT_PATH = "INTENT_PATH";
    public static final int IMAGE_WIDTH = 1200;
    public static final int IMAGE_HEIGHT = 1200;

    /**
     * Different mobile phone photography could rotate images
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        Bitmap createBitmap = null;
        try {
            createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        } catch (OutOfMemoryError error) {
            return bitmap;

        }
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    /**
     * Get the rotation Angle of pictures Get image information, ExifInterface
     * TAG_MAKE photo equipment models, ExifInterface. TAG_MODEL photo equipment
     * brand, ExifInterface. TAG_ORIENTATION photos rotation Angle
     *
     * @param path
     * @return int
     * @Title: readPictureDegree
     * @date 2013-11-27 上午9:22:33
     * @author zhaocheng
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {

            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap getThenumBitmap(CameraOptions builder, int width,
                                         int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(builder.getPhotoUri().getFileUri().getPath(),
                options);
        // Calculate inSampleSize
        options.inPreferredConfig = Config.ARGB_8888;
        float s = options.outWidth / (float) (options.outHeight);
        int calculateInSampleSize = 1;
        //太长太宽图片不做处理
        if (s > builder.getmImThumbnailsPhoto().getWidthSmallScale()) {
        } else if (s < builder.getmImThumbnailsPhoto().getGrowSmallScale()) {
        } else {
            calculateInSampleSize = calculateInSampleSize(options, width,
                    height);
        }

        options.inSampleSize = calculateInSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bm = getLocalBitmap(
                builder.getPhotoUri().getFileUri().getPath(), options);
        return bm;
    }

    /**
     * avoid OutOfMemoryError
     *
     * @param path
     * @param options
     * @return
     */
    public static Bitmap getLocalBitmap(final String path,
                                        final BitmapFactory.Options options) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            ++options.inSampleSize;
            return getLocalBitmap(path, options);
        }
        return bitmap;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            } else {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight;
            while (totalPixels / inSampleSize > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap getPhoto(CameraOptions build) {
        return rotateBitmap(getThenumBitmap(build, IMAGE_WIDTH, IMAGE_HEIGHT),
                readPictureDegree(build.getFileUri().getPath()));
    }
    public static Bitmap getPhoto(String file) {
        return rotateBitmap(getThenumBitmap(file, IMAGE_WIDTH, IMAGE_HEIGHT),
                readPictureDegree(file));
    }
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static Bitmap cutBitmapHight(Bitmap bm,
                                        IMThumbnailsPhoto thunmbnailPhoto) {
        int height = bm.getHeight() / 2;
        Bitmap createBitmap = null;
        try {

            if (height - thunmbnailPhoto.getMaxHeight() / 2 > 0) {
                createBitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() / 2
                                - thunmbnailPhoto.getMaxHeight() / 2, bm.getWidth(),
                        thunmbnailPhoto.getMaxHeight());
            } else {
                createBitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() / 2,
                        bm.getWidth(), thunmbnailPhoto.getMaxHeight());
            }
            if (bm != createBitmap) {
                bm.recycle();
                bm = null;
            }
        } catch (OutOfMemoryError error) {
            createBitmap = bm;
        }
        return createBitmap;
    }

    public Bitmap cutBitmapWidth(Bitmap bm, IMThumbnailsPhoto imThunmbnailPhoto) {
        int width = bm.getWidth() / 2;
        Bitmap createBitmap = null;
        try {

            if (width - imThunmbnailPhoto.getMaxWidth() / 2 > 0) {
                createBitmap = Bitmap.createBitmap(bm, bm.getWidth() / 2
                                - imThunmbnailPhoto.getMaxWidth() / 2, 0,
                        imThunmbnailPhoto.getMaxWidth(), bm.getHeight());
            } else {
                createBitmap = Bitmap.createBitmap(bm, bm.getWidth() / 2, 0,
                        imThunmbnailPhoto.getMaxWidth(), bm.getHeight());
            }
            if (bm != createBitmap) {
                bm.recycle();
                bm = null;
            }
            return createBitmap;
        } catch (OutOfMemoryError error) {
            return bm;
        }
    }

    public static String depositInDisk(Context context,Bitmap compressImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 90;
        compressImage.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        while (baos.toByteArray().length / 2048 >= 2048) {
            if (quality >= 50) {
                baos.reset();
                quality -= 5;
                compressImage.compress(Bitmap.CompressFormat.JPEG, quality,
                        baos);
            } else {
                break;
            }
        }
        FileOutputStream outputStream = null;
        StringBuffer buffer = new StringBuffer(
                FileConstants.getImageDirPath(context));
        FileUtil.mkdirs(buffer.toString());
        buffer.append(MD5Utils.toMD5(DefaultOptions.IMG_TEMP_DEFAULT));
        buffer.append(MD5Utils.toMD5(System.currentTimeMillis()+""));
        try {
            outputStream = new FileOutputStream(buffer.toString());
            baos.writeTo(outputStream);
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compressImage.recycle();
            compressImage = null;
            FileUtil.closeQuietly(outputStream, baos);
        }
        return buffer.toString();
    }



    public static void depositInDisk(Bitmap compressImage,
                                     CameraOptions mBuilder) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 80;
        compressImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 >= 1024) {
            if (quality >= 50) {
                baos.reset();
                quality -= 5;
                compressImage.compress(Bitmap.CompressFormat.JPEG, quality,
                        baos);
            } else {
                break;
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mBuilder.getPhotoUri()
                    .getTempFile());
            baos.writeTo(outputStream);
            if (mBuilder.getmImThumbnailsPhoto().getIsCreateThunmbnail()) {
                getThumbnail(compressImage, mBuilder, outputStream);
            } else {
                mBuilder.delThumbnailFile();
            }
            baos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compressImage.recycle();
            compressImage = null;
            FileUtil.closeQuietly(outputStream, baos);
        }

    }

    private static void getThumbnail(Bitmap compressImage,
                                     CameraOptions mBuilder, FileOutputStream outputStream) {
        Bitmap cutBitmapWidth = null;
        float s = compressImage.getWidth() / (float) compressImage.getHeight();
        ByteArrayOutputStream baosThum = new ByteArrayOutputStream();
        compressImage.compress(Bitmap.CompressFormat.JPEG, 60, baosThum);
        try {
            outputStream = new FileOutputStream(mBuilder
                    .getmImThumbnailsPhoto().getTempFileThumbnail());
            baosThum.writeTo(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeQuietly(baosThum);
        }
    }


    public static Bitmap getThenumBitmap(String file,int width,
                                         int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file,
                options);
        // Calculate inSampleSize
        options.inPreferredConfig = Config.ARGB_8888;
        float s = options.outWidth / (float) (options.outHeight);
        int calculateInSampleSize = 1;
        //太长太宽图片不做处理
        if (s > 10.5f ) {
        } else if (s < 0.15f) {
        } else {
            calculateInSampleSize = calculateInSampleSize(options, width,
                    height);
        }

        options.inSampleSize = calculateInSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bm = getLocalBitmap(file, options);
        return bm;
    }

}
