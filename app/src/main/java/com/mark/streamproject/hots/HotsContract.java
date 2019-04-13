package com.mark.streamproject.hots;

import com.mark.streamproject.base.BasePresenter;
import com.mark.streamproject.base.BaseView;

/**
 * Created by Mark Ho on April 2019.
 *
 * This specifies the contract between the view and the presenter.
 */
public interface HotsContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void showStreamDialog();

    }
}
