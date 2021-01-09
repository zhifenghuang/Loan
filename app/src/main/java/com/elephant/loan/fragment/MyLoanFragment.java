package com.elephant.loan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.bean.LoanInfoBean;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.constant.Constants;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.utils.LogUtil;
import com.elephant.loan.R;
import com.elephant.loan.activity.ContractActivity;
import com.elephant.loan.activity.RealNameVerifyActivity;
import com.elephant.loan.contract.MyLoanContract;
import com.elephant.loan.presenter.MyLoanPresenter;
import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyLoanFragment extends BaseFragment<MyLoanContract.Presenter> implements MyLoanContract.View {

    private ArrayList<HashMap<String, String>> mLoanInfo;

    private String mMonth;
    private String mRange;
    private String mRate, mLoanValue;

    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        private WeakReference<MyLoanFragment> mFragmentWeakReference;

        public MyHandler(MyLoanFragment fragment) {
            this.mFragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MyLoanFragment fragment = mFragmentWeakReference.get();
                    if (fragment != null && fragment.getView() != null && fragment.mListBanner == null) {
                        fragment.getPresenter().banner();
                        sendEmptyMessageDelayed(0, 5 * 1000);
                    }
                    break;
            }
        }
    }

    ;

    @NotNull
    @Override
    protected MyLoanContract.Presenter onCreatePresenter() {
        return new MyLoanPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_loan;
    }

    @Override
    protected void initView(@NotNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        mLoanInfo = DataManager.Companion.getInstance().getLoanInfo();
        setViewsOnClickListener(R.id.tvApplyNow, R.id.tvRange1, R.id.tvRange2, R.id.tvRange3, R.id.tvRange4,
                R.id.tvRange5, R.id.tvRange6, R.id.tvTime1, R.id.tvTime2, R.id.tvTime3, R.id.tvTime4);
        mMonth = "12";
        mRate = "0.5%";
        mRange = getString(R.string.app_money_0);
        mLoanValue = "45000";
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessage(0);
        resetView();
        getPresenter().getLoanInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRange1:
            case R.id.tvRange2:
            case R.id.tvRange3:
            case R.id.tvRange4:
            case R.id.tvRange5:
            case R.id.tvRange6:
                if (mLoanInfo == null) {
                    getPresenter().getLoanInfo();
                    return;
                }
                mLoanValue = (String) v.getTag();
                mRange = (String) v.getTag(R.id.tag);
                mRange = mRange.replaceAll("\n", "");
                setText(R.id.tvMoney, mRange);
                resetTabs((TextView) v, getView().findViewById(R.id.tvRange1), getView().findViewById(R.id.tvRange2), getView().findViewById(R.id.tvRange3),
                        getView().findViewById(R.id.tvRange4), getView().findViewById(R.id.tvRange5), getView().findViewById(R.id.tvRange6));
                break;
            case R.id.tvTime1:
            case R.id.tvTime2:
            case R.id.tvTime3:
            case R.id.tvTime4:
                if (mLoanInfo == null) {
                    getPresenter().getLoanInfo();
                    return;
                }
                mMonth = (String) v.getTag();
                setText(R.id.tvMonth, mMonth);
                resetTabs((TextView) v, getView().findViewById(R.id.tvTime1), getView().findViewById(R.id.tvTime2), getView().findViewById(R.id.tvTime3),
                        getView().findViewById(R.id.tvTime4));

                break;
            case R.id.tvApplyNow:
                RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
                if (TextUtils.isEmpty(realInfoBean.getName())
                        || TextUtils.isEmpty(realInfoBean.getEducation())
                        || TextUtils.isEmpty(realInfoBean.getBank_id_card())
                        || TextUtils.isEmpty(realInfoBean.getPhone())
                        || TextUtils.isEmpty(realInfoBean.getSign())) {
                    showVerifyRealNameDialog();
                    return;
                }
                showSureLoanDialog(realInfoBean);
                break;
        }
    }

    private void showVerifyRealNameDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_remind_real_name_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.btn);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.btn:
                        openActivity(RealNameVerifyActivity.class);
                        break;
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    private void showSureLoanDialog(final RealInfoBean realInfoBean) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_info_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                dialogFragment.setClickDismiss(false);
                TextView tv = view.findViewById(R.id.tvLoanInfo);
                tv.setText(getString(R.string.app_loan_user_info,
                        realInfoBean.getName(),
                        realInfoBean.getId_card(),
                        realInfoBean.getPhone(),
                        mRange,
                        mMonth + getString(R.string.app_sum_month),
                        mRate));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.tvLookContract, R.id.btn);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.ivClose:
                        dialogFragment.dismiss();
                        break;
                    case R.id.tvLookContract:
                        LoanInfoBean bean = new LoanInfoBean();
                        bean.setAmount(mLoanValue);
                        bean.setAmount_class(mRange);
                        bean.setTerm(mMonth);
                        bean.setOrder_id("");
                        bean.setCreated_at(BaseUtils.StaticParams.longToDate2(System.currentTimeMillis()));
                        bean.setRate(mRate.substring(mRate.length() - 1));
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_EXTRA, 100);
                        bundle.putSerializable(Constants.BUNDLE_EXTRA_2, bean);
                        openActivity(ContractActivity.class, bundle);
                        break;
                    case R.id.btn:
                        getPresenter().applyLoan(mLoanValue, mRange, mMonth);
                        dialogFragment.dismiss();
                        break;
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    @Override
    protected void updateUIText() {
    }

    public ArrayList<String> mListBanner;

    @Override
    public void getBannerSuccess(ArrayList<String> banners) {
        if (getView() == null || banners == null || banners.isEmpty()) {
            return;
        }
        mListBanner = banners;
        LogUtil.LogE("size: " + banners.size());
        MarqueeView marqueeView = getView().findViewById(R.id.marqueeView);
        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(getActivity(), mListBanner);
        marqueeView.setAdapter(simpleTextAdapter);
        marqueeView.startFlip();
    }


    @Override
    public void getLoanInfoSuccess(ArrayList<HashMap<String, String>> list) {
        mLoanInfo = list;
        resetView();
    }

    @Override
    public void applyLoanSuccess(String msg) {
        showApplyLoanResultDialog(true, msg);
    }

    @Override
    public void applyLoanFailed(String msg) {
        showApplyLoanResultDialog(false, msg);
    }

    private int mTotalTime;

    private void showApplyLoanResultDialog(final boolean isSuccess, final String msg) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                if (!isSuccess) {
                    ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_t_fail);
                    ((TextView) view.findViewById(R.id.tvStatus)).setText(getString(R.string.app_sorry));
                    ((TextView) view.findViewById(R.id.tvTip)).setText(msg == null ? "" : msg);
                }
                mTotalTime = 5;
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


    private void resetView() {
        if (getView() == null || mLoanInfo == null) {
            return;
        }
        String key, value;
        TextView tv;
        for (HashMap<String, String> map : mLoanInfo) {
            try {
                key = map.get("key");
                value = map.get("value");
                if (key.equals("loan_rate")) {
                    mRate = Integer.parseInt(value) * 0.01 + "%";
                    setText(R.id.tvRate, mRate);
                } else if (key.equals("loan_term") && !TextUtils.isEmpty(value)) {
                    String[] terms = value.split(",");
                    for (int i = 0; i < 4; ++i) {
                        if (i == 0) {
                            mMonth = terms[i];
                        }
                        tv = getView().findViewById(getResources().getIdentifier("tvTime" + (i + 1), "id", getActivity().getPackageName()));
                        if (i < terms.length) {
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(terms[i] + getString(R.string.app_sum_month));
                            tv.setTag(terms[i]);
                        } else {
                            tv.setVisibility(View.INVISIBLE);
                        }
                    }
                } else if (key.equals("loan_class_1")) {
                    mLoanValue = value;
                    getView().findViewById(R.id.tvRange1).setTag(value);
                    getView().findViewById(R.id.tvRange1).setTag(R.id.tag, getString(R.string.app_money_0));
                } else if (key.equals("loan_class_2")) {
                    getView().findViewById(R.id.tvRange2).setTag(value);
                    getView().findViewById(R.id.tvRange2).setTag(R.id.tag, getString(R.string.app_money_1));
                } else if (key.equals("loan_class_3")) {
                    getView().findViewById(R.id.tvRange3).setTag(value);
                    getView().findViewById(R.id.tvRange3).setTag(R.id.tag, getString(R.string.app_money_2));
                } else if (key.equals("loan_class_4")) {
                    getView().findViewById(R.id.tvRange4).setTag(value);
                    getView().findViewById(R.id.tvRange4).setTag(R.id.tag, getString(R.string.app_money_3));
                } else if (key.equals("loan_class_5")) {
                    getView().findViewById(R.id.tvRange5).setTag(value);
                    getView().findViewById(R.id.tvRange5).setTag(R.id.tag, getString(R.string.app_money_4));
                } else if (key.equals("loan_class_6")) {
                    getView().findViewById(R.id.tvRange6).setTag(value);
                    getView().findViewById(R.id.tvRange6).setTag(R.id.tag, getString(R.string.app_money_5));
                }
            } catch (Exception e) {

            }
        }
    }

    private void resetTabs(TextView... tvs) {
        for (int i = 1; i < tvs.length; ++i) {
            tvs[i].setBackgroundResource(R.drawable.app_shape_ffffff_4);
            tvs[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.color_8b_8b_8b));
        }
        tvs[0].setBackgroundResource(R.drawable.app_shape_4968ea_4);
        tvs[0].setTextColor(ContextCompat.getColor(getActivity(), R.color.color_ff_ff_ff));
    }

    @Override
    public void onDestroyView() {
        MarqueeView marqueeView = getView().findViewById(R.id.marqueeView);
        marqueeView.stopFilp();
        super.onDestroyView();
        // 外部类Activity生命周期结束时，同时清空消息队列 和 结束Handler生命周期
        myHandler.removeCallbacksAndMessages(null);
    }


    static class SimpleTextAdapter extends CommonAdapter<String> {

        public SimpleTextAdapter(Context context, List<String> datas) {
            super(context, R.layout.item_banner, datas);
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, int position) {
            TextView tv = viewHolder.getView(R.id.tvBanner);
            tv.setText(item);
        }

    }
}
