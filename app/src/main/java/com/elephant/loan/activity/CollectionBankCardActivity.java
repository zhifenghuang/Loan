package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.manager.DataManager;
import com.elephant.loan.R;
import com.elephant.loan.contract.CollectionBankCardContract;
import com.elephant.loan.presenter.CollectionBankCardPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class CollectionBankCardActivity extends BaseActivity<CollectionBankCardContract.Presenter> implements CollectionBankCardContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection_bank_card;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_bank_card);
        initInputListener();
        setViewsOnClickListener(R.id.tvSubmit);
    }

    @NotNull
    @Override
    protected CollectionBankCardContract.Presenter onCreatePresenter() {
        return new CollectionBankCardPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                getPresenter().uploadBankInfo(getTextById(R.id.etOwnerName), getTextById(R.id.etCerNo), getTextById(R.id.tvBankName), getTextById(R.id.etBankCardNo));
                break;
        }
    }

    @Override
    public void uploadSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        bean.setBank_user(getTextById(R.id.etOwnerName));
        bean.setBank_id_card(getTextById(R.id.etCerNo));
        bean.setBank_name(getTextById(R.id.tvBankName));
        bean.setBank_card(getTextById(R.id.etBankCardNo));
        DataManager.Companion.getInstance().saveMyInfo(bean);
        finish();
    }

    private void initInputListener() {
        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);
        final EditText etOwnerName = findViewById(R.id.etOwnerName);
        final EditText etCerNo = findViewById(R.id.etCerNo);
        final EditText etBankCardNo = findViewById(R.id.etBankCardNo);
        Observable.combineLatest(RxTextView.textChanges(etOwnerName).skip(1),
                RxTextView.textChanges(etCerNo).skip(1),
                RxTextView.textChanges(etBankCardNo).skip(1),
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                        return !TextUtils.isEmpty(getTextById(R.id.etOwnerName))
                                && !TextUtils.isEmpty(getTextById(R.id.etCerNo))
                                && !TextUtils.isEmpty(getTextById(R.id.etBankCardNo));
                    }

                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvSubmit.setEnabled(true);
                } else {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvSubmit.setEnabled(false);
                }
            }
        });
    }
}
