package com.jakubcervenak.nessnavigation.core;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.ui.MainActivity;
import com.jakubcervenak.nessnavigation.ui.fragments.BaseFragment;

import java.util.LinkedList;
import java.util.NoSuchElementException;


public class FragmentsManager {
    public final static int ANIMATION_OPEN = 0;
    public final static int ANIMATION_SLIDE_LEFT = 1;
    public final static int ANIMATION_SLIDE_RIGHT = 2;
    public final static int ANIMATION_FADE = 3;

    private static Context context;
    private static FragmentsManager instance;

    private static final String MAIN_BACKSTACK = "mainBackStack";
    private static LinkedList<BaseFragment> mainBackStack;


    private FragmentsManager(Context context) {
        FragmentsManager.context = context;
        init();
    }

    public void destroy() {
        instance = null;
    }

    public void clearAndInit(Context context, boolean clearMain) {
        FragmentsManager.context = context;
        if (clearMain) mainBackStack = null;
        init();
    }

    public void init() {
        if (mainBackStack == null)
            mainBackStack = new LinkedList<BaseFragment>();
    }

    public static FragmentsManager getInstance(Context context) {
        if (instance == null)
            instance = new FragmentsManager(context);
        return instance;
    }

    public void switchFragmentInMainBackStack(BaseFragment fragment, boolean clearBackStack, boolean hideParent) {
        if (!((MainActivity) context).isFinishing())
            switchFragment(fragment, clearBackStack, mainBackStack, MAIN_BACKSTACK, R.id.content, hideParent, ANIMATION_OPEN);
    }

    public void switchFragmentInMainBackStack(BaseFragment fragment, boolean clearBackStack, boolean hideParent, int animation) {
        if (!((MainActivity) context).isFinishing())
            switchFragment(fragment, clearBackStack, mainBackStack, MAIN_BACKSTACK, R.id.content, hideParent, animation);
    }

    public int getMainBackStackSize() {
        return mainBackStack.size();
    }


    public void refreshAllFragments() {
        for (Fragment f : mainBackStack) {
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(f);
            fragTransaction.attach(f);
            fragTransaction.commit();
        }
    }

    private FragmentTransaction initTransactionWithAnimation(int anim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (anim) {
            case ANIMATION_OPEN:
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                break;
            case ANIMATION_SLIDE_LEFT:
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_right_out);
                break;
        }
        return transaction;
    }

    private void switchFragment(BaseFragment fragment, boolean clearBackStack, LinkedList<BaseFragment> backStack, String backStackName, int layoutId, boolean hideParent,
                                int anim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment top = null;
        try {
            top = hideParent ? backStack.getLast() : null;
        } catch (NoSuchElementException e) {
        }

        transaction.add(layoutId, fragment)
                .addToBackStack(backStackName)
                .commitAllowingStateLoss();
        if (top != null)
            getSupportFragmentManager().beginTransaction().hide(top).commit();

        if (clearBackStack) {
            transaction = getSupportFragmentManager().beginTransaction();
            for (BaseFragment f : backStack)
                transaction.remove(f);
            backStack.clear();
            transaction.commit();
        }
        backStack.add(fragment);
    }

    public void showTopFragmentInMainBS() {
        showTopBackStackFragment(mainBackStack);
    }


    private void showTopBackStackFragment(LinkedList<BaseFragment> fragmentBackStack) {
        try {
            // fragment on top - remove this fragment
            BaseFragment top = fragmentBackStack.getLast();
            fragmentBackStack.removeLast();

            // second fragment, show this one
            BaseFragment toShow = fragmentBackStack.getLast();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(top)
                    .show(toShow)
                    .commit();

        } catch (NoSuchElementException e) {
            // no element in stack
        }
    }

    public BaseFragment getTopFragmentInMainBackstack() {
        return mainBackStack.getLast();
    }

    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return ((MainActivity) context).getSupportFragmentManager();
    }
}
