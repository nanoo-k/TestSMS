package com.testmenudrawer.android.testmenudrawer

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
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.testmenudrawer.android.testmenudrawer.models.SupportViewModel
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.TextView


//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User


/**
 * Created by mvalencia on 10/17/17.
 */
class SupportActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    var mViewModel: SupportViewModel? = null

    private var mContainer: android.support.constraint.ConstraintLayout? = null
    private var mUsernameEditText: EditText? = null
    private var mEmailEditText: EditText? = null
    private var mPhoneEditText: EditText? = null
    private var mCommentsEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_support)

        mContainer = findViewById<android.support.constraint.ConstraintLayout>(R.id.container)
        mUsernameEditText = findViewById<EditText>(R.id.contact_name_field)
        mEmailEditText = findViewById<EditText>(R.id.contact_email_field)
        mPhoneEditText = findViewById<EditText>(R.id.contact_phone_field)
        mCommentsEditText = findViewById<EditText>(R.id.block_text_field)



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
    }

    fun setFocusEventHandlers() {
        mUsernameEditText!!.setOnFocusChangeListener(object : OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
//                    Toast.makeText(applicationContext, "Got the focus", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Lost the focus", Toast.LENGTH_LONG).show()
//                    mViewModel!!.setUsername("NEW USERNAME")

                    val anotherName : String = "John Doe";
                    mViewModel!!.getUsername().setValue(anotherName);
                }
            }
        })
//        mEmailEditText!!.setOnFocusChangeListener(object : OnFocusChangeListener {
//            override fun onFocusChange(view: View, hasFocus: Boolean) {
//                if (hasFocus) {
////                    Toast.makeText(applicationContext, "Got the focus", Toast.LENGTH_LONG).show()
//                } else {
////                    Toast.makeText(applicationContext, "Lost the focus", Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//        mPhoneEditText!!.setOnFocusChangeListener(object : OnFocusChangeListener {
//            override fun onFocusChange(view: View, hasFocus: Boolean) {
//                if (hasFocus) {
////                    Toast.makeText(applicationContext, "Got the focus", Toast.LENGTH_LONG).show()
//                } else {
////                    Toast.makeText(applicationContext, "Lost the focus", Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//        mCommentsEditText!!.setOnFocusChangeListener(object : OnFocusChangeListener {
//            override fun onFocusChange(view: View, hasFocus: Boolean) {
//                if (hasFocus) {
////                    Toast.makeText(applicationContext, "Got the focus", Toast.LENGTH_LONG).show()
//                } else {
////                    Toast.makeText(applicationContext, "Lost the focus", Toast.LENGTH_LONG).show()
//                }
//            }
//        })
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
}