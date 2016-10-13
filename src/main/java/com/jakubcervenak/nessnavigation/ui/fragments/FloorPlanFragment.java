package com.jakubcervenak.nessnavigation.ui.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.customviews.BlueDotView;
import com.jakubcervenak.nessnavigation.navigation.WayPoints;
import com.jakubcervenak.nessnavigation.utils.Logger;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by jakubcervenak on 05/10/16.
 */

public class FloorPlanFragment extends BaseFragment implements WayPoints {

    private IALocationManager locationManager;
    private IALocationListener locationListener;
    private IAResourceManager mResourceManager;
    private BlueDotView mFloorPlanImage;

    private SubsamplingScaleImageView.OnImageEventListener onImageEventListener;

    private ProgressBar progressBar;

    private IATask mPendingAsyncResult;
    private IAFloorPlan mFloorPlan;

    private static final float dotRadius = 0.3f;

    private long mDownloadId;
    private DownloadManager mDownloadManager;
    private String fileName;
    private String filePath;
    private String planId_2_floor = "95b20962-c98b-4e3a-be43-c49fe9e39e0c";
    private String planIdHome = "a54fcd0f-8d4f-4377-b472-8cf24ddfc5e6";




    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;




    public static FloorPlanFragment newInstance(Bundle args){
        FloorPlanFragment fragment = new FloorPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }



    private void initializeLocationAndRegionListener(){
        Logger.i("[FloorPlanFragment]","LocationListener Initialized!");
        locationListener = new IALocationListener() {

            @Override
            public void onLocationChanged(IALocation iaLocation) {
                Logger.e( "location is: " + iaLocation.getLatitude() + "," + iaLocation.getLongitude());
                if (mFloorPlanImage != null && mFloorPlanImage.isReady()) {
                    IALatLng latLng = new IALatLng(iaLocation.getLatitude(), iaLocation.getLongitude());
                    PointF point = mFloorPlan.coordinateToPoint(latLng);
                    PointF mePoint = mFloorPlan.coordinateToPoint(me_point);
                    mFloorPlanImage.setDotCenter(point);
                    mFloorPlanImage.setMePoint(mePoint);
                    mFloorPlanImage.postInvalidate();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

        };

        onImageEventListener = new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResourceManager = IAResourceManager.create(getContext());
        locationManager = IALocationManager.create(getContext());
        mDownloadManager = getDownloadManger();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_floor_plan,container,false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mFloorPlanImage = (BlueDotView) rootView.findViewById(R.id.imageView);
        initializeLocationAndRegionListener();
        fetchFloorPlan(planId_2_floor,2);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ensurePermissions();
        locationManager.requestLocationUpdates(IALocationRequest.create(), locationListener);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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


    private void fetchFloorPlan(String id,int floor) {
        cancelPendingNetworkCalls();

        final IATask<IAFloorPlan> asyncResult = mResourceManager.fetchFloorPlanWithId(id);
        mPendingAsyncResult = asyncResult;
        if(!checkIfFloorPlanIsDownloaded(getCorrectFilePath(floor))) {
            if (mPendingAsyncResult != null) {
                mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                    @Override
                    public void onResult(IAResult<IAFloorPlan> result) {
                        Logger.d("[FloorPlanFragment]", "fetch floor plan result:" + result);

                        if (result.isSuccess() && result.getResult() != null) {
                            mFloorPlan = result.getResult();
                            fileName = mFloorPlan.getId() + ".img";
                            filePath = Environment.getExternalStorageDirectory() + "/"
                                    + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                            File file = new File(filePath);

                            if (!file.exists()) {
                                Logger.d("[FloorPlanFragment]","Downloading Map");
                                DownloadManager.Request request =
                                        new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
                                request.setDescription("IndoorAtlas floor plan");
                                request.setTitle("Floor plan");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.
                                            Request.VISIBILITY_VISIBLE);
                                }
                                request.setDestinationInExternalPublicDir(Environment.
                                        DIRECTORY_DOWNLOADS, fileName);

                                mDownloadId = mDownloadManager.enqueue(request);
                            } else {
                                Logger.d("[FloorPlanFragment]","Loading map from Downloads");
                                showFloorPlanImage(filePath);
                            }
                        } else {
                            if (!asyncResult.isCancelled()) {
                                Logger.e("[FloorPlanFragment]",
                                        (result.getError() != null
                                                ? "error loading floor plan: " + result.getError()
                                                : "access to floor plan denied"));
                            }
                        }
                    }
                }, Looper.getMainLooper()); // deliver callbacks in main thread
            }
        }
        else{
            Logger.e("[FetchFloorPlan]","Loaded from external storage");
            showFloorPlanImage(filePath);
        }
    }

    private void cancelPendingNetworkCalls() {
        if (mPendingAsyncResult != null && !mPendingAsyncResult.isCancelled()) {
            mPendingAsyncResult.cancel();
        }
    }

    private boolean checkIfFloorPlanIsDownloaded(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            return true;
        }
        return false;
    }

    private String getCorrectFilePath(int floor){
        String firstFloor = filePath;
        String secondFloor = filePath + planId_2_floor + ".img";
        filePath = Environment.getExternalStorageDirectory() + "/"
                + Environment.DIRECTORY_DOWNLOADS + "/";
        switch(floor){
            case 1:
                return null;
            case 2:
                return secondFloor;
            default: return secondFloor;
        }
    }



    private void showFloorPlanImage(String filePath) {
        Logger.d( "showFloorPlanImage: " + filePath);
        mFloorPlanImage.setRadius(mFloorPlan.getMetersToPixels() * dotRadius);
        mFloorPlanImage.setImage(ImageSource.uri(filePath));
        mFloorPlanImage.setOnImageEventListener(onImageEventListener);

    }

    @Override
    public void onPause() {
        locationManager.removeLocationUpdates(locationListener);
        getActivity().unregisterReceiver(onComplete);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        locationManager.destroy();
        super.onDestroy();
    }

    private void ensurePermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length == 0
                        || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Logger.e("[FloorPlanFragment]","External Storage Write Deny!");
                }
                break;
        }

    }

    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            if (id != mDownloadId) {
                Logger.e( "Ignore unrelated download");
                return;
            }
            Logger.e( "Image download completed");
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = mDownloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    String filePath = c.getString(c.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_FILENAME));
                    showFloorPlanImage(filePath);
                }
            }
            c.close();
        }
    };
}
