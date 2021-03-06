package com.elephant.loan.contract;

import com.common.lib.bean.ArticleBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public class ContractContract {
    public interface View extends IView {
        public void getContractSuccess(ArrayList<ArticleBean> list);
    }

    public interface Presenter extends IPresenter {
        public void getContract(int type);
    }
}
