package com.mark.streamproject.categeory;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryPresenter implements  CategoryContract.Presenter {

    private final CategoryContract.View mCategoryView;

    public CategoryPresenter(@NonNull CategoryContract.View categoryView) {
        mCategoryView = checkNotNull(categoryView, "hotsView cannot be null!");
        mCategoryView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
