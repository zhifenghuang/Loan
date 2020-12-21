package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.PhoneAuthContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PhoneAuthPresenter extends BasePresenter<PhoneAuthContract.View> implements PhoneAuthContract.Presenter {

    public PhoneAuthPresenter(@NotNull PhoneAuthContract.View rootView) {
        super(rootView);
    }

    @Override
    public void phoneAuth(String phone) {
        HttpMethods.Companion.getInstance().phoneVerify(phone,
                new HttpObserver(true, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().phoneAuthSuccess(msg);
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
