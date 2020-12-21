package com.common.lib.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.lib.R;
import com.common.lib.constant.Constants;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WebsiteActivity extends BaseActivity<EmptyPresenter> implements EmptyContract.View {

    private Boolean isDestroy = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        isDestroy = false;
        setTopStatusBarStyle(findViewById(R.id.ll_parent));
        String loadUrl = getIntent().getExtras().getString(Constants.BUNDLE_EXTRA);
        final WebView webView = findViewById(R.id.webView);
        webView.setBackgroundColor(0);
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(false);          //支持缩放
        settings.setBuiltInZoomControls(false);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        webView.loadUrl(loadUrl);
        final ProgressBar loadingBar = findViewById(R.id.loadingBar);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isDestroy) {
                    loadingBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isDestroy) {
                    loadingBar.setMax(100);
                    loadingBar.setProgress(0);
                    loadingBar.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!isDestroy) {
                    loadingBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!isDestroy) {
                    ((TextView) findViewById(R.id.tv_title)).setText(title);
                }
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    @NotNull
    @Override
    protected EmptyPresenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    protected void updateUIText() {

    }
}
