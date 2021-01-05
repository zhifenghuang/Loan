package com.elephant.loan.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.MD5Util;
import com.common.lib.utils.NetUtil;
import com.elephant.loan.R;
import com.elephant.loan.contract.LoginContract;
import com.elephant.loan.presenter.LoginPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private int mType;//0表示登录1表示注册

    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.llLogin, R.id.llRegister, R.id.tvGetVerCode, R.id.tvLogin);
        mType = 0;
        initInputListener();
    }

    @NotNull
    @Override
    protected LoginContract.Presenter onCreatePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.tvLogin:
                String phone = getTextById(R.id.etPhone);
                if (!isPhoneCorrect(phone)) {
                    showToast(getString(R.string.app_input_phone_wrong));
                    return;
                }
                if (mType == 0) {
                    getPresenter().login(phone, MD5Util.INSTANCE.getMd5(getTextById(R.id.etPassword)));
                } else {
                    getPresenter().register(phone, getTextById(R.id.etPassword),
                            getTextById(R.id.etVerCode));
                }
                break;
            case R.id.llLogin:
                if (mType == 0) {
                    return;
                }
                mType = 0;
                resetTabs(viewId, R.id.llRegister);
                setViewGone(R.id.llVerCode, R.id.lineVerCode);
                setText(R.id.tvLogin, R.string.app_login);
                TextView tvLogin = findViewById(R.id.tvLogin);
                if (!TextUtils.isEmpty(getTextById(R.id.etPhone))
                        && !TextUtils.isEmpty(getTextById(R.id.etPassword))) {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvLogin.setEnabled(true);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvLogin.setEnabled(false);
                }
                break;
            case R.id.llRegister:
                if (mType == 1) {
                    return;
                }
                mType = 1;
                resetTabs(viewId, R.id.llLogin);
                setViewVisible(R.id.llVerCode, R.id.lineVerCode);
                setText(R.id.tvLogin, R.string.app_register_login);
                tvLogin = findViewById(R.id.tvLogin);
                if (!TextUtils.isEmpty(getTextById(R.id.etPhone))
                        && !TextUtils.isEmpty(getTextById(R.id.etPassword))
                        && !TextUtils.isEmpty(getTextById(R.id.etVerCode))) {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvLogin.setEnabled(true);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvLogin.setEnabled(false);
                }
                break;
            case R.id.tvGetVerCode:
                phone = getTextById(R.id.etPhone);
                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.app_please_input_phone));
                    return;
                }
                if (!isPhoneCorrect(phone)) {
                    showToast(getString(R.string.app_input_phone_wrong));
                    return;
                }
                if (NetUtil.isConnected(this)) {
                    mTotalTime = 60;
                    mTimer = new Timer();
                    initTimerTask();
                    mTimer.schedule(mTask, 1000, 1000);
                    getPresenter().getVerCode(phone);
                }
                break;
        }
    }

    private void resetTabs(int... ids) {
        LinearLayout ll = findViewById(ids[0]);
        TextView tv = (TextView) ll.getChildAt(0);
        tv.setTextColor(ContextCompat.getColor(this, R.color.color_49_68_ea));
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        ll.getChildAt(1).setVisibility(View.VISIBLE);

        for (int i = 1; i < ids.length; ++i) {
            ll = findViewById(ids[i]);
            tv = (TextView) ll.getChildAt(0);
            tv.setTextColor(ContextCompat.getColor(this, R.color.color_db_db_db));
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ll.getChildAt(1).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void getVerCodeSuccess() {
        showToast(getString(R.string.app_get_ver_code_success));
    }

    @Override
    public void loginSuccess() {
        if (DataManager.Companion.getInstance().getMyInfo() == null) {
            return;
        }
        openActivity(MainActivity.class);
        finish();
    }


    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinish()) {
                            return;
                        }
                        --mTotalTime;
                        TextView tvGetVerCode = findViewById(R.id.tvGetVerCode);
                        if (mTotalTime <= 0) {
                            tvGetVerCode.setTextColor(getResources().getColor(R.color.color_0d_0d_0d));
                            tvGetVerCode.setText(getString(R.string.app_send_again));
                            tvGetVerCode.setEnabled(true);
                            mTimer.cancel();
                            mTimer = null;
                        } else {
                            tvGetVerCode.setTextColor(getResources().getColor(R.color.color_49_68_ea));
                            tvGetVerCode.setText(mTotalTime + "s");
                            tvGetVerCode.setEnabled(false);
                        }
                    }
                });
            }
        };
    }

    private void initInputListener() {

        final TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvLogin.setEnabled(false);

        final EditText etPhone = findViewById(R.id.etPhone);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText etVerCode = findViewById(R.id.etVerCode);

        Observable.combineLatest(RxTextView.textChanges(etPhone).skip(1),
                RxTextView.textChanges(etPassword).skip(1),
                RxTextView.textChanges(etVerCode).skip(1),
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                        boolean isPhoneValid = !TextUtils.isEmpty(getTextById(R.id.etPhone));
                        boolean isPasswordValid = !TextUtils.isEmpty(getTextById(R.id.etPassword));
                        boolean isVerCodeValid = !TextUtils.isEmpty(getTextById(R.id.etVerCode));
                        return mType == 0 ? (isPhoneValid && isPasswordValid) : (isPhoneValid && isPasswordValid && isVerCodeValid);
                    }

                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                TextView tvLogin = findViewById(R.id.tvLogin);
                if (aBoolean) {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvLogin.setEnabled(true);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvLogin.setEnabled(false);
                }
            }
        });

        Observable.combineLatest(RxTextView.textChanges(etPhone).skip(1),
                RxTextView.textChanges(etPassword).skip(1),
                (BiFunction<CharSequence, CharSequence, Boolean>) (charSequence, charSequence2) -> {
                    boolean isPhoneValid = !TextUtils.isEmpty(getTextById(R.id.etPhone));
                    boolean isPasswordValid = !TextUtils.isEmpty(getTextById(R.id.etPassword));
                    boolean isVerCodeValid = !TextUtils.isEmpty(getTextById(R.id.etVerCode));

                    return mType == 0 ? (isPhoneValid && isPasswordValid) : (isPhoneValid && isPasswordValid && isVerCodeValid);
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvLogin.setEnabled(true);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvLogin.setEnabled(false);
                }
            }
        });
    }

    private boolean isPhoneCorrect(String phone) {
        int length = phone.length();
        if (length == 10) {
            return phone.startsWith("06") || phone.startsWith("08") || phone.startsWith("09");
        }
        if (length == 9) {
            return phone.startsWith("6") || phone.startsWith("8") || phone.startsWith("9");
        }
        return false;
    }
}
