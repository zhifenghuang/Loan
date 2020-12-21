package com.elephant.loan.presenter;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.common.lib.utils.MD5Util;
import com.elephant.loan.contract.LoginContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(@NotNull LoginContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getVerCode(String phone) {
        HttpMethods.Companion.getInstance().sms(phone,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().getVerCodeSuccess();
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
    public void login(final String phone, String password) {
        HttpMethods.Companion.getInstance().login(phone, password,
                new HttpObserver(getRootView(), new HttpListener<String>() {
                    @Override
                    public void onSuccess(@Nullable String token, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        RealInfoBean bean = new RealInfoBean();
                        bean.setLoginAccount(phone);
                        DataManager.Companion.getInstance().saveMyInfo(bean);
                        DataManager.Companion.getInstance().saveToken(token);
                        getRootView().loginSuccess();
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
    public void register(String phone, String password, String code) {
        HttpMethods.Companion.getInstance().register(phone, password, code,
                new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object bean, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        login(phone, MD5Util.INSTANCE.getMd5(password));
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
