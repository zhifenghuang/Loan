package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ArticleBean;
import com.common.lib.bean.LoanInfoBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.network.HttpMethods;
import com.elephant.loan.R;
import com.elephant.loan.contract.ContractContract;
import com.elephant.loan.presenter.ContractPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ContractActivity extends BaseActivity<ContractContract.Presenter> implements ContractContract.View {

    private int mType;
    private LoanInfoBean mLoanInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract;
    }

    @Override
    protected void updateUIText() {
        getPresenter().getContract(mType);
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        mType = bundle.getInt(Constants.BUNDLE_EXTRA, 100);
        mLoanInfo = (LoanInfoBean) bundle.getSerializable(Constants.BUNDLE_EXTRA_2);
    }

    @NotNull
    @Override
    protected ContractContract.Presenter onCreatePresenter() {
        return new ContractPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getContractSuccess(ArrayList<ArticleBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tv_title, list.get(0).getTitle());
        String sign = bean.getSign().replace("\\", "/");
        String signUrl = "<img src=\"" + HttpMethods.Companion.getInstance().getBaseUrl() + sign + "\" style=\"width:300px; height:100px\">";

        String content = list.get(0).getContent().
                replace("{user_name}", bean.getName())
                .replace("{id_card}", bean.getId_card())
                .replace("{loan_money2}", mLoanInfo.geAmountStr())
                .replace("{loan_time}", mLoanInfo.getTerm())
                .replace("{loan_phone}", bean.getPhone())
                .replace("{contract_no}", mLoanInfo.getOrder_id())
                .replace("{loan_date}", mLoanInfo.getCreated_at().substring(0, 10))
                .replace("{loan_date_time}", mLoanInfo.getCreated_at().substring(0, 10))
                .replace("{loan_money1}", mLoanInfo.getAmount_class())
                .replace("{loan_term}", mLoanInfo.getTerm())
                .replace("{loan_rate}", mLoanInfo.getRate() + "%")
                .replace("{user_sign}", signUrl);
        setHtml(R.id.tvContent, content);
    }
}
