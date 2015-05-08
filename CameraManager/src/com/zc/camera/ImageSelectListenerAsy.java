package com.zc.camera;

import java.util.List;

import com.zc.photoalbum.ImageItem;

/**
 * get image listern
 * 
 * @author zhaocheng
 * 
 */
public interface ImageSelectListenerAsy {
    /**
     * get image Path Asy
     * 
     * @param imgPath
     */
    public void onSelectedAsy(String imgPath);
    
    public void onSelectedAsy(List<ImageItem> pathList);

}
