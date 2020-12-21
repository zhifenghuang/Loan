package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.lib.activity.BaseActivity;
import com.elephant.loan.R;
import com.elephant.loan.contract.ChangePasswordContract;
import com.elephant.loan.presenter.ChangePasswordPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class ChangePasswordActivity extends BaseActivity<ChangePasswordContract.Presenter> implements ChangePasswordContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_modify_login_psw);
        initInputListener();
        setViewsOnClickListener(R.id.tvSubmit);
    }

    @NotNull
    @Override
    protected ChangePasswordContract.Presenter onCreatePresenter() {
        return new ChangePasswordPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                getPresenter().changePassword(getTextById(R.id.etOldPsw), getTextById(R.id.etNewPsw), getTextById(R.id.etNewPswAgain));
                break;
        }
    }

    private void initInputListener() {
        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);
        final EditText etOldPsw = findViewById(R.id.etOldPsw);
        final EditText etNewPsw = findViewById(R.id.etNewPsw);
        final EditText etNewPswAgain = findViewById(R.id.etNewPswAgain);
        Observable.combineLatest(RxTextView.textChanges(etOldPsw).skip(1),
                RxTextView.textChanges(etNewPsw).skip(1),
                RxTextView.textChanges(etNewPswAgain).skip(1),
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                        return !TextUtils.isEmpty(getTextById(R.id.etOldPsw))
                                && !TextUtils.isEmpty(getTextById(R.id.etNewPsw))
                                && !TextUtils.isEmpty(getTextById(R.id.etNewPswAgain));
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

    @Override
    public void changePswSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        finish();
    }
}
