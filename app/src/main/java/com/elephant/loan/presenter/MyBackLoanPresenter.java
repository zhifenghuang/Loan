package com.elephant.loan.presenter;

import com.common.lib.bean.RepayBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MyBackLoanContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class MyBackLoanPresenter extends BasePresenter<MyBackLoanContract.View> implements MyBackLoanContract.Presenter {

    public MyBackLoanPresenter(@NotNull MyBackLoanContract.View rootView) {
        super(rootView);
    }


    @Override
    public void loanRepay() {
        HttpMethods.Companion.getInstance().getRepayList(
                new HttpObserver(getRootView(), new HttpListener<ArrayList<RepayBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<RepayBean> list, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getLoanRepaySuccess(list);
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
