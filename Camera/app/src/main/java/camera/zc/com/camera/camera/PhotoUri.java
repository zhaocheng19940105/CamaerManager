package camera.zc.com.camera.camera;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

public class PhotoUri implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3981188737857901693L;
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
