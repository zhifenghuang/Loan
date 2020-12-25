package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RepayBean;
import com.elephant.loan.R;
import com.elephant.loan.apapter.MyBackLoanAdapter;
import com.elephant.loan.contract.MyBackLoanContract;
import com.elephant.loan.presenter.MyBackLoanPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MyBackLoanActivity extends BaseActivity<MyBackLoanContract.Presenter> implements MyBackLoanContract.View {

    private MyBackLoanAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_back_loan;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_my_back_money);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        getPresenter().loanRepay();
    }

    @NotNull
    @Override
    protected MyBackLoanContract.Presenter onCreatePresenter() {
        return new MyBackLoanPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    private MyBackLoanAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyBackLoanAdapter(this);
        }
        return mAdapter;
    }

    @Override
    public void getLoanRepaySuccess(ArrayList<RepayBean> list) {
        if (list != null && !list.isEmpty()) {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.llEmpty);
            getAdapter().setNewInstance(list);
        } else {
            setViewVisible(R.id.llEmpty);
            setViewGone(R.id.recyclerView);
        }
    }
}
