package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MyLoanContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MyLoanPresenter extends BasePresenter<MyLoanContract.View> implements MyLoanContract.Presenter {

    public MyLoanPresenter(@NotNull MyLoanContract.View rootView) {
        super(rootView);
    }


    @Override
    public void banner() {
        HttpMethods.Companion.getInstance().banner(
                new HttpObserver(false, getRootView(), new HttpListener<String>() {
                    @Override
                    public void onSuccess(@Nullable String bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getBannerSuccess(bean);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {

                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }
}
