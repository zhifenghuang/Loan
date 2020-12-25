package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.LoanInfoBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoanDetailActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    private LoanInfoBean mDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loan_detail;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.flTop);
        mDetail = (LoanInfoBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        findViewById(R.id.topView).setBackgroundColor(ContextCompat.getColor(this, R.color.color_ff_ff_ff));
        setText(R.id.tv_title, R.string.app_loan_detail);

        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvNo, getString(R.string.app_loan_no_xxx, mDetail.getOrder_id()));
        setText(R.id.tvTime, mDetail.getCreated_at().substring(0, 10));
        setText(R.id.tvLoanMoney, mDetail.getAmount());
        setText(R.id.tvLoanTime, mDetail.getTerm());

        int status = mDetail.getStatus();
        setText(R.id.tvProgress1, R.string.app_submit_complete);
        setText(R.id.tvProgress2, R.string.app_loan_status_0);
        if (status == 0) {
            setText(R.id.tvExplain, R.string.app_loan_status_0);
            setText(R.id.tvBackMoneyInfo, R.string.app_loan_status_0);
            setText(R.id.tvProgress3, R.string.app_receive_success);
            setText(R.id.tvBackMoneyMonth, mDetail.getRepay_m());
        } else if (status == 1) {
            setText(R.id.tvExplain, R.string.app_loan_status_1);
            setText(R.id.tvBackMoneyInfo, R.string.app_loan_status_1);
            setText(R.id.tvProgress3, R.string.app_loan_status_1);
            setTextColor(R.id.tvProgress2, R.color.color_0d_0d_0d);
            setTextColor(R.id.tvProgress3, R.color.color_e3_4e_47);
            setBackground(R.id.ivProgress3, R.drawable.app_shape_e34e47_stroke_2_circle);
            setBackgroundColor(R.id.lineProgress, R.color.color_e3_4e_47);
            setText(R.id.tvBackMoneyMonth, R.string.app_loan_status_1);
            setTextColor(R.id.tvBackMoneyMonth, R.color.color_e3_4e_47);
            setTextColor(R.id.tvBackMoneyInfo, R.color.color_e3_4e_47);
        } else {
            setText(R.id.tvExplain, R.string.app_loan_status_2_info);
            setText(R.id.tvBackMoneyInfo, R.string.app_loan_status_2_info);
            setTextColor(R.id.tvProgress2, R.color.color_0d_0d_0d);
            setText(R.id.tvProgress3, R.string.app_receive_success);
            setTextColor(R.id.tvProgress3, R.color.color_49_68_ea);
            setBackground(R.id.ivProgress3, R.drawable.app_shape_4968ea_stroke_2_circle);
            setBackgroundColor(R.id.lineProgress, R.color.color_49_68_ea);
            setText(R.id.tvBackMoneyMonth, mDetail.getRepay_m());
        }
        setText(R.id.tvOrderNo, getString(R.string.app_loan_no_xxx, mDetail.getOrder_id()));
        setText(R.id.tvLoanMoney2, getString(R.string.app_loan_money_xxx, mDetail.getAmount()));
        setText(R.id.tvLoanTime2, getString(R.string.app_loan_time_xxx, mDetail.getTerm()));
        setText(R.id.tvReceiveBank, getString(R.string.app_receive_bank, bean.getBank_name()));
        setText(R.id.tvBankCard, getString(R.string.app_receive_bank_card_id, bean.getBank_card()));
    }

    @NotNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }
}
