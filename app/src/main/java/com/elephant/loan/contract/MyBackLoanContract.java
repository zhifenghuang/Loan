package com.elephant.loan.contract;

import com.common.lib.bean.RepayBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public class MyBackLoanContract {
    public interface View extends IView {
        public void getLoanRepaySuccess(ArrayList<RepayBean> list);
    }

    public interface Presenter extends IPresenter {
        public void loanRepay();
    }
}
