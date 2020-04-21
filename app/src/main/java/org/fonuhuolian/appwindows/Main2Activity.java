package org.fonuhuolian.appwindows;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.fonuhuolian.xappwindows.XPermissionsNoticeWindow;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private XPermissionsNoticeWindow xPermissionsNoticeWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final List<XPermissionNoticeBean> l = new ArrayList<>();
        l.add(new XPermissionNoticeBean(R.drawable.eg_storage_permission, "存储权限", "启权限后，可以使用图片下载、可以使用图片下载、文件上传等功能", Manifest.permission.WRITE_EXTERNAL_STORAGE));
        l.add(new XPermissionNoticeBean(R.drawable.eg_storage_permission, "存储权限2", "启权限后，可以使用图片下载、可以使用图片下载、文件上传等功能2", Manifest.permission.CAMERA));


        xPermissionsNoticeWindow = new XPermissionsNoticeWindow(Main2Activity.this, l, new XPermissionsNoticeWindow.Listener() {
            @Override
            public void onGranted() {
                Log.e("Ddd", "授权");
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        xPermissionsNoticeWindow.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("Ddd", "onRequestPermissionsResult");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Ddd", "onResume");
        xPermissionsNoticeWindow.onResume();
    }
}
