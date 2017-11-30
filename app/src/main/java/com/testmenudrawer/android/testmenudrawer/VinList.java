package com.testmenudrawer.android.testmenudrawer;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.testmenudrawer.android.testmenudrawer.models.Vin;
import com.testmenudrawer.android.testmenudrawer.models.VinsAdapter;
//import com.varvet.barcodereadersample.barcode.BarcodeCaptureActivity;
import com.testmenudrawer.android.testmenudrawer.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//import com.motoshop.vins.ManualEntryActivity;
import com.testmenudrawer.android.testmenudrawer.utilities.PreferenceData;
import com.testmenudrawer.android.testmenudrawer.utilities.VinListAsyncTaskLoader;
//import com.testmenudrawer.android.testmenudrawer.utilities.VinListAsyncTaskLoader;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class VinList extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {


    private VinsAdapter mAdapter;
    private RecyclerView mVinsList;

    private TextView mResultTextView;
    private TextView mDecodedVinTextView;
    private ListView mRecentVinsListView;
    private ProgressBar mLoadingIndicator;


    // A RecyclerView.Adapter which will display the data
//    private MyAdapter mAdapter;
    // Our Callbacks. Could also have the Activity/Fragment implement
// LoaderManager.LoaderCallbacks<List<String>>
    private LoaderManager.LoaderCallbacks<List<String>>
            mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<List<String>>() {
                @Override
                public Loader<List<String>> onCreateLoader(
                        int id, Bundle args) {
                    Log.i("onCreateLoader", "STARTED");
                    return new VinListAsyncTaskLoader(VinList.this);
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    Log.i("onLoadFinished", "DONE");
                    Log.i("Datum: ", data.get(0));

                    // Display our data, for instance updating our adapter
                    if (mAdapter == null) {
                        Log.i("mAdapter is ", "null");
                    } else {
                        mAdapter.setData(data);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                    // Loader reset, throw away our data,
                    // unregister any listeners, etc.

                    if (mAdapter == null) {
                        Log.i("mAdapter is ", "null");
                    } else {

                        List<String> data = new ArrayList<>();
                        mAdapter.setData(data);
                    }
                    // Of course, unless you use destroyLoader(),
                    // this is called when everything is already dying
                    // so a completely empty onLoaderReset() is
                    // totally acceptable
                }
            };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // The usual onCreate() — setContentView(), etc.
//        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);
//    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAdapter = new VinsAdapter(null);


//        super.onCreate(savedInstanceState);
//        getSupportLoaderManager().initLoader(0, null,  mLoaderCallbacks);
        getLoaderManager().initLoader(0, null,  mLoaderCallbacks);

        /* Save references to onscreen elements */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mResultTextView = (TextView) findViewById(R.id.result_textview);
        mDecodedVinTextView = (TextView) findViewById(R.id.decode_vin_result_textview);
//        mRecentVinsListView = (ListView) findViewById(R.id.recent_vins_list_view);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), VinCaptureActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Log.i("HITH IT", "HERE");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vin_list) {
            Log.i("Nav", "Scan");
            goToVinListActivity();
//        } else if (id == R.id.nav_support) {
//            Log.i("Nav", "Support");
//            goToSupportActivity();
        } else if (id == R.id.nav_user) {
            Log.i("Nav", "User");
            goToUserActivity();
        } else if (id == R.id.nav_support) {
            Log.i("Nav", "Support");
            goToSupportActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToVinListActivity() {
        Intent intent = new Intent(getApplicationContext(), VinList.class);
        startActivity(intent);
    }

    private void goToSupportActivity() {
        Intent intent = new Intent(getApplicationContext(), SupportActivity.class);
        startActivity(intent);
    }

    private void goToUserActivity() {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }


    private static final int BARCODE_READER_REQUEST_CODE = 1;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == BARCODE_READER_REQUEST_CODE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    Point[] p = barcode.cornerPoints;
//                    mResultTextView.setText(barcode.displayValue);
//
//                    /* Vibrate phone and decode this Vin */
////                    vibrate(500);
////                    decodeVinRequest(barcode.displayValue);
//
//                } else mResultTextView.setText(R.string.no_barcode_captured);
//            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
//                    CommonStatusCodes.getStatusCodeString(resultCode)));
//        } else super.onActivityResult(requestCode, resultCode, data);
//    }

    /* onStart, onResume */
    @Override
    protected void onPostResume() {

        /* Check if user is logged in */
        getRecentVins();
//        decodeVinRequest("3FA6P0K93FR226629");
//        boolean isLoggedIn = PreferenceData.getUserLoggedInStatus(this.getApplicationContext());
//        if (!isLoggedIn) {
//
//            navigateToLogin();
//        }
//        else {
//            super.onPostResume();
//        }
        super.onPostResume();
    }

    /* Navigate to login page */
    protected void navigateToLogin () {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void getRecentVins () {
        boolean isLoggedIn = PreferenceData.getUserLoggedInStatus(this.getApplicationContext());

        /* If user doesn't have a JWT, we can't make the request */
        if (!isLoggedIn) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            String text = "You are not logged in. Please log in.";

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            navigateToLogin();

        } else {
            makeRecentVinsRequest();
        }

    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link GetRecentVinsTask}
     */
    private void makeRecentVinsRequest() {

        String jwt = PreferenceData.getJwt(this.getApplicationContext());

        URL recentVinsUrl = NetworkUtils.buildRecentVinsUrl();

        new GetRecentVinsTask(this.getApplicationContext()).execute(recentVinsUrl);

    }

    public class GetRecentVinsTask extends AsyncTask<URL, Void, String> {

        /* We need the app context available for these callback functions, so
        ensure that we set it when calling this task */
        private Context context;
        public GetRecentVinsTask (Context c){
            context = c;
        }

        @Override
        protected void onPreExecute() {
            Log.i("onPreExecute", "MADE IT.");
            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            Log.i("doInBackground", "MADE IT.");

            URL recentVinsUrl = params[0];
            String vinResults = null;
            try {
                vinResults = NetworkUtils.getRecentVins(recentVinsUrl, context);
            } catch (IOException e) {
                Log.i("doInBackground", "exception.");

                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vinResults;
//            return "true";
        }

        @Override
        protected void onPostExecute(String vinResults) {

//            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (vinResults != null) {
                Log.i("onPostExecute", vinResults);

//                showJsonDataView();
//                mRecentVinsTextView.setText(vinResults);


                Gson gson = new Gson();

                /* This is a pattern for gson parsing an array of some kind of object */
                /* First you determine the listType of the object */
                Type listType = new TypeToken<List<Vin>>(){}.getType();
                /* Then you say you're going to parse to create a list of that thing, assigning to
                 * gson the listType */
                List<Vin> vinList = gson.fromJson(vinResults, listType);

//                Vin[] vinList = gson.fromJson(vinResults, Vin[].class);


//                VinListAdapter adapter = new VinListAdapter(context, R.layout.vin_management_list, vinList);

//                mRecentVinsListView.setAdapter(adapter);

                mVinsList = (RecyclerView) findViewById(R.id.recyclerview_vins);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                mVinsList.setLayoutManager(layoutManager);

                mVinsList.setHasFixedSize(true);

//                mAdapter = new VinsAdapter(vinList);
                if (mAdapter != null) {
                    mAdapter.setVinList(vinList);
                }

                mVinsList.setAdapter(mAdapter);

            } else {
                Log.i("onPostExecute", "Null.");
                showErrorMessage();
            }
        }
    }


    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
//        mRecentVinsListView.setVisibility(View.INVISIBLE);
        // Then, show the error
//        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
