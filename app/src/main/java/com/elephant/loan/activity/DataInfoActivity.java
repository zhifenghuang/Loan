package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.dialog.MyDialogFragment;
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
        initInputListener();
        setViewsOnClickListener(R.id.llEducation, R.id.llIncome, R.id.tvSubmit);
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


    private void showSelectOneDialog(final int type) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.dialog_select_one);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                WheelView wheelView = view.findViewById(R.id.wheelView);
                final ArrayList<String> list = new ArrayList<>();
                if (type == 0) {
                    list.add(getString(R.string.app_education_0));
                    list.add(getString(R.string.app_education_1));
                    list.add(getString(R.string.app_education_2));
                    list.add(getString(R.string.app_education_3));
                } else if (type == 1) {
                    list.add(getString(R.string.app_income_0));
                    list.add(getString(R.string.app_income_1));
                    list.add(getString(R.string.app_income_2));
                    list.add(getString(R.string.app_income_3));
                    list.add(getString(R.string.app_income_4));
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
