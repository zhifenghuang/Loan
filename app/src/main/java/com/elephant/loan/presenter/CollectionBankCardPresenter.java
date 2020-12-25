package com.elephant.loan.presenter;

import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;
import com.elephant.loan.contract.CollectionBankCardContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CollectionBankCardPresenter extends BasePresenter<CollectionBankCardContract.View> implements CollectionBankCardContract.Presenter {

    public CollectionBankCardPresenter(@NotNull CollectionBankCardContract.View rootView) {
        super(rootView);
    }

    @Override
    public void uploadBankInfo(String bank_user, String bank_id_card, String bank_name, String bank_card) {
        HttpMethods.Companion.getInstance().realBank(bank_user, bank_id_card, bank_name, bank_card,
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
