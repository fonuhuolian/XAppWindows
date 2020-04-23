# XAppWindows

> 添加依赖

`root build.gradle `
```
allprojects {
    repositories {
        ...
        maven {
            url 'https://jitpack.io'
        }
    }
}
```
`module build.gradle `
```
implementation ('com.github.fonuhuolian:XAppWindows:0.0.9'){
        // 本项目使用了 recyclerview 为防止包错误，请用自身项目的support包
        exclude group: 'com.android.support', module: 'support-annotations'
}
```

> 混淆
```
-dontwarn org.fonuhuolian.appwindows.**
-keep class org.fonuhuolian.appwindows.**{*;}
```

> Windows

`XAgreementWindow`协议弹出框（支持查看协议，不同意协议时的提醒）
```
/**
  * 初始协议化弹出框
  * @param context       依附的Activity
  * @param startMsg      协议理由开始片段
  * @param agreement1    例如：《隐私政策》
  * @param agreement2    例如：《用户协议》
  * @param endMsg        协议理由后部分片段
  * @param agreementUrl1 对应agreement1的url
  * @param agreementUrl2 对应agreement2的url
  * @param listener      监听
*/
public XAgreementWindow(final Activity context, String startMsg, String agreement1, String agreement2, String endMsg, String agreementUrl1, String agreementUrl2, @NonNull Listener listener) ；

```
```
// 启动协议检查
agreementWindow.start();
```
```
// 在Activity的onDestroy方法里调用，防止内存泄漏
agreementWindow.onDestroy();
```
```
// 判断是否弹出弹出框
agreementWindow.isShowing();
```
`XPermissionsNoticeWindow`权限检查弹出框（支持自动申请权限）
```
/**
   * 初始化权限检查
   * @param context   上下文对象
   * @param dataList  List<包含 icon,权限名称,权限描述,权限的id>
   * @param XPermissionNoticeBean   eg. R.drawable.eg_storage_permission, "存储权限", "启权限后，可以使用图片下载、文件上传等功能", Manifest.permission.WRITE_EXTERNAL_STORAGE
   * @param listener  监听
*/
public XPermissionsNoticeWindow(final Activity context, List<XPermissionNoticeBean> dataList, @NonNull Listener listener);
```
```
// 启动权限检查
permissionsWindow.start();
// 在Activity的onResume方法里调用(必须)
permissionsWindow.onResume();
// 在Activity的onRequestPermissionsResult方法里调用(必须)
permissionsWindow.onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
```
```
// 在Activity的onDestroy方法里调用，防止内存泄漏
permissionsWindow.onDestroy();
```
```
// 判断是否弹出弹出框
permissionsWindow.isShowing();
```
> 联合使用(协议检查+权限检查)【一般用于入口Activity使用】
```
public class MainActivity extends AppCompatActivity {

    private XAgreementWindow xAgreementWindow;
    private XPermissionsNoticeWindow xPermissionsNoticeWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

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
        xAgreementWindow = new XAgreementWindow(MainActivity.this, getString(R.string.start), getString(R.string.agreement1), getString(R.string.agreement2), getString(R.string.end), "https://ourpyw.com/privacy/privacyStatement.html", "https://ourpyw.com/privacy/privacyStatement.html", new XAgreementWindow.Listener() {

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

```
`XNotifactionWindow`通知提醒框
```
/**
  * 初始化通知提醒框
  * @param context        依附的Activity
  * @param reasonMsg      提醒的原因
  * @param isNeedShowHow  是否展示开启示例
*/
public XNotifactionWindow(final Activity context, @NonNull String reasonMsg, final boolean isNeedShowHow);
```
```
// 启动协议检查
notifactionWindow.start();
```
```
// 在Activity的onDestroy方法里调用，防止内存泄漏
notifaction.onDestroy();
```
```
// 判断是否弹出弹出框
notifaction.isShowing();
```