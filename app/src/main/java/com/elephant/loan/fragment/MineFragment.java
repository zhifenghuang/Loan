package com.elephant.loan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.bean.BalanceBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.elephant.loan.R;
import com.elephant.loan.activity.CommonProblemActivity;
import com.elephant.loan.activity.MainActivity;
import com.elephant.loan.activity.MyBackLoanActivity;
import com.elephant.loan.activity.MyLoanActivity;
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
        if (DataManager.Companion.getInstance().getBalance() == null) {
            ((MainActivity) getActivity()).getBalance();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivSetting:
                openActivity(SettingActivity.class);
                break;
            case R.id.llWallet:
                ((MainActivity) getActivity()).toWalletFragment();
                break;
            case R.id.llRealNameVerify:
                openActivity(RealNameVerifyActivity.class);
                break;
            case R.id.llMyLoan:
                openActivity(MyLoanActivity.class);
                break;
            case R.id.llMyBackMoney:
                openActivity(MyBackLoanActivity.class);
                break;
            case R.id.llOnlineService:
                ((MainActivity) getActivity()).toServiceFragment();
                break;
            case R.id.llCommonQuestion:
                openActivity(CommonProblemActivity.class);
                break;
        }
    }

    @Override
    protected void updateUIText() {
        BalanceBean bean = DataManager.Companion.getInstance().getBalance();
        setText(R.id.tvBalance, bean == null ? "0.00" : String.valueOf(bean.getMoney()));
        RealInfoBean infoBean = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvId, DataManager.Companion.getInstance().getLoginPhone());
        setText(R.id.tvName, infoBean.getName());
        if (TextUtils.isEmpty(infoBean.getName())
                || TextUtils.isEmpty(infoBean.getEducation())
                || TextUtils.isEmpty(infoBean.getBank_id_card())
                || TextUtils.isEmpty(infoBean.getPhone())
                || TextUtils.isEmpty(infoBean.getSign())) {
            setText(R.id.tvVerifyStatus, R.string.app_not_verify);
        } else {
            setText(R.id.tvVerifyStatus, R.string.app_verified);
        }
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
