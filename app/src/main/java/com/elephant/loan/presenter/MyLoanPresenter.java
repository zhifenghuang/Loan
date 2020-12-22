package com.elephant.loan.presenter;

import com.common.lib.constant.EventBusEvent;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MyLoanContract;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;


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

    @Override
    public void getLoanInfo() {
        HttpMethods.Companion.getInstance().paramsIndex(
                new HttpObserver(false, getRootView(), new HttpListener<ArrayList<HashMap<String, String>>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<HashMap<String, String>> list, @Nullable String msg) {
                        if (list == null) {
                            return;
                        }
                        DataManager.Companion.getInstance().saveLoanInfo(list);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(EventBusEvent.GET_REAL_INFO_SUCCESS, "");
                        EventBus.getDefault().post(map);
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getLoanInfoSuccess(list);
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
    public void applyLoan(String amount, String a_class, String term) {
        HttpMethods.Companion.getInstance().applyLoan(amount, a_class, term,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().applyLoanSuccess();
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().applyLoanFailed();
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().applyLoanFailed();
                    }
                }, getCompositeDisposable()));
    }
}
