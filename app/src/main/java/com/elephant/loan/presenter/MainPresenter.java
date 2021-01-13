package com.elephant.loan.presenter;

import com.common.lib.bean.BalanceBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MainContract;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(@NotNull MainContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getRealInfo() {
        HttpMethods.Companion.getInstance().realInfo(
                new HttpObserver(false, getRootView(), new HttpListener<RealInfoBean>() {
                    @Override
                    public void onSuccess(@Nullable RealInfoBean bean, @Nullable String msg) {
                        if (bean == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveMyInfo(bean);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(EventBusEvent.GET_REAL_INFO_SUCCESS, "");
                        EventBus.getDefault().post(map);
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getRealInfoSuccess(bean);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {

                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void balance() {
        HttpMethods.Companion.getInstance().balance(
                new HttpObserver(false, getRootView(), new HttpListener<BalanceBean>() {
                    @Override
                    public void onSuccess(@Nullable BalanceBean bean, @Nullable String msg) {
                        if (bean == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveBalance(bean);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(EventBusEvent.GET_REAL_INFO_SUCCESS, "");
                        EventBus.getDefault().post(map);
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

    @Override
    public void installIndex() {
        HttpMethods.Companion.getInstance().installIndex(
                new HttpObserver(false, getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
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
