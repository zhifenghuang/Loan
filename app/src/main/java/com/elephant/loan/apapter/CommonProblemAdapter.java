package com.elephant.loan.apapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ArticleBean;
import com.elephant.loan.R;

import org.jetbrains.annotations.NotNull;


public class CommonProblemAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {

    private Context mContext;


    public CommonProblemAdapter(Context context) {
        super(R.layout.item_common_problem);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.tvTitle, item.getTitle())
                .setText(R.id.tvContent, item.getContent())
                .setGone(R.id.line, helper.getAdapterPosition() == getItemCount() - 1);
        if (item.isShowContent()) {
            helper.setGone(R.id.tvContent, false);
            helper.getView(R.id.ivArrow).setRotation(90f);
        } else {
            helper.setGone(R.id.tvContent, true);
            helper.getView(R.id.ivArrow).setRotation(0f);
        }

    }

}
