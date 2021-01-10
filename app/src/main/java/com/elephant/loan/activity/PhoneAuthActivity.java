package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.Constants;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.interfaces.OnClickCallback;
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
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getPhone())) {
            initInputListener();
            setViewsOnClickListener(R.id.tvSubmit);
        } else {
            setViewGone(R.id.etPhone);
            setViewVisible(R.id.tvPhone);
            setText(R.id.tvPhone, realInfoBean.getPhone());
            TextView tvSubmit = findViewById(R.id.tvSubmit);
            tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
            tvSubmit.setEnabled(false);
            tvSubmit.setText(getString(R.string.app_had_submit));
        }
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
                String phone = getTextById(R.id.etPhone);
                if (!isPhoneCorrect(phone)) {
                    showToast(getString(R.string.app_input_phone_wrong));
                    return;
                }
                getPresenter().phoneAuth(phone);
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

    @Override
    public void onBackClick(View view) {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getPhone())) {
            showTwoBtnDialog(null,
                    getString(R.string.app_sure_back),
                    ContextCompat.getColor(this, R.color.color_0d_0d_0d),
                    getString(R.string.app_cancel),
                    getString(R.string.app_ok),
                    new OnClickCallback() {
                        @Override
                        public void onClick(int viewId) {
                            if (viewId == R.id.btn2) {
                                finish();
                            }
                        }
                    });
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackClick(null);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void uploadFailed(String msg) {
        showUploadFailedDialog();
    }

    private int mTotalTime;

    private void showUploadFailedDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_home_t_zhu);
                ((TextView) view.findViewById(R.id.tvStatus)).setText(getString(R.string.app_failed));
                ((TextView) view.findViewById(R.id.tvTip)).setText(getString(R.string.app_failed_reason));
                mTotalTime = 3;
                countTime(view.findViewById(R.id.tvCloseTip), dialogFragment);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    private void countTime(final TextView tv, final MyDialogFragment dialogFragment) {
        if (isDestroyed()) {
            return;
        }
        tv.setText(getString(R.string.app_the_window_will_close_xxx_second, String.valueOf(mTotalTime)));
        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                --mTotalTime;
                if (mTotalTime == 0) {
                    dialogFragment.dismiss();
                } else {
                    countTime(tv, dialogFragment);
                }
            }
        }, 1000);
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

    private boolean isPhoneCorrect(String phone) {
        int length = phone.length();
        if (Constants.APP_VERSION == 0) {
            if (length == 10) {
                return phone.startsWith("06") || phone.startsWith("08") || phone.startsWith("09");
            }
            if (length == 9) {
                return phone.startsWith("6") || phone.startsWith("8") || phone.startsWith("9");
            }
        } else {
            return (length == 10 && phone.startsWith("0")) || length == 9;
        }
        return false;
    }
}
