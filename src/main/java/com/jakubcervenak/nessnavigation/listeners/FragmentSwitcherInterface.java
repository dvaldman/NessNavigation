package com.jakubcervenak.nessnavigation.listeners;

import android.os.Bundle;

public interface FragmentSwitcherInterface {

    int FRAGMENT_SPLASH = 0;
    int FRAGMENT_HOME = 1;
//    int FRAGMENT_ABOUT = 2;
    int FRAGMENT_FLOOR_PLAN = 2;


    public void switchToFragment(int fragmentID, Bundle args);

    public void switchToFragmentAndClear(int fragmentID, Bundle args);
}
