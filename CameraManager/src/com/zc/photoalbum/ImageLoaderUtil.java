/**
 * Title:ImageLoader
 * Description:TODO
 * Copyright:Copyright (c) 2014
 * Company:中国平安健康保险有限公司
 * Author:maple
 * Date:2014/8/8
 * Version 1.0
 */
package com.zc.photoalbum;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zc.cameramanager.R;

public class ImageLoaderUtil {

    public static void loadImage(Context c, ImageView iv, String imageUrl,
            int errorBgRes) {
        loadImage(c, iv, imageUrl, errorBgRes, errorBgRes);
    }

    public static void loadImage(Context c, ImageView iv, String imageUrl,
            int errorBgRes, int defaultBgRes) {
        DisplayImageOptions options = getImageOptionsPress(errorBgRes,
                defaultBgRes);
        com.nostra13.universalimageloader.core.ImageLoader loader = getImageLoader(c);
        loader.displayImage(imageUrl, iv, options);
    }

    public static void loadImage(Context c, ImageView iv, String imageUrl,
            DisplayImageOptions option) {
        DisplayImageOptions options = option;
        ImageLoader loader = getImageLoader(c);
        loader.displayImage(imageUrl, iv, options);
    }

    private static ImageLoader getImageLoader(Context c) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(getImgConfig(c));
        }
        return loader;
    }

    /**
     * imageloader load image
     * 
     * @param errorBgRes
     * @param defaultBgRes
     * @return
     */
    private static DisplayImageOptions getImageOptionsPress(int errorBgRes,
            int defaultBgRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(
                        defaultBgRes == 0 ? getImageDefaultBG() : defaultBgRes)
                .showImageForEmptyUri(
                        errorBgRes == 0 ? getImageErrorBG() : errorBgRes)
                .showImageOnFail(
                        errorBgRes == 0 ? getImageErrorBG() : errorBgRes)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .build();
        return options;
    }

    public static ImageLoaderConfiguration getImgConfig(Context context) {
        return new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCache(
                        new LruMemoryCache((int) (Runtime.getRuntime()
                                .maxMemory() / 8)))
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

    }

    public static void loadImageListern(ImageLoadingListener listener,
            String url, DisplayImageOptions options, Context c) {
        ImageLoader loader = getImageLoader(c);
        if (options != null)
            loader.loadImage(url, options, listener);
        else
            loader.loadImage(url, listener);
    }

    public static void loadImageListern(ImageLoadingListener listener,
            String url, Context c) {
        loadImageListern(listener, url, null, c);
    }

    /**
     * load localImage
     * 
     * @param imgView
     * @param path
     */
    public static void loadLoaclImage(final ImageView imgView, final String path) {
        loadImage(imgView.getContext(), imgView, Scheme.FILE.wrap(path), 0);
    }

    public static int getImageDefaultBG() {
        return R.drawable.bg_img;
    }

    public static int getImageErrorBG() {
        return R.drawable.bg_img;
    }

    public static int getImageEmpty() {
        return R.drawable.bg_img;
    }

}
