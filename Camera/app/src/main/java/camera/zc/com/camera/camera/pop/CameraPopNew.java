package camera.zc.com.camera.camera.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pajk.hm.sdk.android.R;
import com.pingan.papd.camera.CameraHandler;
import com.pingan.papd.camera.CropBuilder;
import com.pingan.papd.camera.SDCardUtil;
import com.pingan.papd.camera.callback.CameraImageListener;

/**
 * Custom PopWindow
 * 
 * @author zhaocheng
 * 
 */
public class CameraPopNew extends PopupWindow implements OnClickListener,
        OnTouchListener {
    private LinearLayout ll_show_del;
    private TextView btn_take_photo, btn_pick_photo, btn_cancel, btn_del_photo,
            btn_reload_photo;
    private View mMenuView;
    private Context mContext;
    private Fragment mFragment;
    private CameraPopListener mPopListern;
    private CameraHandler mCameraHandler;
    private int clickId;
    private View clickView;

    public CameraPopNew(Context context, CameraPopListener listener,
            CameraImageListener imageListern) {
        super(context);
        this.mContext = context;
        this.mPopListern = listener;
        init();
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(popItemClick);
        btn_take_photo.setOnClickListener(popItemClick);
        btn_del_photo.setOnClickListener(popItemClick);
        mMenuView.setOnTouchListener(this);
        mCameraHandler = new CameraHandler(context, imageListern);
    }

    public View getClickView() {
        return clickView;
    }

    public void setClickView(View clickView) {
        this.clickView = clickView;
    }

    public CameraPopNew(Fragment fragment, CameraPopListener listener,
            CameraImageListener imageListern) {
        super(null == fragment ? null : fragment.getActivity());
        this.mContext = null == fragment ? null : fragment.getActivity();
        mFragment = fragment;
        this.mPopListern = listener;
        init();
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(popItemClick);
        btn_take_photo.setOnClickListener(popItemClick);
        btn_del_photo.setOnClickListener(popItemClick);
        mMenuView.setOnTouchListener(this);
        mCameraHandler = new CameraHandler(fragment, imageListern);
    }

    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
    }

    public void setDel(boolean isDel) {
        if (isDel)
            btn_del_photo.setVisibility(View.VISIBLE);
        else
            btn_del_photo.setVisibility(View.GONE);
    }

    public void showOnlyDelView(View view) {
        if (!isShowing()) {
            if (SDCardUtil.checkSDCard()) {
                setDel(true);
                btn_reload_photo.setVisibility(View.GONE);
                btn_take_photo.setVisibility(View.GONE);
                btn_pick_photo.setVisibility(View.GONE);
                buildMenu(view);
            } else {
            }
        }
    }

    public void showReloadView(View view,OnClickListener listener) {
        if (!isShowing()) {
            if (SDCardUtil.checkSDCard()) {
                setDel(true);
                btn_take_photo.setVisibility(View.GONE);
                btn_pick_photo.setVisibility(View.GONE);
                btn_reload_photo.setVisibility(View.VISIBLE);
                btn_reload_photo.setOnClickListener(listener);
                buildMenu(view);
            } else {
            }
        }
    }

    OnClickListener popItemClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_take_photo) {
                mPopListern.onCamreaClick(mCameraHandler.getOptions());
            } else if (id == R.id.btn_pick_photo) {
                mPopListern.onPickClick(mCameraHandler.getOptions());
            } else if (id == R.id.btn_del_photo) {
                mPopListern.onDelClick();
            }
            dismiss();
        }

    };

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog_new, null);
        btn_del_photo = (TextView) mMenuView.findViewById(R.id.btn_del_photo);
        btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
        btn_reload_photo = (TextView) mMenuView
                .findViewById(R.id.btn_reload_photo);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(
                R.color.transparent_50));
        this.setBackgroundDrawable(dw);
    }

    public void showMenuDefault(View view, int clickId) {
        this.clickId = clickId;
        showMenuDefault(view);
    }

    /**
     * 弹出选择菜单
     */
    public void showMenuDefault(View view) {
        if (!isShowing()) {
            if (SDCardUtil.checkSDCard()) {
                setDel(false);
                btn_reload_photo.setVisibility(View.GONE);
                btn_take_photo.setVisibility(View.VISIBLE);
                btn_pick_photo.setVisibility(View.VISIBLE);
                buildMenu(view);
            }
        }
    }

    private void buildMenu(View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void setCropBuilder(CropBuilder builder) {
        mCameraHandler.getOptions().setCropBuilder(builder);
    }

    public void process() {
        try {
            mCameraHandler.process();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void forResult(int requestCode, int resultCode, Intent data) {
        mCameraHandler.forResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int height = mMenuView.findViewById(R.id.pop_layout).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return true;

    }
}
