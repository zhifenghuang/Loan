package com.elephant.loan.fragment;

import android.os.Bundle;
import android.view.View;

import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WalletFragment extends BaseFragment<EmptyContract.Presenter> implements EmptyContract.View {

    @NotNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void updateUIText() {

    }
}
