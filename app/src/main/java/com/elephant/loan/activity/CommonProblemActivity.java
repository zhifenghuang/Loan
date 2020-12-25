package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ArticleBean;
import com.elephant.loan.R;
import com.elephant.loan.apapter.CommonProblemAdapter;
import com.elephant.loan.contract.CommonProblemContract;
import com.elephant.loan.presenter.CommonProblemPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CommonProblemActivity extends BaseActivity<CommonProblemContract.Presenter>
        implements CommonProblemContract.View {

    private CommonProblemAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_problem;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_common_question);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        getPresenter().getArticleList();
    }

    private CommonProblemAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommonProblemAdapter(this);
            mAdapter.addChildClickViewIds(R.id.llTitle);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    ArticleBean bean = mAdapter.getItem(position);
                    bean.setShowContent(!bean.isShowContent());
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        return mAdapter;
    }

    @NotNull
    @Override
    protected CommonProblemContract.Presenter onCreatePresenter() {
        return new CommonProblemPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getArticleListSuccess(ArrayList<ArticleBean> list) {
        getAdapter().setNewInstance(list);
        getAdapter().notifyDataSetChanged();
    }

}
