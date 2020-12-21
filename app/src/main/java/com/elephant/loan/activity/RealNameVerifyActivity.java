package com.elephant.loan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;
import com.elephant.loan.contract.RealNameContract;
import com.elephant.loan.presenter.RealNamePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class RealNameVerifyActivity extends BaseActivity<RealNameContract.Presenter> implements RealNameContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name_verify;
    }

    @Override
    protected void updateUIText() {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getName())) {
            setViewGone(R.id.tvIdComplete);
            setViewVisible(R.id.tvGoBlankId);
        } else {
            setViewGone(R.id.tvGoBlankId);
            setViewVisible(R.id.tvIdComplete);
        }
        if (TextUtils.isEmpty(realInfoBean.getEducation())) {
            setViewGone(R.id.tvDataComplete);
            setViewVisible(R.id.tvGoBlankData);
        } else {
            setViewGone(R.id.tvGoBlankData);
            setViewVisible(R.id.tvDataComplete);
        }
        if (TextUtils.isEmpty(realInfoBean.getBank_id_card())) {
            setViewGone(R.id.tvBankCardComplete);
            setViewVisible(R.id.tvGoBlankBankCard);
        } else {
            setViewGone(R.id.tvGoBlankBankCard);
            setViewVisible(R.id.tvBankCardComplete);
        }
        if (TextUtils.isEmpty(realInfoBean.getPhone())) {
            setViewGone(R.id.tvPhoneVerComplete);
            setViewVisible(R.id.tvGoBlankPhoneVer);
        } else {
            setViewGone(R.id.tvGoBlankPhoneVer);
            setViewVisible(R.id.tvPhoneVerComplete);
        }
        if (TextUtils.isEmpty(realInfoBean.getSign())) {
            setViewGone(R.id.tvHandComplete);
            setViewVisible(R.id.tvGoBlankHand);
        } else {
            setViewGone(R.id.tvGoBlankHand);
            setViewVisible(R.id.tvHandComplete);
        }
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_real_name_verify);
        setViewsOnClickListener(R.id.cvIdentity, R.id.cvData, R.id.cvBankCard, R.id.cvPhoneVer, R.id.cvHandMark);
    }

    @NotNull
    @Override
    protected RealNameContract.Presenter onCreatePresenter() {
        return new RealNamePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvIdentity:
                openActivity(IdentityInfoActivity.class);
                break;
            case R.id.cvData:
                openActivity(DataInfoActivity.class);
                break;
            case R.id.cvBankCard:
                openActivity(CollectionBankCardActivity.class);
                break;
            case R.id.cvPhoneVer:
                openActivity(PhoneAuthActivity.class);
                break;
            case R.id.cvHandMark:
                openActivity(HandWriteSignatureActivity.class);
                break;
        }
    }

    @Override
    public void getRealInfoSuccess(RealInfoBean bean) {
        updateUIText();
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (map != null) {
            try {
                if (map.containsKey(EventBusEvent.REFRESH_INFO)) {
                    getPresenter().getRealInfo();
                }
            } catch (Exception e) {

            }
        }
    }
}
