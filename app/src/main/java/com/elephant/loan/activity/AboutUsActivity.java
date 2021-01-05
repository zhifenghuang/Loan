package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ArticleBean;
import com.elephant.loan.R;
import com.elephant.loan.contract.AboutUsContract;
import com.elephant.loan.presenter.AboutUsPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class AboutUsActivity extends BaseActivity<AboutUsContract.Presenter> implements AboutUsContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void updateUIText() {
        getPresenter().aboutUs();
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_about_us);
    }

    @NotNull
    @Override
    protected AboutUsContract.Presenter onCreatePresenter() {
        return new AboutUsPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getAboutUsSuccess(ArrayList<ArticleBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        setText(R.id.tvTitle, list.get(0).getTitle());
        setHtml(R.id.tvContent, list.get(0).getContent());
    }
}
