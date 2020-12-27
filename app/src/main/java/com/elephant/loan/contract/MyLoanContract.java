package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyLoanContract {
    public interface View extends IView {
        public void getBannerSuccess(String banner);

        public void getLoanInfoSuccess(ArrayList<HashMap<String, String>> list);

        public void applyLoanSuccess(String msg);

        public void applyLoanFailed(String msg);
    }

    public interface Presenter extends IPresenter {
        public void banner();

        public void getLoanInfo();

        public void applyLoan(String amount, String a_class, String term);
    }
}
