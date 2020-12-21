package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
