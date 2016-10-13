package com.jakubcervenak.nessnavigation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakubcervenak.nessnavigation.R;


/**
 * Created by V3502505 on 20/09/2016.
 */
public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance(Bundle args){
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,container,false);
        setActionBar();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        showActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toggleDrawerButtonHamburger();
    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void setViewContents() {

    }
}
