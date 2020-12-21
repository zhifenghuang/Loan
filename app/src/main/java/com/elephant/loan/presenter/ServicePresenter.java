package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MyLoanContract;
import com.elephant.loan.contract.ServiceContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ServicePresenter extends BasePresenter<ServiceContract.View> implements ServiceContract.Presenter {

    public ServicePresenter(@NotNull ServiceContract.View rootView) {
        super(rootView);
    }


    @Override
    public void getServiceUrl() {
        HttpMethods.Companion.getInstance().getServiceUrl(
                new HttpObserver(true, getRootView(), new HttpListener<String>() {
                    @Override
                    public void onSuccess(@Nullable String bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getServiceSuccess(bean);
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
