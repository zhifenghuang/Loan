package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.DataInfoContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class DataInfoPresenter extends BasePresenter<DataInfoContract.View> implements DataInfoContract.Presenter {

    public DataInfoPresenter(@NotNull DataInfoContract.View rootView) {
        super(rootView);
    }


    @Override
    public void uploadDataInfo(String education, String income, String purpose, int house, int car) {
        HttpMethods.Companion.getInstance().uploadDataInfo(education, income, purpose, house, car,
                new HttpObserver(true, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().uploadSuccess(msg);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().uploadFailed(msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }
}
