package com.elephant.loan.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class MyLoanContract {
    public interface View extends IView {
        public void getBannerSuccess(String banner);
    }

    public interface Presenter extends IPresenter {
        public void banner();
    }
}
