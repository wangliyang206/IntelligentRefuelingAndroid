package com.axzl.mobile.refueling.mvp.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.axzl.mobile.refueling.R;
import com.axzl.mobile.refueling.mvp.model.entity.MessageBean;
import com.jess.arms.base.BaseHolder;

import butterknife.BindView;

public class WebViewHolder extends BaseHolder<MessageBean> {
    @BindView(R.id.mywebview)
    WebView webView;

    public WebViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(@NonNull MessageBean message, int position) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });

        webView.loadUrl(message.getUrl());
    }
}
