package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.LogUtil;
import com.common.lib.view.WheelView;
import com.elephant.loan.R;
import com.elephant.loan.contract.DataInfoContract;
import com.elephant.loan.presenter.DataInfoPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class DataInfoActivity extends BaseActivity<DataInfoContract.Presenter> implements DataInfoContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_info;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_data_info);
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();

        if (TextUtils.isEmpty(realInfoBean.getEducation())) {
            initInputListener();
            setViewsOnClickListener(R.id.llEducation, R.id.llIncome, R.id.tvSubmit);
        } else {
            setViewGone(R.id.tvEducation, R.id.tvIncomeYear, R.id.etLoanUse);
            setViewVisible(R.id.tvEducation2, R.id.tvIncomeYear2, R.id.tvLoanUse);
            setText(R.id.tvEducation2, realInfoBean.getEducation());
            setText(R.id.tvIncomeYear2, realInfoBean.getIncome());
            setText(R.id.tvLoanUse, realInfoBean.getPurpose());
            TextView tvSubmit = findViewById(R.id.tvSubmit);
            tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
            tvSubmit.setEnabled(false);
            tvSubmit.setText(getString(R.string.app_had_submit));

            RadioButton rbHadHouse = findViewById(R.id.rbHadHouse);
            RadioButton rbNoHouse = findViewById(R.id.rbNoHouse);
            if (realInfoBean.getHouse() == 1) {
                rbHadHouse.setChecked(true);
                rbNoHouse.setChecked(false);
            } else {
                rbHadHouse.setChecked(false);
                rbNoHouse.setChecked(true);
            }

            RadioButton rbHadCar = findViewById(R.id.rbHadCar);
            RadioButton rbNoCar = findViewById(R.id.rbNoCar);
            if (realInfoBean.getCar() == 1) {
                rbHadCar.setChecked(true);
                rbNoCar.setChecked(false);
            } else {
                rbHadCar.setChecked(false);
                rbNoCar.setChecked(true);
            }
            rbHadHouse.setEnabled(false);
            rbNoHouse.setEnabled(false);
            rbHadCar.setEnabled(false);
            rbNoCar.setEnabled(false);
        }
    }

    @NotNull
    @Override
    protected DataInfoContract.Presenter onCreatePresenter() {
        return new DataInfoPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llEducation:
                showSelectOneDialog(0);
                break;
            case R.id.llIncome:
                showSelectOneDialog(1);
                break;
            case R.id.tvSubmit:
                String education = getTextById(R.id.tvEducation);
                String income = getTextById(R.id.tvIncomeYear);
                String use = getTextById(R.id.etLoanUse);
                int hadHouse = ((RadioButton) findViewById(R.id.rbHadHouse)).isChecked() ? 1 : 0;
                int hadCar = ((RadioButton) findViewById(R.id.rbHadCar)).isChecked() ? 1 : 0;
                getPresenter().uploadDataInfo(education, income, use, hadHouse, hadCar);
                break;
        }
    }

    @Override
    public void uploadSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        String education = getTextById(R.id.tvEducation);
        String income = getTextById(R.id.tvIncomeYear);
        String use = getTextById(R.id.etLoanUse);
        int hadHouse = ((RadioButton) findViewById(R.id.rbHadHouse)).isChecked() ? 1 : 0;
        int hadCar = ((RadioButton) findViewById(R.id.rbHadCar)).isChecked() ? 1 : 0;
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        bean.setEducation(education);
        bean.setIncome(income);
        bean.setPurpose(use);
        bean.setHouse(hadHouse);
        bean.setCar(hadCar);
        DataManager.Companion.getInstance().saveMyInfo(bean);
        finish();
    }

    @Override
    public void onBackClick(View view) {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getEducation())) {
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


    private void showSelectOneDialog(final int type) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.dialog_select_one);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                WheelView wheelView = view.findViewById(R.id.wheelView);
                TextView tvTitle = view.findViewById(R.id.tvTitle);
                final ArrayList<String> list = new ArrayList<>();
                if (type == 0) {
                    tvTitle.setText(getString(R.string.app_select_education));
                    list.add(getString(R.string.app_education_0));
                    list.add(getString(R.string.app_education_1));
                    list.add(getString(R.string.app_education_2));
                    list.add(getString(R.string.app_education_3));
                } else if (type == 1) {
                    tvTitle.setText(getString(R.string.app_select_income));
                    list.add(getString(R.string.app_income_0));
                    list.add(getString(R.string.app_income_1));
                    list.add(getString(R.string.app_income_2));
                    list.add(getString(R.string.app_income_3));
                    list.add(getString(R.string.app_income_4));
                    list.add(getString(R.string.app_income_5));
                }
                wheelView.setData(list);
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.tvOk);
            }

            @Override
            public void onViewClick(int viewId) {
                if (viewId == R.id.tvOk) {
                    WheelView wheelView = dialogFragment.getView().findViewById(R.id.wheelView);
                    if (type == 0) {
                        setText(R.id.tvEducation, wheelView.getSelectedText());
                    } else if (type == 1) {
                        setText(R.id.tvIncomeYear, wheelView.getSelectedText());
                    }
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    private void initInputListener() {

        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);

        final TextView tvEducation = findViewById(R.id.tvEducation);
        final TextView tvIncomeYear = findViewById(R.id.tvIncomeYear);
        final EditText etLoanUse = findViewById(R.id.etLoanUse);

        Observable.combineLatest(RxTextView.textChanges(tvEducation).skip(1),
                RxTextView.textChanges(tvIncomeYear).skip(1),
                RxTextView.textChanges(etLoanUse).skip(1),
                new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                        boolean isEducationVaild = !TextUtils.isEmpty(getTextById(R.id.tvEducation));
                        boolean isIncomeVaild = !TextUtils.isEmpty(getTextById(R.id.tvIncomeYear));
                        boolean isLoanVaild = !TextUtils.isEmpty(getTextById(R.id.etLoanUse));
                        return isEducationVaild && isIncomeVaild && isLoanVaild;
                    }

                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                LogUtil.LogE(aBoolean);
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
