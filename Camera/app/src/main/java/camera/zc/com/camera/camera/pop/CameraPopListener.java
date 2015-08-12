package camera.zc.com.camera.camera.pop;

import com.pingan.papd.camera.options.CameraOptions;

/**
 * @author zhaocheng
 * 
 */
public interface CameraPopListener {

    /**
     * click openCamrea
     */
    void onCamreaClick(CameraOptions options);

    /**
     * click openPick
     */
    void onPickClick(CameraOptions options);

    /**
     * del image click
     */
    void onDelClick();

}
