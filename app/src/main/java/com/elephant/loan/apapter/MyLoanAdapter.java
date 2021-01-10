package com.elephant.loan.apapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

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
                .setText(R.id.tvLoanMoney, item.geAmountStr())
                .setText(R.id.tvLoanTime, item.getTerm() + mContext.getString(R.string.app_sum_month))
                .setText(R.id.tvMonthRate, item.getRate() + "%")
                .setText(R.id.tvApplyTime, item.getCreated_at().substring(0, 10));

        int status = item.getStatus();
        if (status == 0) {
            helper.setText(R.id.tvStatus, R.string.app_loan_status_0)
                    .setTextColor(R.id.tvStatus, ContextCompat.getColor(mContext, R.color.color_f2_bc_0b));
        } else if (status == 1) {
            helper.setText(R.id.tvStatus, R.string.app_loan_status_1)
                    .setTextColor(R.id.tvStatus, ContextCompat.getColor(mContext, R.color.color_f6_3e_3e));
        } else {
            helper.setText(R.id.tvStatus, R.string.app_loan_status_2)
                    .setTextColor(R.id.tvStatus, ContextCompat.getColor(mContext, R.color.color_25_ac_1b));
        }
    }

}
