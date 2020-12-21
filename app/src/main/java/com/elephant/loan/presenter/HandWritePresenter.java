package com.elephant.loan.presenter;

import com.common.lib.constant.EventBusEvent;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.HandWriteContract;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;


public class HandWritePresenter extends BasePresenter<HandWriteContract.View> implements HandWriteContract.Presenter {

    public HandWritePresenter(@NotNull HandWriteContract.View rootView) {
        super(rootView);
    }

    @Override
    public void uploadHandWrite(File sign) {
        HttpMethods.Companion.getInstance().writeSign(sign,
                new HttpObserver(true, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(EventBusEvent.REFRESH_INFO, "");
                        EventBus.getDefault().post(map);
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
                        getRootView().showErrorDialog(code, msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }
}
