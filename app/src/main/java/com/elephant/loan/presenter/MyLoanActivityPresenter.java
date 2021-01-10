package com.elephant.loan.presenter;

import com.common.lib.bean.LoanInfoBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.MyLoanActivityContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class MyLoanActivityPresenter extends BasePresenter<MyLoanActivityContract.View> implements MyLoanActivityContract.Presenter {

    public MyLoanActivityPresenter(@NotNull MyLoanActivityContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getMyLoan() {
        HttpMethods.Companion.getInstance().loanInfo(new HttpObserver(getRootView(), new HttpListener<ArrayList<LoanInfoBean>>() {
            @Override
            public void onSuccess(@Nullable ArrayList<LoanInfoBean> list, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getMyLoanSuccess(list);
            }

            @Override
            public void dataError(@Nullable int code, @Nullable String msg) {
                if (getRootView() == null) {
                    return;
                }
                getRootView().getMyLoanSuccess(null);
            }

            @Override
            public void connectError(@Nullable Throwable e) {
                if (getRootView() == null) {
                    return;
                }
            }
        }, getCompositeDisposable()));
    }
}
