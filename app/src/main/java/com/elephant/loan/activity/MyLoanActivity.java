package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.LoanInfoBean;
import com.common.lib.constant.Constants;
import com.elephant.loan.R;
import com.elephant.loan.apapter.MyLoanAdapter;
import com.elephant.loan.contract.MyLoanActivityContract;
import com.elephant.loan.presenter.MyLoanActivityPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MyLoanActivity extends BaseActivity<MyLoanActivityContract.Presenter> implements MyLoanActivityContract.View {

    private MyLoanAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_loan;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_my_loan_2);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        getPresenter().getMyLoan();
    }

    @NotNull
    @Override
    protected MyLoanActivityContract.Presenter onCreatePresenter() {
        return new MyLoanActivityPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getMyLoanSuccess(ArrayList<LoanInfoBean> list) {
        if (list != null && !list.isEmpty()) {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.llEmpty);
            getAdapter().setNewInstance(list);
        } else {
            setViewVisible(R.id.llEmpty);
            setViewGone(R.id.recyclerView);
        }
    }

    private MyLoanAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyLoanAdapter(this);
            mAdapter.addChildClickViewIds(R.id.tvLookContract, R.id.tvLoanDetail);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    LoanInfoBean bean = getAdapter().getItem(position);
                    switch (view.getId()) {
                        case R.id.tvLookContract:
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.BUNDLE_EXTRA, 200);
                            bundle.putSerializable(Constants.BUNDLE_EXTRA_2, bean);
                            openActivity(ContractActivity.class, bundle);
                            break;
                        case R.id.tvLoanDetail:
                            bundle = new Bundle();
                            bundle.putSerializable(Constants.BUNDLE_EXTRA, bean);
                            openActivity(LoanDetailActivity.class, bundle);
                            break;
                    }
                }
            });
        }
        return mAdapter;
    }
}
