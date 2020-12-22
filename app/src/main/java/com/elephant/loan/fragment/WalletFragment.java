package com.elephant.loan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.bean.BalanceBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;
import com.elephant.loan.activity.MainActivity;
import com.elephant.loan.contract.WalletContract;
import com.elephant.loan.presenter.WalletPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class WalletFragment extends BaseFragment<WalletContract.Presenter> implements WalletContract.View {

    @NotNull
    @Override
    protected WalletContract.Presenter onCreatePresenter() {
        return new WalletPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(view);
        setViewsOnClickListener(R.id.tvWithdraw);
        if (DataManager.Companion.getInstance().getBalance() == null) {
            ((MainActivity) getActivity()).getBalance();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWithdraw:
                break;
        }
    }

    @Override
    protected void updateUIText() {
        BalanceBean bean = DataManager.Companion.getInstance().getBalance();
        if (bean == null || bean.getMoney() <= 0.0) {
            setText(R.id.tvBalance, "0.00");
            setViewGone(R.id.tvWithdraw);
        } else {
            setText(R.id.tvBalance, String.valueOf(bean.getMoney()));
            setViewVisible(R.id.tvWithdraw);
        }
        RealInfoBean infoBean = DataManager.Companion.getInstance().getMyInfo();
        if(TextUtils.isEmpty(infoBean.getName())) {
            setText(R.id.tvId, "");
            setText(R.id.tvName, infoBean.getLoginAccount());
        }else{
            setText(R.id.tvId, String.valueOf(infoBean.getUser_id()));
            setText(R.id.tvName, infoBean.getName());
        }
        if (TextUtils.isEmpty(infoBean.getBank_card())) {
            setViewGone(R.id.tvBankName, R.id.tvBankCardNo);
            setViewVisible(R.id.tvNoBankCard);
        } else {
            setViewVisible(R.id.tvBankName, R.id.tvBankCardNo);
            setViewGone(R.id.tvNoBankCard);
            setText(R.id.tvBankName, infoBean.getBank_name());
            setText(R.id.tvBankCardNo, infoBean.getBank_card());
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
