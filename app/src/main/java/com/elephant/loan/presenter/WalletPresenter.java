package com.elephant.loan.presenter;

import com.common.lib.bean.WithdrawDetailBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.WalletContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class WalletPresenter extends BasePresenter<WalletContract.View> implements WalletContract.Presenter {

    public WalletPresenter(@NotNull WalletContract.View rootView) {
        super(rootView);
    }

    @Override
    public void withDraw(String pas) {
        HttpMethods.Companion.getInstance().applyWithDraw(pas,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().withDrawSuccess(msg);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().withDrawFailed(msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {

                    }
                }, getCompositeDisposable()));
    }

    @Override
    public void getWithDrawDetail() {
        HttpMethods.Companion.getInstance().withdrawDetail(
                new HttpObserver(getRootView(), new HttpListener<ArrayList<WithdrawDetailBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<WithdrawDetailBean> list, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getWithDrawDetailSuccess(list);
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
