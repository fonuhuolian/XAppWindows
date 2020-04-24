package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.fonuhuolian.xappwindows.utils.XNotifactionUtils;

/**
 * TODO 通知提醒框
 */
public class XNotifactionWindow {

    private XPopupWindow popWindow;

    // 依附的Activity
    private Activity mActivity;

    private XNotifactionShowHowWindow showHowWindow;

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 初始化通知提醒框
     *
     * @param context       依附的Activity
     * @param reasonMsg     提醒的原因
     * @param isNeedShowHow 是否展示开启示例
     */
    public XNotifactionWindow(final Activity context, @NonNull String reasonMsg, final boolean isNeedShowHow) {

        this.mActivity = context;

        showHowWindow = new XNotifactionShowHowWindow(context);

        View contentView = LayoutInflater.from(context).inflate(R.layout.x_notifaction_window, null);
        // 必须三元素
        popWindow = new XPopupWindow(context, contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        // 点击空白区域消失
        popWindow.setFocusable(false);
        // 加入动画
        popWindow.setAnimationStyle(R.style.global_pop_animation);

        //处理popWindow 显示内容
        TextView cancle = (TextView) contentView.findViewById(R.id.x_notifaction_cancle);
        TextView start = (TextView) contentView.findViewById(R.id.x_notifaction_open);
        TextView reason = (TextView) contentView.findViewById(R.id.x_reason);

        reason.setText(reasonMsg);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNeedShowHow) {
                            showHowWindow.show();
                        } else {
                            XNotifactionUtils.jumpToAppDetailsSettings(context);
                        }
                    }
                }, 200);


            }
        });
    }

    public void start() {

        if (XNotifactionUtils.isNeedShowDialog(mActivity)) {
            show();
        }

    }

    private void show() {

        if (popWindow != null) {

            XNotifactionUtils.setVersionNeverNotify(mActivity);

            try {
                final View decorView = mActivity.getWindow().getDecorView();
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        popWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
                    }
                });

            } catch (Exception e) {
                // 防止crash后永远不能进入
                XNotifactionUtils.jumpToAppDetailsSettings(mActivity);
            }
        }
    }

    public void onDestroy() {

        popWindow.dismiss();

        if (showHowWindow != null)
            showHowWindow.onDestroy();
    }

    public boolean isShowing() {

        boolean notifactionWindow = popWindow != null && popWindow.isShowing();
        boolean notifactionShowHowWindow = showHowWindow != null && showHowWindow.isShowing();

        return notifactionWindow || notifactionShowHowWindow;
    }
}
