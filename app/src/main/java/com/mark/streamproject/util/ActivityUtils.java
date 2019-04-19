package com.mark.streamproject.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mark.streamproject.MainMvpController;
import com.mark.streamproject.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Wayne Chen on Feb. 2019.
 *
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void showOrAddFragmentByTag(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment,
                                              @MainMvpController.FragmentType String fragmentTag) {
        checkNotNull(fragmentManager);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_main_container, fragment, fragmentTag);
        }

        transaction.commit();
    }

    public static void addFragmentByTag(@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment,
                                        @MainMvpController.FragmentType String fragmentTag) {
        checkNotNull(fragmentManager);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                transaction.addToBackStack(null);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_main_container, fragment, fragmentTag);
        }

        transaction.commit();
    }
}