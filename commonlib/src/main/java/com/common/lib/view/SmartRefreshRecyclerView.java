package com.common.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.lib.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class SmartRefreshRecyclerView extends SmartRefreshLayout {

    private RecyclerView recyclerView;
    private View ll_empty_view;
    private TextView tv_empty;
    private BaseQuickAdapter mAdapter;


    public SmartRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public SmartRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView();
    }

    private void iniView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_smart_refresh_recyclerview, this);
        recyclerView = findViewById(R.id.recyclerView);
        ll_empty_view = findViewById(R.id.ll_empty_view);
        tv_empty = findViewById(R.id.tv_empty);
    }

    public void initView(BaseQuickAdapter adapter, String emptyText, boolean isCanRefresh, boolean isCanLoadMore) {
        setEnableRefresh(isCanRefresh);
        setEnableLoadmore(isCanLoadMore);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        mAdapter = adapter;
        tv_empty.setText(emptyText == null ? "" : emptyText);
    }

    public void showView() {
        setVisibility(View.VISIBLE);
        if (mAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            ll_empty_view.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            ll_empty_view.setVisibility(View.GONE);
        }
    }

    public void stopLoad() {
        finishRefresh();
        finishLoadmore();
    }
}
