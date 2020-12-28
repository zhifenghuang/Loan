package com.elephant.loan.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.RealInfoBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.view.WheelView;
import com.elephant.loan.R;
import com.elephant.loan.contract.CollectionBankCardContract;
import com.elephant.loan.presenter.CollectionBankCardPresenter;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;

public class CollectionBankCardActivity extends BaseActivity<CollectionBankCardContract.Presenter> implements CollectionBankCardContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection_bank_card;
    }

    @Override
    protected void updateUIText() {

    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tv_title, R.string.app_bank_card);
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getBank_id_card())) {
            initInputListener();
            setViewsOnClickListener(R.id.tvSubmit, R.id.llBank);
        } else {
            setViewGone(R.id.etOwnerName, R.id.etCerNo, R.id.tvBankName, R.id.etBankCardNo);
            setViewVisible(R.id.tvOwnerName, R.id.tvCerNo, R.id.tvBankName2, R.id.tvBankCardNo);
            setText(R.id.tvOwnerName, realInfoBean.getBank_user());
            setText(R.id.tvCerNo, realInfoBean.getBank_id_card());
            setText(R.id.tvBankName2, realInfoBean.getBank_name());
            setText(R.id.tvBankCardNo, realInfoBean.getBank_card());
            TextView tvSubmit = findViewById(R.id.tvSubmit);
            tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
            tvSubmit.setEnabled(false);
            tvSubmit.setText(getString(R.string.app_had_submit));
        }
    }

    @NotNull
    @Override
    protected CollectionBankCardContract.Presenter onCreatePresenter() {
        return new CollectionBankCardPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBank:
                showSelectBankDialog();
                break;
            case R.id.tvSubmit:
                String cerNo = getTextById(R.id.etCerNo);
                if (cerNo.length() != 13) {
                    showToast(R.string.app_please_input_cer_no);
                    return;
                }
                String bankCard = getTextById(R.id.etBankCardNo);
                if (bankCard.length() != 10 && bankCard.length() != 16) {
                    showToast(R.string.app_please_input_card_no);
                    return;
                }
                getPresenter().uploadBankInfo(getTextById(R.id.etOwnerName), cerNo, getTextById(R.id.tvBankName), bankCard);
                break;
        }
    }

    private void showSelectBankDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.dialog_select_one);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                TextView tvTitle = view.findViewById(R.id.tvTitle);
                tvTitle.setText(getString(R.string.app_select_bank));
                WheelView wheelView = view.findViewById(R.id.wheelView);
                ArrayList<String> list = new ArrayList<>();
                ArrayList<Bitmap> iconList = new ArrayList<>();
                for (int i = 0; i < 24; ++i) {
                    iconList.add(((BitmapDrawable) getDrawable(getResources().getIdentifier("app_bank_" + i, "drawable", getPackageName()))).getBitmap());
                    list.add(getString(getResources().getIdentifier("app_bank_" + i, "string", getPackageName())));
                }
                wheelView.setIcons(iconList);
                wheelView.setData(list);
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.tvOk);
            }

            @Override
            public void onViewClick(int viewId) {
                if (viewId == R.id.tvOk) {
                    WheelView wheelView = dialogFragment.getView().findViewById(R.id.wheelView);
                    setText(R.id.tvBankName, wheelView.getSelectedText());
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    @Override
    public void uploadSuccess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
        RealInfoBean bean = DataManager.Companion.getInstance().getMyInfo();
        bean.setBank_user(getTextById(R.id.etOwnerName));
        bean.setBank_id_card(getTextById(R.id.etCerNo));
        bean.setBank_name(getTextById(R.id.tvBankName));
        bean.setBank_card(getTextById(R.id.etBankCardNo));
        DataManager.Companion.getInstance().saveMyInfo(bean);
        finish();
    }

    @Override
    public void onBackClick(View view) {
        RealInfoBean realInfoBean = DataManager.Companion.getInstance().getMyInfo();
        if (TextUtils.isEmpty(realInfoBean.getBank_id_card())) {
            showTwoBtnDialog(null,
                    getString(R.string.app_sure_back),
                    ContextCompat.getColor(this, R.color.color_0d_0d_0d),
                    getString(R.string.app_cancel),
                    getString(R.string.app_ok),
                    new OnClickCallback() {
                        @Override
                        public void onClick(int viewId) {
                            if (viewId == R.id.btn2) {
                                finish();
                            }
                        }
                    });
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackClick(null);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void uploadFailed(String msg) {
        showUploadFailedDialog();
    }

    private int mTotalTime;

    private void showUploadFailedDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_loan_status_dialog);
        dialogFragment.setOutClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((ImageView) view.findViewById(R.id.ivStatus)).setImageResource(R.drawable.jd_home_t_zhu);
                ((TextView) view.findViewById(R.id.tvStatus)).setText(getString(R.string.app_failed));
                ((TextView) view.findViewById(R.id.tvTip)).setText(getString(R.string.app_failed_reason));
                mTotalTime = 3;
                countTime(view.findViewById(R.id.tvCloseTip), dialogFragment);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    private void countTime(final TextView tv, final MyDialogFragment dialogFragment) {
        if (isDestroyed()) {
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

    private void initInputListener() {
        final TextView tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
        tvSubmit.setEnabled(false);
        final EditText etOwnerName = findViewById(R.id.etOwnerName);
        final EditText etCerNo = findViewById(R.id.etCerNo);
        final TextView tvBankName = findViewById(R.id.tvBankName);
        final EditText etBankCardNo = findViewById(R.id.etBankCardNo);
        Observable.combineLatest(RxTextView.textChanges(etOwnerName).skip(1),
                RxTextView.textChanges(etCerNo).skip(1),
                RxTextView.textChanges(tvBankName).skip(1),
                RxTextView.textChanges(etBankCardNo).skip(1),
                new Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3, @NonNull CharSequence charSequence4) throws Exception {
                        return !TextUtils.isEmpty(getTextById(R.id.etOwnerName))
                                && !TextUtils.isEmpty(getTextById(R.id.etCerNo))
                                && !TextUtils.isEmpty(getTextById(R.id.tvBankName))
                                && !TextUtils.isEmpty(getTextById(R.id.etBankCardNo));
                    }

                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_4968ea_25);
                    tvSubmit.setEnabled(true);
                } else {
                    tvSubmit.setBackgroundResource(R.drawable.app_shape_879cf5_25);
                    tvSubmit.setEnabled(false);
                }
            }
        });
    }
}
