package com.testmenudrawer.android.testmenudrawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.testmenudrawer.android.testmenudrawer.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by mvalencia on 10/17/17.
 */

public class LoginActivity extends AppCompatActivity {

    private android.support.constraint.ConstraintLayout mContainer;
    private LinearLayout mErrorMessage;
    private TextView mErrorMessageClose;
    private TextView mErrorMessageForgotPassword;
    private ImageView mMotoshopLogo;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;
    private ProgressBar mLoadingIndicator;

    // `252` is the height when keyboard is closed on Nexus 6 and Pixel XL
    // `18` is height for this tiny ass device
    private int keyboardHeight;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        mContainer = findViewById(R.id.container);
        mErrorMessage = findViewById(R.id.error_message);
        mErrorMessageClose = findViewById(R.id.error_message_close);
        mErrorMessageForgotPassword = findViewById(R.id.error_message_forgot_password);
        mMotoshopLogo = findViewById(R.id.motoshop_logo);
        mUsernameEditText = findViewById(R.id.username);
        mPasswordEditText = findViewById(R.id.password);
        mSignInButton = findViewById(R.id.submitButton);
//        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

//        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//            public void onGlobalLayout(){
//                keyboardHeight = mContainer.getRootView().getHeight() - mContainer.getHeight();
//                // IF height diff is more then 150, consider keyboard as visible.
//                Log.i("Keyboard height", String.valueOf(keyboardHeight));
//
//                if (keyboardHeight > 300) {
//
//                    EditText editTextThatHasFocus;
//
//                    if (mUsernameEditText.hasFocus()) {
//                        editTextThatHasFocus = mUsernameEditText;
//                    } else {
//                        editTextThatHasFocus = mPasswordEditText;
//                    }
//
//                    int[] sp = new int[2];
//                    int[] wp = new int[2];
//                    editTextThatHasFocus.getLocationOnScreen(sp);
//                    editTextThatHasFocus.getLocationInWindow(wp);
//
//                    int height = editTextThatHasFocus.getHeight();
//
//                    DisplayMetrics displayMetrics = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                    int wheight = displayMetrics.heightPixels;
//
//                    int topOfKeyboard = wheight - keyboardHeight;
//                    int bottomOfEditText = height + sp[1];
////                    int marginPlusButton = mSignInButton.getHeight();
//
//                    int animateThisMuch = bottomOfEditText - topOfKeyboard - 252;
//
////                    animateTextFields(animateThisMuch * -1);
//
////                    Log.i("topOfKeyboard", String.valueOf(topOfKeyboard));
////                    Log.i("bottomOfEditText", String.valueOf(bottomOfEditText));
//                }
////                animateTextFields(-1 * keyboardHeight);
//            }
//        });

        mErrorMessageClose.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideErrorMessage();

            }
        });
        mErrorMessageForgotPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goToSupportActivity(v);

            }
        });


        // Register which EditText user selected
//        mUsernameEditText.setOnTouchListener(new View.OnTouchListener()
//        {
//            public boolean onTouch(View arg0, MotionEvent arg1)
//            {
//                if (keyboardHeight > 300) {
//                    //                evalue="2";
////                    int[] sp = new int[2];
////                    int[] wp = new int[2];
////                    arg0.getLocationOnScreen(sp);
////                    arg0.getLocationInWindow(wp);
////
////                    int height = arg0.getHeight();
////
////                    DisplayMetrics displayMetrics = new DisplayMetrics();
////                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
////                    int wheight = displayMetrics.heightPixels;
////                    int wwidth = displayMetrics.widthPixels;
////
//////                    Log.i("Window height", String.valueOf(wheight));
//////                    Log.i("Window width", String.valueOf(wwidth));
////
////                    int topOfKeyboard = keyboardHeight;
////                    int bottomOfEditText = height + sp[1];
////
////                    Log.i("topOfKeyboard", String.valueOf(topOfKeyboard));
////                    Log.i("bottomOfEditText", String.valueOf(bottomOfEditText));
//
//                    //                Log.i("Screen position X", String.valueOf(sp[0]));
//                    //                Log.i("Screen position Y", String.valueOf(sp[1]));
//                    //                Log.i("Window position X", String.valueOf(wp[0]));
//                    //                Log.i("Window position Y", String.valueOf(wp[1]));
//                    //                Log.i("height", String.valueOf(height));
//                    Log.i("Which: ", "The Username");
//                }
//                return false;
//            }
//        });
//        mPasswordEditText.setOnTouchListener(new View.OnTouchListener()
//        {
//            public boolean onTouch(View arg0, MotionEvent arg1)
//            {
//                Log.i("Which: ", "The Password");
////                evalue="2";
//                return false;
//            }
//        });


//        mUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    // code to execute when EditText loses focus
//                    Log.i("Username lost focus", "");
//                } else {
//
//                    Log.i("Username has focus", "");
//                }
//            }
//        });
//        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    // code to execute when EditText loses focus
//                    Log.i("Password lost focus", "");
//                } else {
//
//                    Log.i("Password has focus", "");
//                }
//            }
//        });

        // Hide keyboard when user clicks around screen
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // Make textfields lose focus
                mUsernameEditText.clearFocus();
                mPasswordEditText.clearFocus();

//                animateTextFields(0);
                return true;
            }
        });

        /* Set default test username and pass */
        mUsernameEditText.setText("MANAGERKM");
        mPasswordEditText.setText("PASS8520");

//        mUsernameEditText.setText("managerfost");
//        mPasswordEditText.setText("pass8520");

//        mUsernameEditText.setText("km@managerkm");
//        mPasswordEditText.setText("pass8520");

        resizeLogo();
        mErrorMessage.bringToFront();

    }

    //
//    private void animateTextFields(int toThisSpot) {
//        mContainer.animate().translationY(toThisSpot);
//    }

    /**
     *  Determine whether the textfield is going to be covered by the keyboard, and, if it is,
     *  pass back the location the textfield needs to be animated to.
     */
    private int positionToAnimateTo(Object textFieldBottomBorder) {

        // Find out how tall the keyboard is
        // IMPLEMENTED callbacks for this.

        // Find out distance of bottom of the textfield (with margin) to bottom of the screen
        //

        // Figure out the difference. If the keyboard is taller (greater) than the bottom of the textfield,

        return 10;
    }

    private void resizeLogo() {
        // Get the height of the window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        // Define and assign new logo height
        int newLogoHeight = height / 3;
        mMotoshopLogo.getLayoutParams().height = newLogoHeight;
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.animate().translationY(0);
    }

    private void hideErrorMessage() {
        mErrorMessage.animate().translationY(-350);
//        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    public class LoginTask extends AsyncTask<URL, Void, String> {
        private Context context;
        public LoginTask (Context c){
            context = c;
        }

        @Override
        protected void onPreExecute() {
            Log.i("onPreExecute", "preprepre");
            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String loginResults = null;


            Log.i("doInBackground", "starting it");


            try {
//                loginResults = NetworkUtils.getJWT(searchUrl, context);
                loginResults = NetworkUtils.getJWT(searchUrl, context, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());

            } catch (IOException e) {
                Log.i("doInBackground", "exception.");

                e.printStackTrace();
            } catch (Exception e) {

                Log.i("doInBackground", "catch.");
                e.printStackTrace();
            }
            Log.i("doInBackground", "doing it?");
            return loginResults;
        }

        @Override
        protected void onPostExecute(String loginResults) {

            Log.i("loginResults", "HEREHERE!");

//            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (loginResults != null && loginResults != "closed") {
//                Log.i("onPostExecute", loginResults);

                goToMainActivity();

            } else {
                Log.i("onPostExecute", "Null.");
                showErrorMessage();
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

            Log.i("THIS THE ERROR", "YEP");

            showErrorMessage();

        } else {
            Log.i("This part works", "YEP");
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
        intent.putExtra("setBackButtonToThisActivity", "LoginActivity");
        startActivity(intent);
    }

//    public void showJwt (View view) {
//        String text = PreferenceData.getJwt(this.getApplicationContext());
//
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
//    }
}
