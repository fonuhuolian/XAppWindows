package org.fonuhuolian.xappwindows;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.fonuhuolian.xappwindows.utils.XNotifactionUtils;

/**
 * TODO 通知提醒演示(不对外提供)
 */
class XNotifactionShowHowWindow {

    private XPopupWindow popWindow;

    // 依附的Activity
    private Activity mActivity;

    private ImageView bg;
    private ImageView btn;
    private ImageView finger;

    private Handler handler = new Handler(Looper.getMainLooper());

    public XNotifactionShowHowWindow(final Activity context) {

        this.mActivity = context;

        View contentView = LayoutInflater.from(context).inflate(R.layout.x_notifaction_show_how_window, null);
        // 必须三元素
        popWindow = new XPopupWindow(context, contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        // 点击空白区域消失
        popWindow.setFocusable(false);
        // 加入动画
        popWindow.setAnimationStyle(R.style.global_pop_animation);

        //处理popWindow 显示内容
        bg = (ImageView) contentView.findViewById(R.id.switch_bg);
        btn = (ImageView) contentView.findViewById(R.id.switch_btn);
        finger = (ImageView) contentView.findViewById(R.id.x_finger);

        TextView appName = (TextView) contentView.findViewById(R.id.appName);
        TextView appMsg = (TextView) contentView.findViewById(R.id.appMsg);
        TextView know = (TextView) contentView.findViewById(R.id.x_know);

        String appNameStr = "";

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appNameStr = context.getResources().getString(labelRes);
        } catch (Exception e) {
            appNameStr = "";
        }

        appName.setText(appNameStr);

        appMsg.setText(context.getResources().getString(R.string.notifaction_part1) + appNameStr + context.getResources().getString(R.string.notifaction_part2));


        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XNotifactionUtils.setVersionNeverNotify(mActivity);
                        XNotifactionUtils.jumpToAppDetailsSettings(context);
                    }
                }, 200);
            }
        });
    }


    public void show() {

        if (popWindow != null) {

            try {
                final View decorView = mActivity.getWindow().getDecorView();
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        popWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bg.setImageResource(R.drawable.x_switch_open);
                                int right = bg.getWidth() - btn.getWidth();
                                ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "translationX",
                                        0, right);
                                animator.setDuration(1000);
                                animator.start();

                                ObjectAnimator animator2 = ObjectAnimator.ofFloat(finger, "translationX",
                                        0, right);
                                animator2.setDuration(1000);
                                animator2.start();
                            }
                        }, 1000);
                    }
                });

            } catch (Exception e) {
                // 防止crash后永远不能进入
                XNotifactionUtils.setVersionNeverNotify(mActivity);
                XNotifactionUtils.jumpToAppDetailsSettings(mActivity);
            }
        }
    }

    public void onDestroy() {
        popWindow.dismiss();
    }

    public boolean isShowing() {
        return popWindow != null && popWindow.isShowing();
    }

}
