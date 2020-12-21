package com.elephant.loan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.elephant.loan.R;
import com.elephant.loan.activity.RealNameVerifyActivity;
import com.elephant.loan.activity.SettingActivity;
import com.elephant.loan.contract.MineContract;
import com.elephant.loan.presenter.MinePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MineFragment extends BaseFragment<MineContract.Presenter> implements MineContract.View {

    @NotNull
    @Override
    protected MineContract.Presenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(view);
        setViewsOnClickListener(R.id.ivSetting, R.id.llWallet, R.id.llRealNameVerify,
                R.id.llMyLoan, R.id.llMyBackMoney, R.id.llOnlineService, R.id.llCommonQuestion);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivSetting:
                openActivity(SettingActivity.class);
                break;
            case R.id.llWallet:
                break;
            case R.id.llRealNameVerify:
                openActivity(RealNameVerifyActivity.class);
                break;
            case R.id.llMyLoan:
                break;
            case R.id.llMyBackMoney:
                break;
            case R.id.llOnlineService:
                getPresenter().getServiceUrl();
                break;
            case R.id.llCommonQuestion:
                break;
        }
    }

    @Override
    protected void updateUIText() {
        RealInfoBean infoBean = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvId, String.valueOf(infoBean.getUser_id()));
        setText(R.id.tvName, infoBean.getName());
    }

    @Override
    public void getServiceSuccess(String serviceUrl) {
        if (getView() == null) {
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(serviceUrl);
            intent.setData(content_url);
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (map != null) {
            try {
                if (map.containsKey(EventBusEvent.GET_REAL_INFO_SUCCESS)) {
                    if (getView() != null) {
                        updateUIText();
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}
