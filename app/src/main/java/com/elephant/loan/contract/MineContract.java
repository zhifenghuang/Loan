package com.elephant.loan.contract;

import com.common.lib.bean.RealInfoBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public class MineContract {
    public interface View extends IView {
        public void getServiceSuccess(String serviceUrl);
    }

    public interface Presenter extends IPresenter {
        public void getServiceUrl();
    }
}
