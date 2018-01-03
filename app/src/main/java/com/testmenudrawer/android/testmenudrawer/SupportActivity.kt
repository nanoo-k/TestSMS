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
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.*
import com.testmenudrawer.android.testmenudrawer.models.SupportViewModel
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.nio.file.Files.delete


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
//        toolbar.setVisibility(View.INVISIBLE)
        setSupportActionBar(toolbar)
//
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        extractDataFromIntent()

        setToolbar(toggle, toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.getMenu().getItem(2).setChecked(true)

        val header = navigationView.getHeaderView(0)
        val closeMenuBtn = header.findViewById<View>(R.id.close_menu_btn) as ImageView
        closeMenuBtn.setOnClickListener {
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawers()
        }


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

            true
        })

        mPhoneEditText!!.addTextChangedListener( object : TextWatcher {
            private var startString = ""
            private var endString = ""
            private var selectionStartPosition = 0
            private var selectionEndPosition = 0
            private var cursorStartPosition = 0
            private var cursorEndPosition = 0
            private var numberOfCharsToDelete = 0

            // If startPosition > endPosition, something deleted,
            // else something added,
            // (else if equal, something replaced and do nothing unless it's a 0, )
            // (004) 567-8911


            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                startString = s.toString()

                numberOfCharsToDelete = count

                selectionStartPosition = mPhoneEditText!!.selectionStart
                selectionEndPosition = mPhoneEditText!!.selectionEnd

                cursorStartPosition = mPhoneEditText!!.selectionEnd

                startString = s.toString()


            }

            private var formattedPhoneNumber = ""

            override fun afterTextChanged(s: Editable) {

                cursorEndPosition = mPhoneEditText!!.selectionEnd


                // `afterTextChanged` is going to be called over and over again when running
                // `s.replace()` down below unless you give it a reason not to, so I save a copy of
                // the formatted phone number, and if it matches what the "change" is, we do nothing.
                if (s.toString() != formattedPhoneNumber) {

                    // If the end position of the cursor is grater than the start position, user
                    // added characters. Else user deleted characters.
                    if (cursorStartPosition < cursorEndPosition) {
                        var clean = stripPhoneNumber(s)
                        var new = formatPhoneNumber(clean)

                        formattedPhoneNumber = new.toString()
                        s.replace(0, s.length, new, 0, new.length)

                    } else {

                        var correctCursorLocation: Int = mPhoneEditText!!.selectionStart
                        var deleted: Editable
                        if (numberOfCharsToDelete == 1) {
                            var editable = SpannableStringBuilder(startString)
                            var clean = stripPhoneNumber(editable)

                            deleted = deleteOneFromPhoneNumber(clean, selectionStartPosition, numberOfCharsToDelete)
                            correctCursorLocation = correctCursorLocationWhenDeletingOneChar(mPhoneEditText!!.selectionStart)

                        } else {
                            var clean = stripPhoneNumber(s)
                            deleted = clean

                            correctCursorLocation = correctCursorLocationWhenDeletingManyChars(mPhoneEditText!!.selectionStart, deleted.length)
                        }

                        var new = formatPhoneNumber(deleted)
//
                        mPhoneEditText!!.setSelection(correctCursorLocation, correctCursorLocation)

                        formattedPhoneNumber = new.toString()
                        s.replace(0, s.length, new, 0, new.length)


                    }

                }
            }
        })


        // Add elements to array of EditTexts that are going to be validated
        editTexts.add(mUsernameEditText!!)
        editTexts.add(mCommentsEditText!!)
        editTexts.add(mPhoneEditText!!)
        editTexts.add(mEmailEditText!!)
        validateForm()
    }

    private var mBackButtonIntent: String? = null
    private val EXTRA_BACK_BUTTON_INTENT: String = "setBackButtonToThisActivity"
    private fun extractDataFromIntent() {
        var intent: Intent = getIntent()
        mBackButtonIntent = intent.getStringExtra(EXTRA_BACK_BUTTON_INTENT)
    }

    private fun setToolbar(toggle: ActionBarDrawerToggle, toolbar: Toolbar) {
        if (mBackButtonIntent == "LoginActivity") {
            toggle.setDrawerIndicatorEnabled(false)
            toolbar.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                }
            })
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)

        } else {
            toggle.setDrawerIndicatorEnabled(true)

        }
    }

    private fun correctCursorLocationWhenDeletingOneChar(cursorLocation: Int): Int {
        var cursorLocation = cursorLocation

        // (234) 789-0123
        // This gets run only when deleting things

        when(cursorLocation) {
            4 -> {
                cursorLocation = 3
            }
            5 -> {
                cursorLocation = 3
            }
            9 -> {
                cursorLocation = 8
            }
        }

        return cursorLocation
    }

    private fun correctCursorLocationWhenDeletingManyChars(cursorLocation: Int, phoneNumberLength: Int): Int {
        var cursorLocation = cursorLocation

        // (234) 789-0123
        // This gets run only when deleting things

        when(cursorLocation) {
            0 -> {
                if (phoneNumberLength > 2) {
                    cursorLocation = 1
                }
            }
            4 -> {
                if (phoneNumberLength > 4) {
                    cursorLocation = 6
                }
            }
            5 -> {
                if (phoneNumberLength > 4) {
                    cursorLocation = 6
                }
            }
            9 -> {
                if (phoneNumberLength > 6) {
                    cursorLocation = 10
                }
            }
        }

        return cursorLocation
    }

    private fun deleteOneFromPhoneNumber(phoneNumber: Editable, selectionStartPosition: Int, numberOfCharsToDelete: Int): Editable {

        var actualStartCursorPosition: Int = selectionStartPosition
        var numberOfCharsToDelete = numberOfCharsToDelete

        var greaterThanOne = 1
        var greaterThanFour = 2
        var greaterThanNine = 1

        // Set the cursor position
        when(selectionStartPosition) {
            1 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne
            }
            2 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne
            }
            3 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne
            }
            4 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne
            }
            5 -> {
                actualStartCursorPosition = 4 - greaterThanOne
            }
            6 -> {
                actualStartCursorPosition = 4 - greaterThanOne
            }
            7 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour
            }
            8 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour
            }
            9 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour
            }
            10 -> {
                actualStartCursorPosition = 9 - greaterThanOne - greaterThanFour
            }
            11 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour - greaterThanNine
            }
            12 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour - greaterThanNine
            }
            13 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour - greaterThanNine
            }
            14 -> {
                actualStartCursorPosition = selectionStartPosition - greaterThanOne - greaterThanFour - greaterThanNine
            }

        }

        phoneNumber.delete(actualStartCursorPosition - numberOfCharsToDelete, actualStartCursorPosition)

        return phoneNumber
    }

//    private fun updateNumberOfCharsToDelete(numberOfCharsToDelete: Int, index: Int) : Int {
//        var numberOfCharsToDelete = numberOfCharsToDelete
//        // (234) 789-0123
//        var count = numberOfCharsToDelete
//
//        if (count > (4 - index)) {
//            numberOfCharsToDelete = numberOfCharsToDelete - 1
//        }
//        if (count > (9 - index)) {
//            numberOfCharsToDelete = numberOfCharsToDelete - 1
//        }
//        if (count > (10 - index)) {
//            numberOfCharsToDelete = numberOfCharsToDelete - 1
//        }
//        if (count > (14 - index)) {
//            numberOfCharsToDelete = numberOfCharsToDelete - 1
//        }
//
//        Log.i("index", numberOfCharsToDelete.toString())
//        Log.i("updateNumberOfCharsToDelete", numberOfCharsToDelete.toString())
//        return numberOfCharsToDelete
//    }

    private fun stripPhoneNumber(phoneNumber: Editable): Editable {
        var phoneNumberAsString = phoneNumber.toString()

        phoneNumberAsString = phoneNumberAsString.replace("(", "")
        phoneNumberAsString = phoneNumberAsString.replace(")", "")
        phoneNumberAsString = phoneNumberAsString.replace(" ", "")
        phoneNumberAsString = phoneNumberAsString.replace("-", "")

        var editable = SpannableStringBuilder(phoneNumberAsString)

        return editable
    }

    private fun formatPhoneNumber(phoneNumber: Editable): Editable {

        if (phoneNumber.length > 0 && phoneNumber.get(0).toString() != "(") {
            phoneNumber.insert(0, "(")
        }
        if (phoneNumber.length >= 5 && phoneNumber.get(4).toString() != ")") {
            phoneNumber.insert(4, ")")
        }
        if (phoneNumber.length >= 6 && phoneNumber.get(5).toString() != " ") {
            phoneNumber.insert(5, " ")
        }
        if (phoneNumber.length >= 10 && phoneNumber.get(9).toString() != "-") {
            phoneNumber.insert(9, "-")
        }

        return phoneNumber
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
//        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
            super.onBackPressed()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.back, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        Log.i("itemId", item.itemId.toString())
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun hideBackButton() {
        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false) // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false) // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false) // remove the icon
        }
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
        when (mBackButtonIntent) {
            "LoginActivity" -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            "VinList" -> {
                val intent = Intent(applicationContext, VinList::class.java)
                startActivity(intent)
            }
            "UserActivity" -> {
                val intent = Intent(applicationContext, UserActivity::class.java)
                startActivity(intent)
            }
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
