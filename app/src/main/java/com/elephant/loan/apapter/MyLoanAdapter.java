package com.elephant.loan.apapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.LoanInfoBean;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;


public class MyLoanAdapter extends BaseQuickAdapter<LoanInfoBean, BaseViewHolder> {

    private Context mContext;


    public MyLoanAdapter(Context context) {
        super(R.layout.item_my_loan);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, LoanInfoBean item) {
        helper.setText(R.id.tvLoanNo, mContext.getString(R.string.app_loan_no_xxx, item.getOrder_id()))
                .setText(R.id.tvLoanMoney, item.getAmount())
                .setText(R.id.tvLoanTime, item.getTerm() + mContext.getString(R.string.app_sum_month))
                .setText(R.id.tvMonthRate, item.getRate() + "%")
                .setText(R.id.tvApplyTime, item.getCreated_at());
    }

}
