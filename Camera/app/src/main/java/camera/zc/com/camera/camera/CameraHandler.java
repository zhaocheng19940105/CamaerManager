package camera.zc.com.camera.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.pingan.papd.camera.callback.CameraImageListener;
import com.pingan.papd.camera.callback.CameraOperate;
import com.pingan.papd.camera.callback.SelectMoreListener;
import com.pingan.papd.camera.options.CameraOptions;

/**
 * To deal with jump camera photo album Package manager
 *
 * @author zhaocheng
 */
public class CameraHandler implements CameraOperate {

    private Context mContext;
    private Fragment mFragment;
    private CameraManager mCameraManager;

    public CameraHandler(Context mContext, CameraImageListener cameraImageListener) {
        super();
        this.mContext = mContext;
        this.mCameraManager = new CameraManager(mContext, this, cameraImageListener, null);
    }

    public CameraHandler(Fragment fragment, CameraImageListener cameraImageListener) {
        super();
        mFragment = fragment;
        mContext = null != fragment ? fragment.getActivity() : null;
        mCameraManager = new CameraManager(mContext, this, cameraImageListener, null);
    }

    public CameraHandler(Context mContext, SelectMoreListener moreListener) {
        super();
        this.mContext = mContext;
        this.mCameraManager = new CameraManager(mContext, this, null, moreListener);
    }

    public CameraHandler(Fragment fragment, SelectMoreListener moreListener) {
        super();
        mFragment = fragment;
        mContext = null != fragment ? fragment.getActivity() : null;
        mCameraManager = new CameraManager(mContext, this, null, moreListener);
    }


    public void setOptions(CameraOptions options) {
        this.mCameraManager.setOptions(options);
    }

    public CameraOptions getOptions() {
        return mCameraManager.getOptions();
    }

    @Override
    public void onOpenCamera(Intent intent) {
        try {
            if (null != mFragment) {
                mFragment.startActivityForResult(intent, CameraManager.OPEN_CAMERA_CODE);
                return;
            }
            if (mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(intent,
                        CameraManager.OPEN_CAMERA_CODE);
            } else {
                throw new IllegalArgumentException(" Context need activity ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onOpenGallery(Intent intent) {
        if (null != mFragment) {
            mFragment.startActivityForResult(intent, CameraManager.OPEN_GALLERY_CODE);
            return;
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    CameraManager.OPEN_GALLERY_CODE);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }
    }

    @Override
    public void onOpenCrop(Intent intent) {
        if (null != mFragment) {
            mFragment.startActivityForResult(intent, CameraManager.CROP_PHOTO_CODE);
            return;
        }
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    CameraManager.CROP_PHOTO_CODE);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }
    }

    @Override
    public void onOpenUserAlbum(Intent intent) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent,
                    CameraManager.OPEN_USER_ALBUM);
        } else {
            throw new IllegalArgumentException(" Context need activity ");
        }
    }

    /**
     * 执行照片处理
     *
     * @throws ClassNotFoundException
     */
    public void process() throws ClassNotFoundException {
        mCameraManager.process();
    }

    public void forResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            mCameraManager.getOptions().delErrorUri();
            return;
        }
        mCameraManager.forResult(requestCode, data);
    }

}
