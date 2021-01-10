package com.elephant.loan.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SplashActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {

        Configuration configuration = getResources().getConfiguration();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (Constants.APP_VERSION == 0) {
            configuration.locale = new Locale("th", "TH");
        } else {
            configuration.locale = new Locale("vi", "VN");
        }
        getResources().updateConfiguration(configuration, displayMetrics);

        if (Constants.APP_VERSION == 1) {
            setImage(R.id.iv, R.drawable.qidongy);
        }

        findViewById(R.id.iv).postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(TextUtils.isEmpty(DataManager.Companion.getInstance().getToken()) ?
                        LoginActivity.class : MainActivity.class);
                finish();
            }
        }, 1500);

    }

    @NotNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }
}
