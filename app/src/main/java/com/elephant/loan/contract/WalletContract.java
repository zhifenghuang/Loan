package com.elephant.loan.contract;

import com.common.lib.bean.WithdrawDetailBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public class WalletContract {
    public interface View extends IView {
        public void withDrawSuccess(String msg);

        public void withDrawFailed(String msg);

        public void getWithDrawDetailSuccess(ArrayList<WithdrawDetailBean> list);
    }

    public interface Presenter extends IPresenter {
        public void withDraw(String pas);

        public void getWithDrawDetail();
    }
}
