package com.elephant.loan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.elephant.loan.R;
import com.elephant.loan.contract.MainContract;
import com.elephant.loan.fragment.MineFragment;
import com.elephant.loan.fragment.MyLoanFragment;
import com.elephant.loan.fragment.ServiceFragment;
import com.elephant.loan.fragment.WalletFragment;
import com.elephant.loan.presenter.MainPresenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    private ArrayList<BaseFragment> mBaseFragment;
    private int mCurrentItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        initFragments();
        initViews();
        switchFragment(mBaseFragment.get(0));
        resetBottomBar(0);
    }

    private void initFragments() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new MyLoanFragment());
        mBaseFragment.add(new WalletFragment());
        mBaseFragment.add(new ServiceFragment());
        mBaseFragment.add(new MineFragment());
    }

    @NotNull
    @Override
    protected MainContract.Presenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    private void initViews() {
        LinearLayout llBottom = findViewById(R.id.llBottom);
        int count = llBottom.getChildCount();
        View itemView;
        for (int i = 0; i < count; ++i) {
            itemView = llBottom.getChildAt(i);
            itemView.setTag(i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == mCurrentItem) {
                        return;
                    }
                    switchFragment(mBaseFragment.get(tag));
                    resetBottomBar(tag);
                }
            });
        }
    }

    @Override
    protected void updateUIText() {
        getPresenter().getRealInfo();
        getBalance();
    }

    public void getBalance() {
        getPresenter().balance();
    }

    public void toWalletFragment() {
        switchFragment(mBaseFragment.get(1));
        resetBottomBar(1);
    }

    public void toServiceFragment() {
        switchFragment(mBaseFragment.get(2));
        resetBottomBar(2);
    }

    private void resetBottomBar(int currentPos) {
        mCurrentItem = currentPos;
        LinearLayout llBottom = findViewById(R.id.llBottom);
        int count = llBottom.getChildCount();
        ViewGroup itemView;
        for (int i = 0; i < count; ++i) {
            itemView = (ViewGroup) llBottom.getChildAt(i);
            (((ImageView) itemView.getChildAt(0))).setImageResource(getResIdByIndex(i, currentPos == i));
            (((TextView) itemView.getChildAt(1))).
                    setTextColor(ContextCompat.getColor(this, currentPos == i ? R.color.color_49_68_ea : R.color.color_a0_a7_bc));
        }
    }

    private int getResIdByIndex(int index, boolean isCheck) {
        int id = 0;
        switch (index) {
            case 0:
                id = isCheck ? R.drawable.jd_home_m_loan : R.drawable.jd_home_x_loan;
                break;
            case 1:
                id = isCheck ? R.drawable.jd_home_m_qianbao : R.drawable.jd_home_x_qianbao;
                break;
            case 2:
                id = isCheck ? R.drawable.jd_home_m_kefu : R.drawable.jd_home_x_kefu;
                break;
            case 3:
                id = isCheck ? R.drawable.jd_home_m_pro : R.drawable.jd_home_x_pro;
                break;
        }
        return id;
    }

    @Override
    public int getContainerViewId() {
        return R.id.fl;
    }

    @Override
    public void getRealInfoSuccess(RealInfoBean bean) {

    }
}
