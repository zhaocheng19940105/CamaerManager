package com.zc.photoalbum;

import java.io.Serializable;

public class ImageItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
