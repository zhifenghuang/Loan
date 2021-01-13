package com.elephant.loan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.fragment.BaseFragment;
import com.elephant.loan.R;
import com.elephant.loan.contract.ServiceContract;
import com.elephant.loan.presenter.ServicePresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceFragment extends BaseFragment<ServiceContract.Presenter> implements ServiceContract.View {

    @NotNull
    @Override
    protected ServiceContract.Presenter onCreatePresenter() {
        return new ServicePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(view);
        setViewsOnClickListener(R.id.tvContact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvContact:
                getPresenter().getServiceUrl();
                break;
        }
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    public void getServiceSuccess(String serviceUrl) {
        if (getView() == null || TextUtils.isEmpty(serviceUrl)) {
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(serviceUrl);
            intent.setData(content_url);
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}
