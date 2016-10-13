package com.jakubcervenak.nessnavigation.ui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.adapters.MenuAdapter;
import com.jakubcervenak.nessnavigation.core.Constants;
import com.jakubcervenak.nessnavigation.listeners.FragmentSwitcherInterface;
import com.jakubcervenak.nessnavigation.network.RestHelper;
import com.jakubcervenak.nessnavigation.ui.fragments.AboutFragment;
import com.jakubcervenak.nessnavigation.ui.fragments.FloorPlanFragment;
import com.jakubcervenak.nessnavigation.ui.fragments.HomeFragment;
import com.jakubcervenak.nessnavigation.ui.fragments.SplashFragment;
import com.jakubcervenak.nessnavigation.utils.Logger;


public class MainActivity extends BaseActivity implements FragmentSwitcherInterface, Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.initialize(true);
        prepareCache();
        setContentView(R.layout.activity_main);
        setToolbarAndDrawer(savedInstanceState);

        getFragmentsManager().clearAndInit(this, true);
        switchToFragmentAndClear(FRAGMENT_SPLASH, null);
//        Intent intent = new Intent(this,TryActivity.class);
//        startActivity(intent);
    }


    private void setToolbarAndDrawer(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (RelativeLayout) findViewById(R.id.content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        setHamburgerFunction();
        setNavigationView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        RestHelper.getInstance(this).cancelAllRequests();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getContentProvider().releaseLoadedData();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToFragment(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, false);
    }

    @Override
    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, true);
    }

    private void switchToFragment(int fragmentID, Bundle args, boolean clear) {
        switch (fragmentID) {
            case FRAGMENT_SPLASH:
                getFragmentsManager().switchFragmentInMainBackStack(SplashFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_HOME:
                getFragmentsManager().switchFragmentInMainBackStack(HomeFragment.newInstance(args), clear, true);
                break;
//            case FRAGMENT_ABOUT:
//                getFragmentsManager().switchFragmentInMainBackStack(AboutFragment.newInstance(args), clear, true);
//                break;
            case FRAGMENT_FLOOR_PLAN:
                getFragmentsManager().switchFragmentInMainBackStack(FloorPlanFragment.newInstance(args), clear, true);
                break;
        }
    }


    private void setNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(!item.isChecked()) item.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.home:
                        switchToFragmentAndClear(FRAGMENT_HOME,null);
                        return true;
                    case R.id.floorplan:
                        switchToFragmentAndClear(FRAGMENT_FLOOR_PLAN,null);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    protected void setHamburgerFunction() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void toggleDrawerButtonArrow() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setBackButtonFunction();
        toggleDrawerButton(0, 1);
        mDrawerToggle.syncState();
    }

    public void toggleDrawerButtonHamburger() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        setHamburgerFunction();
        toggleDrawerButton(1, 0);
        mDrawerToggle.syncState();
    }

    protected void setBackButtonFunction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopFragmentInMainBS();
                toggleDrawerButtonHamburger();
            }
        });
    }

    private void toggleDrawerButton(float start, float end) {
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(500);
        anim.start();
    }

    public void changeToolbarColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarColor(int color) {
        Window window = getWindow();
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        color = Color.HSVToColor(hsv);
        window.setStatusBarColor(color);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentsManager().getMainBackStackSize() == 1) {
            if (getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed())
                finish();
        } else {
            getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed();
            showTopFragmentInMainBS();
        }
    }
}
