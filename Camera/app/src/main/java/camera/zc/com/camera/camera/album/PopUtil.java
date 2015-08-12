package camera.zc.com.camera.camera.album;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pajk.hm.sdk.android.R;


public class PopUtil {
    public static PopupWindow CreatePop(View view, int width, int height,
            boolean foucs) {
        if (view != null) {
            final PopupWindow mPopupWindow = new PopupWindow(view, width,
                    height);
            mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
            if (foucs) {
                mPopupWindow.setFocusable(true);
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow
                        .setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                mPopupWindow
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                view.setFocusable(true);
                view.setFocusableInTouchMode(true);

                view.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (mPopupWindow.isShowing()) {
                                mPopupWindow.dismiss();
                            }
                        }
                        return false;
                    }
                });
            }

            return mPopupWindow;
        }

        return null;
    }
}
