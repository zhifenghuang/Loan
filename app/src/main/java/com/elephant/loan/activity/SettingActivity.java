package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_setting);
        setViewsOnClickListener(R.id.llModifyLoginPsw, R.id.llAboutUs);
    }

    @NotNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llModifyLoginPsw:
                openActivity(ChangePasswordActivity.class);
                break;
            case R.id.llAboutUs:
                openActivity(AboutUsActivity.class);
                break;
        }
    }
}
