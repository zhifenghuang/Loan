package com.elephant.loan.presenter;

import com.common.lib.bean.ArticleBean;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.AboutUsContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class AboutUsPresenter extends BasePresenter<AboutUsContract.View> implements AboutUsContract.Presenter {

    public AboutUsPresenter(@NotNull AboutUsContract.View rootView) {
        super(rootView);
    }


    @Override
    public void aboutUs() {
        HttpMethods.Companion.getInstance().aboutUs(
                new HttpObserver(true, getRootView(), new HttpListener<ArrayList<ArticleBean>>() {
                    @Override
                    public void onSuccess(@Nullable ArrayList<ArticleBean> list, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getAboutUsSuccess(list);
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
