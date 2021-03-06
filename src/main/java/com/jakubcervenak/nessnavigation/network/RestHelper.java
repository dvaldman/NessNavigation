package com.jakubcervenak.nessnavigation.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jakubcervenak.nessnavigation.utils.Utility;


/**
 * Created by jakubcervenak on 16/09/16.
 */
public class RestHelper {
    public static final String TAG = RestHelper.class.getSimpleName();
    private static RestHelper mInstance = null;
    private RequestQueue mRequestQueue = null;
    private static Context mContext;

    private RestHelper(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RestHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RestHelper(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        if (Utility.isOnline(mContext))
            getRequestQueue().add(request);
        else {
            request.getErrorListener().onErrorResponse(null);
        }
    }

    public void cancelAllRequests() {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(TAG);
    }
}
