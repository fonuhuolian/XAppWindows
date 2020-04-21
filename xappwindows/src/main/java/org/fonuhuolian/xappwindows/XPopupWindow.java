package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class XPopupWindow extends PopupWindow {

    private float alpha = 0;
    private Activity mActivity;

    public XPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        // 去除四周空隙
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public XPopupWindow(Activity activity, View contentView, int width, int height, float alpha) {
        this(contentView, width, height);
        this.alpha = alpha;
        this.mActivity = activity;

    }


    /**
     * showAsDropDown(View anchor)
     * 相对某个控件的位置（正左下方）
     * <p>
     * 当anchor控件为整个屏幕大小  调用此方法
     * 则可能不显示布局
     * 需要调用showAtLocation(View parent, int gravity, int x, int y)
     * 或调用showAsDropDown(View anchor, int xoff, int yoff) y轴设置偏移
     */

    @Override
    public void showAsDropDown(View anchor) {

        if (mActivity == null || mActivity.isFinishing())
            return;

        super.showAsDropDown(anchor);
        setBackgroundAlpha(alpha);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {

        if (mActivity == null || mActivity.isFinishing())
            return;

        super.showAsDropDown(anchor, xoff, yoff);
        setBackgroundAlpha(alpha);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {

        if (mActivity == null || mActivity.isFinishing())
            return;

        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setBackgroundAlpha(alpha);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {

        if (mActivity == null || mActivity.isFinishing())
            return;

        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(alpha);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBackgroundAlpha(1);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {

        if (mActivity == null || mActivity.isFinishing())
            return;

        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(lp);
    }

}
