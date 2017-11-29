package com.testmenudrawer.android.testmenudrawer.utilities;

import android.content.Context;

import android.content.AsyncTaskLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvalencia on 11/28/17.
 */

//public static class VinListAsyncTaskLoader extends
public class VinListAsyncTaskLoader extends
        AsyncTaskLoader<String> {
    // You probably have something more complicated
    // than just a String. Roll with me
//    private List<String> mData;
    private String mData;
    public VinListAsyncTaskLoader(Context context) {
        super(context);
    }
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
    @Override
    public String loadInBackground() {
        // This is on a background thread
        // Good to know: the Context returned by getContext()
        // is the application context
//        File jsonFile = new File(
//                getContext().getFilesDir(), "downloaded.json");
//        List<String> data = new ArrayList<>();

        String data = "This data come from the loader";
        // Parse the JSON using the library of your choice
        // Check isLoadInBackgroundCanceled() to cancel out early
        return data;
    }
    @Override
    public void deliverResult(String data) {
        // We’ll save the data for later retrieval
        mData = data;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(data);
    }
}