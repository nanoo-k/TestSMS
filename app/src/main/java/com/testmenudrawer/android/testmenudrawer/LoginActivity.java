package com.testmenudrawer.android.testmenudrawer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.testmenudrawer.android.testmenudrawer.utilities.NetworkUtils;
import com.testmenudrawer.android.testmenudrawer.utilities.PreferenceData;

import java.io.IOException;
import java.net.URL;

/**
 * Created by mvalencia on 10/17/17.
 */

public class LoginActivity extends AppCompatActivity {

    private TextView mErrorMessage;
    private ImageView mMotoshopLogo;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mMotoshopLogo = (ImageView) findViewById(R.id.motoshop_logo);
        mUsernameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);

//        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
//        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

//        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        /* Set default test username and pass */
        mUsernameEditText.setText("MANAGERKM");
        mPasswordEditText.setText("PASS8520");

        resizeLogo();

        mErrorMessage.bringToFront();
    }

    private void resizeLogo() {
        // Get the height of the window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        // Define and assign new logo height
        int newLogoHeight = height / 2;
        mMotoshopLogo.getLayoutParams().height = newLogoHeight;
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.animate().translationY(0);
    }

    private void hideErrorMessage() {
        mErrorMessage.animate().translationY(-200);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    public class LoginTask extends AsyncTask<URL, Void, String> {
        private Context context;
        public LoginTask (Context c){
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
            URL searchUrl = params[0];
            String loginResults = null;
            try {
                loginResults = NetworkUtils.getJWT(searchUrl, context, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());

            } catch (IOException e) {
//                Log.i("doInBackground", "exception.");

                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loginResults;
        }

        @Override
        protected void onPostExecute(String loginResults) {

//            Log.i("loginResults", loginResults);

//            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (loginResults != null && loginResults != "closed") {
//                Log.i("onPostExecute", loginResults);

                goToMainActivity();

            } else {
//                Log.i("onPostExecute", "Null.");
//                showErrorMessage();
            }
        }
    }


    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our {@link LoginTask}
     */
    private void makeLoginRequest() {
        String u = mUsernameEditText.getText().toString();
        String p = mPasswordEditText.getText().toString();

        if (u.equals("") || p.equals("")) {

            Log.i("THIS THE ERROR RIGHT HERE", "YEP");

            showErrorMessage();

        } else {
            URL loginUrl = NetworkUtils.buildAuthUrl(u, p);

//        mUrlDisplayTextView.setText(loginUrl.toString());
            new LoginTask(this.getApplicationContext()).execute(loginUrl);
        }

    }



    protected void goToMainActivity () {
        Intent intent = new Intent(getApplicationContext(), VinList.class);
        startActivity(intent);
    }

    public void login (View view) {
        makeLoginRequest();
    }

    public void goToSupportActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SupportActivity.class);
        startActivity(intent);
    }

    public void showJwt (View view) {
        String text = PreferenceData.getJwt(this.getApplicationContext());

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
