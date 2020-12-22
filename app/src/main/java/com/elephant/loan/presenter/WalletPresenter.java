package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.elephant.loan.contract.WalletContract;

import org.jetbrains.annotations.NotNull;


public class WalletPresenter extends BasePresenter<WalletContract.View> implements WalletContract.Presenter {

    public WalletPresenter(@NotNull WalletContract.View rootView) {
        super(rootView);
    }
}
