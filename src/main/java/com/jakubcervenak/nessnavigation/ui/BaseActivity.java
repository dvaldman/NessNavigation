package com.jakubcervenak.nessnavigation.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.adapters.MenuAdapter;
import com.jakubcervenak.nessnavigation.core.ApplicationStorage;
import com.jakubcervenak.nessnavigation.core.ContentProvider;
import com.jakubcervenak.nessnavigation.core.FragmentsManager;
import com.jakubcervenak.nessnavigation.listeners.DialogOkClickListener;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected RelativeLayout content;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private static ProgressDialog progress;
    private AlertDialog alertDialog;
    protected NavigationView navigationView;

    private String filePath;


    protected File extStorageAppBasePath;
    protected File extStorageAppCachePath;

    public void showErrorDialog(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogWindow(int message) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogWithAction(int message, final DialogOkClickListener listener, boolean showNegativeButton) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(showNegativeButton ? R.string.positive_btn : R.string.ok_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onPositiveButtonClicked(BaseActivity.this);
                    }
                });

        if (showNegativeButton)
            builder.setNegativeButton(R.string.negative_btn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this);
    }

//    public DBHelper getDBHelper() {
//        return DBHelper.getInstance(this);
//    }

    public ApplicationStorage getAppStorage() {
        return ApplicationStorage.getInstance(this);
    }

    public FragmentsManager getFragmentsManager() {
        return FragmentsManager.getInstance(this);
    }

    public void showTopFragmentInMainBS() {
        getFragmentsManager().showTopFragmentInMainBS();
    }

    public void showProgressDialog(int title, int message) {
        if (progress != null)
            hideProgressDialog();

        progress = ProgressDialog.show(this, null,
                getResources().getString(message), true);
    }

    public void showProgressDialog(String message) {
        if (progress != null)
            hideProgressDialog();

        progress = ProgressDialog.show(this, null, message, true);
    }

    public void hideProgressDialog() {
        if (progress != null)
            progress.dismiss();
        progress = null;
    }

    public void disableGestures() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableGestures() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideActionBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void showActionBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }




    public String getCachePath() {
        if (extStorageAppCachePath == null)
            return getCacheDir().getPath();
        return extStorageAppCachePath.getPath();
    }

    public void prepareCache() {
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Retrieve the base path for the application in the external storage
            File externalStorageDir = Environment.getExternalStorageDirectory();
            if (externalStorageDir != null) {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd
                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
                        File.separator + "Android" + File.separator + "data" +
                        File.separator + getPackageName());
            }

            if (extStorageAppBasePath != null) {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd/cache
                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() + File.separator + "cache");
                boolean isCachePathAvailable = true;
                if (!extStorageAppCachePath.exists())
                    isCachePathAvailable = extStorageAppCachePath.mkdirs();

                if (!isCachePathAvailable)
                    extStorageAppCachePath = null;
            }
        }
    }

    @Override
    public File getCacheDir() {
        if (extStorageAppCachePath == null)
            return super.getCacheDir();
        return extStorageAppCachePath;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
