package org.fonuhuolian.xappwindows.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import org.fonuhuolian.xappwindows.XAgreementWindow;

public class ClickSpanUtil extends ClickableSpan {

    private boolean mPressed;

    private String agreementUrl;
    private XAgreementWindow.SpanListener mListener;
    private int mTextColor;

    public ClickSpanUtil(String agreementUrl, XAgreementWindow.SpanListener listener, int textColor) {
        this.agreementUrl = agreementUrl;
        this.mListener = listener;
        this.mTextColor = textColor;
    }

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.bgColor = mPressed ? Color.parseColor("#F6F6F6") : Color.TRANSPARENT;
        ds.setColor(mTextColor);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null)
            mListener.onClick(agreementUrl);
    }
}

