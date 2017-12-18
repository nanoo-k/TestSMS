package com.testmenudrawer.android.testmenudrawer

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.SupportActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem


import com.testmenudrawer.android.testmenudrawer.VinList
import com.testmenudrawer.android.testmenudrawer.UserActivity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil.setContentView
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.testmenudrawer.android.testmenudrawer.models.SupportViewModel
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView


//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User


/**
 * Created by mvalencia on 10/17/17.
 */
class SupportActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private val PERMISSION_REQUEST_CALL_PHONE = 0

    var mViewModel: SupportViewModel? = null

    private var mContainer: android.support.constraint.ConstraintLayout? = null
    private var mUsernameEditText: EditText? = null
    private var mEmailEditText: EditText? = null
    private var mPhoneEditText: EditText? = null
    private var mCommentsEditText: EditText? = null
    private var mSubmitButton: Button? = null

    // Going to validate all these
    private var editTexts = arrayListOf<EditText>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_support)

        mContainer = findViewById<android.support.constraint.ConstraintLayout>(R.id.container)
        mUsernameEditText = findViewById<EditText>(R.id.contact_name_field)
        mEmailEditText = findViewById<EditText>(R.id.contact_email_field)
        mPhoneEditText = findViewById<EditText>(R.id.contact_phone_field)
        mCommentsEditText = findViewById<EditText>(R.id.block_text_field)
        mSubmitButton = findViewById<Button>(R.id.submit_button)



        mViewModel = ViewModelProviders.of(this).get(SupportViewModel::class.java!!)

        val usernameObserver = object : Observer<String> {
            override fun onChanged(newName: String?) {
                // Update the UI, in this case, a TextView.
                mUsernameEditText!!.setText(newName)
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel!!.getUsername().observe(this, usernameObserver)

//        mUsernameEditText!!.setText(mViewModel!!.getUsername())
//        mEmailEditText!!.setText(mViewModel!!.getEmail())
//        mPhoneEditText!!.setText(mViewModel!!.getPhone())
//        mCommentsEditText!!.setText(mViewModel!!.getComments())

        setFocusEventHandlers()

        val toolbar = findViewById<Toolbar>(R.id.toolbar_support)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()


        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        // Hide keyboard when user clicks around screen
        mContainer!!.setOnTouchListener(View.OnTouchListener { v, event ->
            // Hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            // Make textfields lose focus
            mEmailEditText!!.clearFocus()
            mPhoneEditText!!.clearFocus()
            mUsernameEditText!!.clearFocus()
            mCommentsEditText!!.clearFocus()

            //                animateTextFields(0);
            true
        })

        mPhoneEditText!!.addTextChangedListener( object : TextWatcher {
            private var startString = ""
            private var endString = ""
            private var selectionStartPosition = 0
            private var selectionEndPosition = 0

            // If startPosition > endPosition, something deleted,
            // else something added,
            // (else if equal, something replaced and do nothing unless it's a 0, )
            // (004) 567-8911


            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                selectionStartPosition = mPhoneEditText!!.selectionStart
                selectionEndPosition = mPhoneEditText!!.selectionEnd
//                Log.i("beforeTextChanged: sSrt", mPhoneEditText!!.selectionStart.toString())
//                Log.i("beforeTextChanged: sEnd", mPhoneEditText!!.selectionEnd.toString())

                startString = s.toString()
            }

            override fun afterTextChanged(s: Editable) {

                selectionEndPosition = mPhoneEditText!!.selectionStart

                if (selectionStartPosition < selectionEndPosition) {
                    when(selectionStartPosition) {
                        0 -> {
                            s.insert(0, "(")
                        }
                        4 -> {
                            s.insert(4, ") ")
                        }
                        9 -> {
                            s.insert(9, "-")
                        }

                    }
                } else {

                    when(selectionEndPosition) {
                        1 -> {
                            s.delete(0, 1)
                        }
                        6 -> {
                            s.delete(4, 6)
                        }
                        10 -> {
                            s.delete(9, 10)
                        }

                    }
                }


//                Log.i("afterTextChanged: sSrt", mPhoneEditText!!.selectionStart.toString())
//                Log.i("afterTextChanged: sEnd", mPhoneEditText!!.selectionEnd.toString())



//        if (s.toString() != current) {
//            val userInput = s.toString().replace("[^\\d]".toRegex(), "")
//            if (userInput.length <= 16) {
//                val sb = StringBuilder()
//                for (i in 0..userInput.length - 1) {
//                    if (i % 4 == 0 && i > 0) {
//                        sb.append(" ")
//                    }
//                    sb.append(userInput[i])
//                }
//                current = sb.toString()
//
//                s.filters = arrayOfNulls<InputFilter>(0)
//            }
//            s.replace(0, s.length, current, 0, current.length)
//        }
            }
        } )


        // Add elements to array of EditTexts that are going to be validated
        editTexts.add(mUsernameEditText!!)
        editTexts.add(mCommentsEditText!!)
        editTexts.add(mPhoneEditText!!)
        editTexts.add(mEmailEditText!!)
        validateForm()
    }

    private fun validateForm() {
        var isFormValid: Boolean = true
        for (editText in editTexts) {
            var isFieldValid: Boolean = false
            if (editText.text.length == 0) {
                isFormValid = false
            }
        }

        if (isFormValid) {
            mSubmitButton!!.setEnabled(true)
            mSubmitButton!!.setAlpha(1.0f)
        } else {
            mSubmitButton!!.setEnabled(false)
            mSubmitButton!!.setAlpha(0.5f)
        }

    }

    fun setFocusEventHandlers() {
        mUsernameEditText!!.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
//                    Toast.makeText(applicationContext, "Got the focus", Toast.LENGTH_LONG).show()
                } else {
//                    val anotherName: String = "John Doe";
//                    mViewModel!!.getUsername().setValue(anotherName);
                    validateForm()
                }
            }
        }
        mEmailEditText!!.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                } else {
                    validateForm()
                }
            }
        }
        mPhoneEditText!!.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                } else {
                    validateForm()
                }
            }
        }
        mCommentsEditText!!.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                } else {
                    validateForm()
                }
            }
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.logout, menu);
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_vin_list) {
            Log.i("Nav", "Scan")
            goToVinListActivity()
            //        } else if (id == R.id.nav_support) {
            //            Log.i("Nav", "Support");
            //            goToSupportActivity();
        } else if (id == R.id.nav_user) {
            Log.i("Nav", "User")
            goToUserActivity()
        } else if (id == R.id.nav_support) {
            Log.i("Nav", "Support")
            goToSupportActivity()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun goToVinListActivity() {
        val intent = Intent(applicationContext, VinList::class.java)
        startActivity(intent)
    }

    private fun goToSupportActivity() {
        val intent = Intent(applicationContext, SupportActivity::class.java)
        startActivity(intent)
    }

    private fun goToUserActivity() {
        val intent = Intent(applicationContext, UserActivity::class.java)
        startActivity(intent)
    }

    fun submit(view: View) {
//        makeLoginRequest()

        Log.i("Submit", "submitting");


        setContentView(R.layout.support_thank_you)

    }

    fun goBack(view: View) {

        // If user is logged in, go to vin list
        if (false) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        // Else go to login screen
        else {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    fun makeCall(view: View) {
//        makeLoginRequest()

        Log.i("Make call", "calling");

        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck ==  PackageManager.PERMISSION_GRANTED) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + 8442786643)//change the number
            startActivity(callIntent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CALL_PHONE);
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
//                Snackbar.make(mLayout, "Camera permission was granted. Starting preview.",
//                        Snackbar.LENGTH_SHORT)
//                        .show()
//                startCamera()

                Log.i("Make call", "calling...");

            } else {
                // Permission request was denied.
//                Snackbar.make(mLayout, "Camera permission request was denied.",
//                        Snackbar.LENGTH_SHORT)
//                        .show()

                Log.i("Call denied", "calling...");
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
}

class PhoneNumberTextWatcher : TextWatcher {
    private var current = ""

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {



        current = s.toString()
//        Log.i("onTextChanged", current)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        current = s.toString()
//        Log.i("beforeTextChanged", current)
    }

    override fun afterTextChanged(s: Editable) {
        current = s.toString()
//        Log.i("afterTextChanged", current)
//        if (s.toString() != current) {
//            val userInput = s.toString().replace("[^\\d]".toRegex(), "")
//            if (userInput.length <= 16) {
//                val sb = StringBuilder()
//                for (i in 0..userInput.length - 1) {
//                    if (i % 4 == 0 && i > 0) {
//                        sb.append(" ")
//                    }
//                    sb.append(userInput[i])
//                }
//                current = sb.toString()
//
//                s.filters = arrayOfNulls<InputFilter>(0)
//            }
//            s.replace(0, s.length, current, 0, current.length)
//        }
    }
}