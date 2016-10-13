package com.jakubcervenak.nessnavigation.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.listeners.FragmentSwitcherInterface;
import com.jakubcervenak.nessnavigation.utils.Logger;


/**
 * Created by V3502505 on 20/09/2016.
 */
public class SplashFragment extends BaseFragment {

    public static SplashFragment newInstance(Bundle args) {
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        // getContentProvider().releaseAllLoadedData();
        setActionBar();
        setViews();
        setViewContents();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                switchToFragment(FRAGMENT_HOME,null);
//                Logger.i("[SplashFragment]","Runnable!!!!");
//            }
//        },2000);
        switchToFragment(FRAGMENT_HOME,null);

        return rootView;
    }


    @Override
    protected void setActionBar() {
        hideActionBar();
    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void setViewContents() {

    }
}
