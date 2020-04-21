package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.fonuhuolian.xappwindows.adapter.XPermissionNoticeAdapter;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;

import java.util.List;


public class XPermissionsNoticeWindow {

    private XPopupWindow popWindow;

    public XPermissionsNoticeWindow(final Activity context, List<XPermissionNoticeBean> dataList) {

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

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }


    private void close() {
        if (popWindow != null) {
            popWindow.dismiss();
        }
    }


    public void show(View parent, int gravity) {

        if (popWindow != null)
            popWindow.showAtLocation(parent, gravity, 0, 0);
    }


    public interface YinSiListener {

        void onRefuse();

        void onAgree();
    }
}
