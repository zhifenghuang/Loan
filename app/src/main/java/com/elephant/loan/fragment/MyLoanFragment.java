package com.elephant.loan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.fragment.BaseFragment;
import com.elephant.loan.R;
import com.elephant.loan.contract.MyLoanContract;
import com.elephant.loan.presenter.MyLoanPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyLoanFragment extends BaseFragment<MyLoanContract.Presenter> implements MyLoanContract.View {

    @NotNull
    @Override
    protected MyLoanContract.Presenter onCreatePresenter() {
        return new MyLoanPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_loan;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void updateUIText() {
        getPresenter().banner();
    }

    @Override
    public void getBannerSuccess(String banner) {
        if (getView() == null || TextUtils.isEmpty(banner)) {
            return;
        }
        setText(R.id.tvBanner, banner);
    }
}
