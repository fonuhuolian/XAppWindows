package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.fonuhuolian.xappwindows.view.CircleWebview;

/**
 * TODO 隐私政策详情的弹出框(不对外提供)
 */
class XAgreementHtmlWindow {

    private XPopupWindow popWindow;

    // 依附的Activity
    private Activity mActivity;
    // 监听
    private Listener mListener;

    private Handler handler = new Handler(Looper.getMainLooper());

    private CircleWebview webView;

    public XAgreementHtmlWindow(final Activity context, WebSettings.TextSize textSize, @NonNull Listener listener) {

        this.mActivity = context;
        mListener = listener;

        View contentView = LayoutInflater.from(context).inflate(R.layout.x_agreement_detail_window, null);
        // 必须三元素
        popWindow = new XPopupWindow(context, contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        // 点击空白区域消失
        popWindow.setFocusable(false);
        // 加入动画
        popWindow.setAnimationStyle(R.style.global_pop_animation);

        //处理popWindow 显示内容
        webView = contentView.findViewById(R.id.x_webView);
        final float scale = context.getResources().getDisplayMetrics().density;
        float v = 24 * scale + 0.5f;
        webView.setRadius((int) v);
        TextView agree = (TextView) contentView.findViewById(R.id.x_detail_agree);
        final ProgressBar pb = (ProgressBar) contentView.findViewById(R.id.x_pb);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextSize(textSize == null ? WebSettings.TextSize.NORMAL : textSize);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                webView.scrollTo(0, 0);
                pb.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
                webView.setVisibility(newProgress == 100 ? View.VISIBLE : View.INVISIBLE);

            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onKnown();
                    }
                }, 200);
            }
        });

    }

    public void show(String url) {

        if (popWindow != null) {

            try {
                webView.clearHistory();
                webView.loadUrl(url);
                final View decorView = mActivity.getWindow().getDecorView();
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        popWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
                    }
                });

            } catch (Exception e) {
                // 防止crash后永远不能进入
                mListener.onKnown();
            }
        }
    }

    public void onDestroy() {
        popWindow.dismiss();
    }

    public boolean isShowing() {
        return popWindow != null && popWindow.isShowing();
    }

    public void goBack() {
        if (webView != null && webView.canGoBack())
            webView.goBack();
    }

    public interface Listener {
        void onKnown();
    }
}
