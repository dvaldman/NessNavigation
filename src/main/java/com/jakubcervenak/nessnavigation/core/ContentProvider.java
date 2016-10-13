package com.jakubcervenak.nessnavigation.core;

import android.content.Context;


public class ContentProvider {
    private static ContentProvider instance;
    private static Context context;


    private ContentProvider(Context context) {
        ContentProvider.context = context;
    }

    public static ContentProvider getInstance(Context context) {
        if (instance == null)
            instance = new ContentProvider(context);
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void releaseLoadedData(){

    }
}

