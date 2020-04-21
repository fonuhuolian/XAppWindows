package org.fonuhuolian.appwindows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import org.fonuhuolian.xappwindows.XPermissionsNoticeWindow;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private XPermissionsNoticeWindow xPermissionsNoticeWindow;
    View frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<XPermissionNoticeBean> l = new ArrayList<>();
        l.add(new XPermissionNoticeBean(R.drawable.eg_storage_permission,"存储权限","启权限后，可以使用图片下载、可以使用图片下载、文件上传等功能"));
        l.add(new XPermissionNoticeBean(R.drawable.eg_storage_permission,"存储权限2","启权限后，可以使用图片下载、可以使用图片下载、文件上传等功能2"));

        frame = findViewById(R.id.frame);

        xPermissionsNoticeWindow = new XPermissionsNoticeWindow(MainActivity.this, l);

    }

    public void click(View view) {
        xPermissionsNoticeWindow.show(frame, Gravity.BOTTOM);
    }
}
