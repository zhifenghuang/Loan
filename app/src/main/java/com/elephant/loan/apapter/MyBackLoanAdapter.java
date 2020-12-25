package com.elephant.loan.apapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.RepayBean;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;


public class MyBackLoanAdapter extends BaseQuickAdapter<RepayBean, BaseViewHolder> {

    private Context mContext;


    public MyBackLoanAdapter(Context context) {
        super(R.layout.item_my_back_loan);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, RepayBean item) {
        helper.setText(R.id.tvLoanNo, mContext.getString(R.string.app_loan_no_xxx, item.getOrder_id()))
                .setText(R.id.tvBackStatus, item.getRepay_status())
                .setText(R.id.tvBackLoanTime, item.getRepay_time())
                .setText(R.id.tvBackLoanMoney, item.getRepay_money())
                .setText(R.id.tvBackLoanWay, item.getRepay_way());
    }

}
