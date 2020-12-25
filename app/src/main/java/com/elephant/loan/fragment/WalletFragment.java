package com.elephant.loan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.lib.bean.BalanceBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.bean.WithdrawDetailBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.view.OnInputListener;
import com.common.lib.view.VerifyCodeView;
import com.elephant.loan.R;
import com.elephant.loan.activity.MainActivity;
import com.elephant.loan.contract.WalletContract;
import com.elephant.loan.presenter.WalletPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class WalletFragment extends BaseFragment<WalletContract.Presenter> implements WalletContract.View {

    private int mTotalTime;

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
        getPresenter().getWithDrawDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWithdraw:
                showWithdrawDialog();
                break;
        }
    }

    private void showWithdrawDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_withdraw_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                BalanceBean bean = DataManager.Companion.getInstance().getBalance();
                double money = 0.00;
                if (bean != null) {
                    money = bean.getMoney();
                }
                ((TextView) view.findViewById(R.id.tvWithdrawMoney)).
                        setText(getString(R.string.app_withdraw_money_xxx, String.valueOf(money)));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose);

                VerifyCodeView codeView = view.findViewById(R.id.verifyCodeView);
                codeView.showKeybord();
                codeView.setOnInputListener(new OnInputListener() {
                    @Override
                    public void onSuccess(@NotNull String code) {
                        getPresenter().withDraw(code);
                        dialogFragment.dismiss();
                    }

                    @Override
                    public void onInput() {

                    }
                });
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
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
        if (TextUtils.isEmpty(infoBean.getName())) {
            setText(R.id.tvId, "");
            setText(R.id.tvName, infoBean.getLoginAccount());
        } else {
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

    @Override
    public void withDrawSuccess(String msg) {
        BalanceBean bean = DataManager.Companion.getInstance().getBalance();
        bean.setMoney(0.0);
        DataManager.Companion.getInstance().saveBalance(bean);
        setText(R.id.tvBalance, "0.00");
        setViewGone(R.id.tvWithdraw);

        getPresenter().getWithDrawDetail();

        showWithDrawResultDialog(true, msg);
    }

    @Override
    public void withDrawFailed(String msg) {
        showWithDrawResultDialog(false, msg);
    }

    @Override
    public void getWithDrawDetailSuccess(ArrayList<WithdrawDetailBean> list) {
        if (getView() == null || list == null || list.isEmpty()) {
            return;
        }
        WithdrawDetailBean bean = list.get(0);
        setText(R.id.tvWithdrawTime, bean.getCreated_at());
        setText(R.id.tvWithdrawMoney, bean.getMoney() + "（฿）");
        int status = bean.getStatus();
        TextView tvWithdrawStatus = getView().findViewById(R.id.tvWithdrawStatus);
        if (status == 0) {
            setText(tvWithdrawStatus, R.string.app_withdraw_status_0);
            setTextColor(tvWithdrawStatus, R.color.color_f2_bc_0b);
        } else if (status == 1) {
            setText(tvWithdrawStatus, R.string.app_no);
            setTextColor(tvWithdrawStatus, R.color.color_0d_0d_0d);
        } else if (status == 2) {
            setText(tvWithdrawStatus, R.string.app_withdraw_status_2);
            setTextColor(tvWithdrawStatus, R.color.color_25_ac_1b);
        } else {
            setText(tvWithdrawStatus, R.string.app_withdraw_status_3);
            setTextColor(tvWithdrawStatus, R.color.color_f6_3e_3e);
        }
    }

    private void showWithDrawResultDialog(final boolean isSuccess, final String msg) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                if (isSuccess) {
                    ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_home_t_chengg);
                    ((TextView) view.findViewById(R.id.tvStatus)).setText(msg == null ? "" : msg);
                } else {
                    ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_home_t_zhu);
                    ((TextView) view.findViewById(R.id.tvStatus)).setText(msg == null ? "" : msg);
                }
                mTotalTime = 3;
                countTime(view.findViewById(R.id.tvCloseTip), dialogFragment);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    private void countTime(final TextView tv, final MyDialogFragment dialogFragment) {
        if (getView() == null) {
            return;
        }
        tv.setText(getString(R.string.app_the_window_will_close_xxx_second, String.valueOf(mTotalTime)));
        tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                --mTotalTime;
                if (mTotalTime == 0) {
                    dialogFragment.dismiss();
                } else {
                    countTime(tv, dialogFragment);
                }
            }
        }, 1000);
    }
}
