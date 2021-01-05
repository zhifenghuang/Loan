package com.elephant.loan.presenter;

import com.common.lib.bean.ArticleBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.AboutUsContract;
import com.elephant.loan.contract.ContractContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class ContractPresenter extends BasePresenter<ContractContract.View> implements ContractContract.Presenter {

    public ContractPresenter(@NotNull ContractContract.View rootView) {
        super(rootView);
    }


    @Override
    public void getContract(int type) {
        HttpMethods.Companion.getInstance().contract(type,
                new HttpObserver(true, getRootView(), new HttpListener<ArrayList<ArticleBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<ArticleBean> list, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getContractSuccess(list);
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
