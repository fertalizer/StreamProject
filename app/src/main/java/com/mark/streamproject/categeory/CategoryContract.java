package com.mark.streamproject.categeory;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface CategoryContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}