package com.zc.camera;

import java.io.File;
import java.io.Serializable;

import android.net.Uri;

public class PhotoUri implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 7899267893318950854L;
    private File tempFile = null;
    private Uri fileUri = null;

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public PhotoUri(File tempFile, Uri fileUri) {
        super();
        this.tempFile = tempFile;
        this.fileUri = fileUri;
    }

}
