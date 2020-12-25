package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.BuildConfig;
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
        setViewsOnClickListener(R.id.llModifyLoginPsw, R.id.llAboutUs, R.id.tvLogout);
        setText(R.id.tvVersionName, "V" + BuildConfig.VERSION_NAME);
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
            case R.id.tvLogout:
                showTwoBtnDialog(getString(R.string.app_logout),
                        getString(R.string.app_sure_logout),
                        getString(R.string.app_cancel),
                        getString(R.string.app_ok),
                        new OnClickCallback() {
                            @Override
                            public void onClick(int viewId) {
                                if (viewId == R.id.btn2) {
                                    logout();
                                }
                            }
                        });
                break;
        }
    }
}
