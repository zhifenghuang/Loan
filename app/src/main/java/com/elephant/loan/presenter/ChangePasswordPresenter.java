package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.ChangePasswordContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ChangePasswordPresenter extends BasePresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {

    public ChangePasswordPresenter(@NotNull ChangePasswordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void changePassword(String password, String new1, String new2) {
        HttpMethods.Companion.getInstance().changePsw(password, new1, new2,
                new HttpObserver(true, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().changePswSuccess(msg);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().showErrorDialog(code, msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }
}
