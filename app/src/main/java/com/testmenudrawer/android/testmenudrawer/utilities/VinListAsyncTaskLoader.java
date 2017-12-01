package com.testmenudrawer.android.testmenudrawer.utilities;

import android.content.Context;

import android.content.AsyncTaskLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.testmenudrawer.android.testmenudrawer.R;
import com.testmenudrawer.android.testmenudrawer.models.Vin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvalencia on 11/28/17.
 */

//public static class VinListAsyncTaskLoader extends
public class VinListAsyncTaskLoader extends
        AsyncTaskLoader<List<Vin>> {
//    AsyncTaskLoader<List<String>> {

    private List<String> mData;
    private List<Vin> mVinList;

    public VinListAsyncTaskLoader(Context context) {
        super(context);
    }
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
//            deliverResult(mData);
            deliverResult(mVinList);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
//    public List<String> loadInBackground() {
    @Override
    public List<Vin> loadInBackground() {
        // This is on a background thread
        // Good to know: the Context returned by getContext()
        // is the application context
//        File jsonFile = new File(
//                getContext().getFilesDir(), "downloaded.json");
//        List<String> data = new ArrayList<>();
//
//        data.add("Number 1");
//        data.add("Number 2");
//        data.add("Number 3");

        URL recentVinsUrl = NetworkUtils.buildRecentVinsUrl();
        String vinResults = null;
        try {
            vinResults = NetworkUtils.getRecentVins(recentVinsUrl, getContext());
        } catch (IOException e) {
            Log.i("doInBackground", "exception.");

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (vinResults != null) {

            Gson gson = new Gson();

            /* This is a pattern for gson parsing an array of some kind of object */
            /* First you determine the listType of the object */
            Type listType = new TypeToken<List<Vin>>(){}.getType();
            /* Then you say you're going to parse to create a list of that thing, assigning to
             * gson the listType */
            List<Vin> vinList = gson.fromJson(vinResults, listType);

//            mVinsList = (RecyclerView) findViewById(R.id.recyclerview_vins);
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//            mVinsList.setLayoutManager(layoutManager);
//
//            mVinsList.setHasFixedSize(true);
//
////                mAdapter = new VinsAdapter(vinList);
//            if (mAdapter != null) {
//                mAdapter.setVinList(vinList);
//            }
//
//            mVinsList.setAdapter(mAdapter);

            return vinList;

        } else {
            Log.i("Data is ", "Null.");
        }

        return null;

//        String data = "This data come from the loader";
        // Parse the JSON using the library of your choice
        // Check isLoadInBackgroundCanceled() to cancel out early
//        return data;
    }
//    public void deliverResult(List<String> data) {
    @Override
    public void deliverResult(List<Vin> vinList) {
        // We’ll save the data for later retrieval
//        mData = data;
        mVinList = vinList;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(vinList);
    }
}