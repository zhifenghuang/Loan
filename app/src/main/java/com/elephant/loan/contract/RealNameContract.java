package com.elephant.loan.contract;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class RealNameContract {
    public interface View extends IView {
        public void getRealInfoSuccess(RealInfoBean bean);
    }

    public interface Presenter extends IPresenter {
        public void getRealInfo();
    }
}
