package com.jakubcervenak.nessnavigation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.jakubcervenak.nessnavigation.R;

/**
 * Created by jakubcervenak on 06/10/16.
 */

public class TryActivity extends BaseActivity {

    IALocationManager locationManager;

    IALocationListener locationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            //TextView textLatitude = (TextView) findViewById(R.id.longitude);
            //TextView textLongitude = (TextView) findViewById(R.id.latitude);
            //textLatitude.setText(String.valueOf(iaLocation.getLatitude()));
            //textLongitude.setText(String.valueOf(iaLocation.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_floor_plan);

        locationManager = IALocationManager.create(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(IALocationRequest.create(),locationListener);
    }

    @Override
    protected void onPause() {
        locationManager.removeLocationUpdates(locationListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        locationManager.destroy();
        super.onDestroy();
    }
}
