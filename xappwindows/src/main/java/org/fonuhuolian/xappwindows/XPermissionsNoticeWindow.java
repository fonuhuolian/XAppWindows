package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.fonuhuolian.xappwindows.adapter.XPermissionNoticeAdapter;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;
import org.fonuhuolian.xappwindows.utils.XPermissionCheckUtil;

import java.util.List;

/**
 * TODO 权限弹出框
 */
public class XPermissionsNoticeWindow {

    private XPopupWindow popWindow;

    // 需要检查的所有危险权限（定位，存储，电话权限）
    private String[] ALL_PERMISSION;
    // 请求码
    private static final int REQUEST_CODE_PERMISSION = 520;
    // 权限检测器
    private XPermissionCheckUtil mChecker;
    // 依附的Activity
    private Activity mActivity;
    // 监听
    private Listener mListener;

    /**
     * 初始化权限检查
     *
     * @param context  上下文对象
     * @param dataList List<包含 icon,权限名称,权限描述,权限的id>
     *                 eg. R.drawable.eg_storage_permission, "存储权限", "启权限后，可以使用图片下载、文件上传等功能", Manifest.permission.WRITE_EXTERNAL_STORAGE
     * @param listener 监听
     */
    public XPermissionsNoticeWindow(final Activity context, List<XPermissionNoticeBean> dataList, @NonNull Listener listener) {

        this.mActivity = context;
        mListener = listener;

        View contentView = LayoutInflater.from(context).inflate(R.layout.x_permisions_notice_window, null);
        // 必须三元素
        popWindow = new XPopupWindow(context, contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        // 点击空白区域消失
        popWindow.setFocusable(false);
        // 加入动画
        popWindow.setAnimationStyle(R.style.global_pop_animation);

        //处理popWindow 显示内容
        TextView agree = (TextView) contentView.findViewById(R.id.x_permission_agree);
        TextView reason = (TextView) contentView.findViewById(R.id.x_reason);
        RecyclerView reasonList = (RecyclerView) contentView.findViewById(R.id.x_reason_list);

        ALL_PERMISSION = new String[dataList.size()];

        for (int i = 0; i < dataList.size(); i++) {
            ALL_PERMISSION[i] = dataList.get(i).getManifestPermission();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            reason.setText(context.getResources().getString(R.string.permissions_reason_msg_part1) + context.getResources().getString(labelRes) + context.getResources().getString(R.string.permissions_reason_msg_part2));
        } catch (Exception e) {
            e.printStackTrace();
        }


        reasonList.setLayoutManager(new LinearLayoutManager(context));
        reasonList.setAdapter(new XPermissionNoticeAdapter(reasonList, dataList));

        mChecker = new XPermissionCheckUtil(context);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences p = mActivity.getSharedPreferences("XPermissionsNoticeWindow", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = p.edit();
                editor.putBoolean("isFirst", false);
                editor.apply();

                if (mChecker.isNeedRequestPermissions(ALL_PERMISSION)) {
                    // 请求权限
                    requestPermissions(ALL_PERMISSION);
                } else {
                    // 全部权限都已获取
                    mListener.onGranted();
                }

                popWindow.dismiss();
            }
        });
    }

    // TODO 在想要使用的地方调用此方法进行权限检查
    public XPermissionsNoticeWindow start() {

        SharedPreferences p = mActivity.getSharedPreferences("XPermissionsNoticeWindow", Context.MODE_PRIVATE);
        boolean isFirst = p.getBoolean("isFirst", true);

        if (isFirst) {

            if (!mChecker.isNeedRequestPermissions(ALL_PERMISSION)) {
                mListener.onGranted();
            } else {
                show();
            }

        } else {
            // 展示过弹窗，默认放行
            mListener.onGranted();
        }

        return this;
    }


    public void onDestroy() {
        popWindow.dismiss();
    }

    public boolean isShowing() {
        return popWindow != null && popWindow.isShowing();
    }

    /**
     * 同意不同意都放行，根据国家政策
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mListener.onGranted();
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(mActivity, permissions, REQUEST_CODE_PERMISSION);
    }

    private void show() {

        if (popWindow != null) {

            try {
                final View decorView = mActivity.getWindow().getDecorView();
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        popWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
                    }
                });

            } catch (Exception e) {
                if (!mChecker.isNeedRequestPermissions(ALL_PERMISSION)) {
                    mListener.onGranted();
                } else {
                    // 请求权限
                    requestPermissions(ALL_PERMISSION);
                }
            }
        }
    }


    public interface Listener {
        void onGranted();
    }
}
