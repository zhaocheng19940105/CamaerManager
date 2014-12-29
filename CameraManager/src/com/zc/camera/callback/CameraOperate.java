package com.zc.camera.callback;

import android.content.Intent;

/**
 * Created by zl on 14/12/26.
 */
public interface CameraOperate {

    /**
     * open android camera
     * @param intent Standard opera intent
     */
    void onOpenCamera(Intent intent);

    /**
     * According to different using a different version number Action
     * @param intent
     */
    void onOpenGallery(Intent intent);

    /**
     * Because individual android call system will cut out the problem so this
     * is crop is this custom
     * @param intent
     */
    void onOpenCrop(Intent intent);


}
