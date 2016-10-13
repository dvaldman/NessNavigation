package com.jakubcervenak.nessnavigation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakubcervenak.nessnavigation.R;


public class AboutFragment extends BaseFragment {

    public static AboutFragment newInstance(Bundle args) {
        AboutFragment fragment = new AboutFragment();
        if (args != null) fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        setActionBar();
        setViews();
        return rootView;
    }

    @Override
    protected void setActionBar() {
    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void setViewContents() {

    }
}