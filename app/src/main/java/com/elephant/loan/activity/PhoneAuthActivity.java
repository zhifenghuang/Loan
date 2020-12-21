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
import com.elephant.loan.contract.PhoneAuthContract;
import com.elephant.loan.presenter.PhoneAuthPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.functions.Consumer;

public class PhoneAuthActivity extends BaseActivity<PhoneAuthContract.Presenter> implements PhoneAuthContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_auth;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_phone_auth);
        setViewsOnClickListener(R.id.tvSubmit);
        initInputListener();
    }

    @NotNull
    @Override
    protected PhoneAuthContract.Presenter onCreatePresenter() {
        return new PhoneAuthPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                getPresenter().phoneAuth(getTextById(R.id.etPhone));
                break;
        }
    }

    @Override
    public void phoneAuthSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        bean.setPhone(getTextById(R.id.etPhone));
        DataManager.Companion.getInstance().saveMyInfo(bean);
        finish();
    }

    private void initInputListener() {
        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);
        final EditText etPhone = findViewById(R.id.etPhone);
        RxTextView.textChanges(etPhone).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvSubmit.setEnabled(false);
                } else {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvSubmit.setEnabled(true);
                }
            }
        });
    }
}
