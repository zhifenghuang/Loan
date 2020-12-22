package com.elephant.loan.contract;

import com.common.lib.bean.LoanInfoBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public class MyLoanActivityContract {
    public interface View extends IView {
        public void getMyLoanSuccess(ArrayList<LoanInfoBean> list);
    }

    public interface Presenter extends IPresenter {

        public void getMyLoan();
    }
}
