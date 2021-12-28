package org.fonuhuolian.xappwindows;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.fonuhuolian.xappwindows.utils.ClickSpanUtil;
import org.fonuhuolian.xappwindows.utils.TextMovementMethodUtil;

/**
 * TODO 协议弹出框（支持查看协议 与 拒绝提醒）
 */
public class XAgreementWindow {

    private XPopupWindow popWindow;

    // 依附的Activity
    private Activity mActivity;
    // 监听
    private Listener mListener;

    private Handler handler = new Handler(Looper.getMainLooper());

    // 预览协议
    private XAgreementHtmlWindow htmlWindow;
    // 退出提醒
    private XAgreementExitAppWindow exitAppWindow;

    /**
     * @param context       依附的Activity
     * @param startMsg      协议理由开始片段
     * @param textSize      浏览器文字大小，可传null
     * @param agreement1    例如：《隐私政策》
     * @param agreement2    例如：《用户协议》
     * @param endMsg        协议理由后部分片段
     * @param agreementUrl1 对应agreement1的url
     * @param agreementUrl2 对应agreement2的url
     * @param listener      监听
     */
    public XAgreementWindow(final Activity context, String startMsg, WebSettings.TextSize textSize, String agreement1, String agreement2, String endMsg, String agreementUrl1, String agreementUrl2, @NonNull Listener listener) {

        this.mActivity = context;
        mListener = listener;

        // 优先生成协议预览(防止拒点击协议链接时未初始化完毕)
        htmlWindow = new XAgreementHtmlWindow(mActivity, textSize,
                new XAgreementHtmlWindow.Listener() {
                    @Override
                    public void onKnown() {
                        show();
                    }
                });

        // 优先生成退出提醒(防止拒绝时弹出时未初始化完毕)
        exitAppWindow = new XAgreementExitAppWindow(context, new XAgreementExitAppWindow.Listener() {
            @Override
            public void onKnown() {
                show();
            }
        });

        View contentView = LayoutInflater.from(context).inflate(R.layout.x_agreement_window, null);
        // 必须三元素
        popWindow = new XPopupWindow(context, contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        // 点击空白区域消失
        popWindow.setFocusable(false);
        // 加入动画
        popWindow.setAnimationStyle(R.style.global_pop_animation);

        //处理popWindow 显示内容
        TextView agree = (TextView) contentView.findViewById(R.id.x_agreement_agree);
        TextView refuse = (TextView) contentView.findViewById(R.id.x_agreement_refuse);
        TextView reason = (TextView) contentView.findViewById(R.id.x_reason);

        reason.setText(getString(startMsg, agreement1, agreement2, endMsg, agreementUrl1, agreementUrl2));

        //设置选中文本的高亮颜色
        reason.setHighlightColor(mActivity.getResources().getColor(android.R.color.transparent));
        reason.setMovementMethod(new TextMovementMethodUtil());

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences p = mActivity.getSharedPreferences("XAgreementWindow", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = p.edit();
                editor.putBoolean("isAgree", true);
                editor.apply();

                popWindow.dismiss();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 已同意
                        mListener.onAgreed();
                    }
                }, 200);
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popWindow.dismiss();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 不同意
                        exitAppWindow.show();
                    }
                }, 200);
            }
        });
    }

    // TODO 在想要使用的地方调用此方法进行协议检查
    public XAgreementWindow start() {

        SharedPreferences p = mActivity.getSharedPreferences("XAgreementWindow", Context.MODE_PRIVATE);
        boolean isAgree = p.getBoolean("isAgree", false);

        if (isAgree) {
            // 已同意
            mListener.onAgreed();
        } else {
            show();
        }

        return this;
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
                // 防止crash后永远不能进入
                mListener.onAgreed();
            }
        }
    }

    public void onDestroy() {
        popWindow.dismiss();

        if (htmlWindow != null)
            htmlWindow.onDestroy();

        if (exitAppWindow != null)
            exitAppWindow.onDestroy();
    }

    public boolean isShowing() {

        boolean agreementWindowShowing = popWindow != null && popWindow.isShowing();
        boolean agreementDetailWindowShowing = htmlWindow != null && htmlWindow.isShowing();
        boolean agreementExitWindowShowing = exitAppWindow != null && exitAppWindow.isShowing();

        return agreementWindowShowing || agreementDetailWindowShowing || agreementExitWindowShowing;
    }

    // TODO 转换 string
    private SpannableStringBuilder getString(String start, String agreement1, String agreement2, String end, final String agreementUrl1, final String agreementUrl2) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(start);


        int startAgreement1 = builder.length();
        int endAgreement1 = startAgreement1 + agreement1.length();

        builder.append(agreement1);

        builder.setSpan(new ClickSpanUtil(agreementUrl1, new SpanListener() {
            @Override
            public void onClick(String url) {

                popWindow.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (htmlWindow != null)
                            htmlWindow.show(agreementUrl1);
                    }
                }, 200);

            }
        }, Color.parseColor("#008EFF")), startAgreement1, endAgreement1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append("和");

        int startAgreement2 = builder.length();
        int endAgreement2 = startAgreement2 + agreement2.length();

        builder.append(agreement2);

        builder.setSpan(new ClickSpanUtil(agreementUrl2, new SpanListener() {
            @Override
            public void onClick(String url) {

                popWindow.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (htmlWindow != null)
                            htmlWindow.show(agreementUrl2);
                    }
                }, 200);
            }
        }, Color.parseColor("#008EFF")), startAgreement2, endAgreement2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(end);

        return builder;
    }

    public interface SpanListener {
        void onClick(String url);
    }

    public interface Listener {
        void onAgreed();
    }
}
