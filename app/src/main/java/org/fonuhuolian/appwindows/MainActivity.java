package org.fonuhuolian.appwindows;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.fonuhuolian.xappwindows.XAgreementWindow;
import org.fonuhuolian.xappwindows.XPermissionsNoticeWindow;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private XAgreementWindow xAgreementWindow;
    private XPermissionsNoticeWindow xPermissionsNoticeWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ① 权限申请 防止onAgreed的时候xPermissionsNoticeWindow还没初始化完毕
        final List<XPermissionNoticeBean> l = new ArrayList<>();
        l.add(new XPermissionNoticeBean(R.drawable.eg_storage_permission, "存储权限", "启权限后，可以使用图片下载、文件上传等功能", Manifest.permission.WRITE_EXTERNAL_STORAGE));

        xPermissionsNoticeWindow = new XPermissionsNoticeWindow(MainActivity.this, l, new XPermissionsNoticeWindow.Listener() {
            @Override
            public void onGranted() {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                finish();
            }
        });

        // ② 协议检查
        xAgreementWindow = new XAgreementWindow(MainActivity.this, getString(R.string.start), getString(R.string.agreement1), getString(R.string.agreement2), getString(R.string.end), "https://github.com/fonuhuolian/XAppWindows", "https://github.com/fonuhuolian/XAppWindows", new XAgreementWindow.Listener() {

            @Override
            public void onAgreed() {
                // 协议检查通过后启动权限检查
                xPermissionsNoticeWindow.start();
            }
        });

        // ③都初始化完毕 调用协议检查
        xAgreementWindow.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        xPermissionsNoticeWindow.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        xPermissionsNoticeWindow.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xAgreementWindow.onDestroy();
        xPermissionsNoticeWindow.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (!xPermissionsNoticeWindow.isShowing() && !xAgreementWindow.isShowing()) {
            finish();
        }
    }
}
