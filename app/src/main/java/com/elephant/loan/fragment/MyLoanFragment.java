package com.elephant.loan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.elephant.loan.R;
import com.elephant.loan.activity.RealNameVerifyActivity;
import com.elephant.loan.contract.MyLoanContract;
import com.elephant.loan.presenter.MyLoanPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class MyLoanFragment extends BaseFragment<MyLoanContract.Presenter> implements MyLoanContract.View {

    private ArrayList<HashMap<String, String>> mLoanInfo;

    private String mMonth;
    private String mRange;
    private String mRate, mLoanValue;

    @NotNull
    @Override
    protected MyLoanContract.Presenter onCreatePresenter() {
        return new MyLoanPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_loan;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        mLoanInfo = DataManager.Companion.getInstance().getLoanInfo();
        setViewsOnClickListener(R.id.tvApplyNow, R.id.tvRange1, R.id.tvRange2, R.id.tvRange3, R.id.tvRange4,
                R.id.tvRange5, R.id.tvRange6, R.id.tvTime1, R.id.tvTime2, R.id.tvTime3, R.id.tvTime4);
        mMonth = "12";
        mRate = "0.5%";
        mRange = getString(R.string.app_money_0);
        mLoanValue = "45000";
        resetView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRange1:
            case R.id.tvRange2:
            case R.id.tvRange3:
            case R.id.tvRange4:
            case R.id.tvRange5:
            case R.id.tvRange6:
                if (mLoanInfo == null) {
                    getPresenter().getLoanInfo();
                    return;
                }
                mLoanValue = (String) v.getTag();
                mRange = ((TextView) v).getText().toString();
                mRange = mRange.replaceAll("\n", "");
                setText(R.id.tvMoney, mRange);
                resetTabs((TextView) v, getView().findViewById(R.id.tvRange1), getView().findViewById(R.id.tvRange2), getView().findViewById(R.id.tvRange3),
                        getView().findViewById(R.id.tvRange4), getView().findViewById(R.id.tvRange5), getView().findViewById(R.id.tvRange6));
                break;
            case R.id.tvTime1:
            case R.id.tvTime2:
            case R.id.tvTime3:
            case R.id.tvTime4:
                if (mLoanInfo == null) {
                    getPresenter().getLoanInfo();
                    return;
                }
                mMonth = (String) v.getTag();
                setText(R.id.tvMonth, mMonth);
                resetTabs((TextView) v, getView().findViewById(R.id.tvTime1), getView().findViewById(R.id.tvTime2), getView().findViewById(R.id.tvTime3),
                        getView().findViewById(R.id.tvTime4));

                break;
            case R.id.tvApplyNow:
                RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
                if (TextUtils.isEmpty(realInfoBean.getName())
                        || TextUtils.isEmpty(realInfoBean.getEducation())
                        || TextUtils.isEmpty(realInfoBean.getBank_id_card())
                        || TextUtils.isEmpty(realInfoBean.getPhone())
                        || TextUtils.isEmpty(realInfoBean.getSign())) {
                    showVerifyRealNameDialog();
                    return;
                }
                showSureLoanDialog(realInfoBean);
                break;
        }
    }

    private void showVerifyRealNameDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_remind_real_name_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.btn);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.btn:
                        openActivity(RealNameVerifyActivity.class);
                        break;
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    private void showSureLoanDialog(final RealInfoBean realInfoBean) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_info_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                dialogFragment.setClickDismiss(false);
                TextView tv = view.findViewById(R.id.tvLoanInfo);
                tv.setText(getString(R.string.app_loan_user_info,
                        realInfoBean.getName(),
                        realInfoBean.getId_card(),
                        realInfoBean.getPhone(),
                        mRange,
                        mMonth + getString(R.string.app_sum_month),
                        mRate));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.tvLookContract, R.id.btn);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.ivClose:
                        dialogFragment.dismiss();
                        break;
                    case R.id.tvLookContract:
                        break;
                    case R.id.btn:
                        getPresenter().applyLoan(mLoanValue, mRange, mMonth);
                        dialogFragment.dismiss();
                        break;
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    @Override
    protected void updateUIText() {
        getPresenter().banner();
    }

    @Override
    public void getBannerSuccess(String banner) {
        if (getView() == null || TextUtils.isEmpty(banner)) {
            return;
        }
        setText(R.id.tvBanner, banner);
    }

    @Override
    public void getLoanInfoSuccess(ArrayList<HashMap<String, String>> list) {
        mLoanInfo = list;
        resetView();
    }

    @Override
    public void applyLoanSuccess() {
        showApplyLoanResultDialog(true);
    }

    @Override
    public void applyLoanFailed() {
        showApplyLoanResultDialog(false);
    }

    private int mTotalTime;

    private void showApplyLoanResultDialog(final boolean isSuccess) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                if (!isSuccess) {
                    ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_t_fail);
                    ((TextView) view.findViewById(R.id.tvStatus)).setText(getString(R.string.app_loan_failed_tip));
                }
                mTotalTime = 5;
                countTime(view.findViewById(R.id.tvCloseTip), dialogFragment);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    private void countTime(final TextView tv, final MyDialogFragment dialogFragment) {
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


    private void resetView() {
        if (getView() == null || mLoanInfo == null) {
            return;
        }
        String key, value;
        TextView tv;
        for (HashMap<String, String> map : mLoanInfo) {
            try {
                key = map.get("key");
                value = map.get("value");
                if (key.equals("loan_rate")) {
                    mRate = Integer.parseInt(value) * 0.01 + "%";
                    setText(R.id.tvRate, mRate);
                } else if (key.equals("loan_term") && !TextUtils.isEmpty(value)) {
                    String[] terms = value.split(",");
                    for (int i = 0; i < 4; ++i) {
                        if (i == 0) {
                            mMonth = terms[i];
                        }
                        tv = getView().findViewById(getResources().getIdentifier("tvTime" + (i + 1), "id", getActivity().getPackageName()));
                        if (i < terms.length) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(terms[i] + getString(R.string.app_sum_month));
                            tv.setTag(terms[i]);
                        } else {
                            tv.setVisibility(View.INVISIBLE);
                        }
                    }
                } else if (key.equals("loan_class_1")) {
                    mLoanValue = value;
                    getView().findViewById(R.id.tvRange1).setTag(value);
                } else if (key.equals("loan_class_2")) {
                    getView().findViewById(R.id.tvRange2).setTag(value);
                } else if (key.equals("loan_class_3")) {
                    getView().findViewById(R.id.tvRange3).setTag(value);
                } else if (key.equals("loan_class_4")) {
                    getView().findViewById(R.id.tvRange4).setTag(value);
                } else if (key.equals("loan_class_5")) {
                    getView().findViewById(R.id.tvRange5).setTag(value);
                } else if (key.equals("loan_class_6")) {
                    getView().findViewById(R.id.tvRange6).setTag(value);
                }
            } catch (Exception e) {

            }
        }
    }

    private void resetTabs(TextView... tvs) {
        for (int i = 1; i < tvs.length; ++i) {
            tvs[i].setBackgroundResource(R.drawable.app_shape_ffffff_4);
            tvs[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.color_8b_8b_8b));
        }
        tvs[0].setBackgroundResource(R.drawable.app_shape_4968ea_4);
        tvs[0].setTextColor(ContextCompat.getColor(getActivity(), R.color.color_ff_ff_ff));
    }
}