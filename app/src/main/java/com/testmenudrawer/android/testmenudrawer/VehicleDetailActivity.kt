package com.testmenudrawer.android.testmenudrawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import com.testmenudrawer.android.testmenudrawer.utilities.PreferenceData
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat.startActivity


private val DEFAULT_TEXT: String = "Information not available"
private val EXTRA_VIN: String = "VehicleDetailActivity - vin"
private val EXTRA_YEAR: String = "VehicleDetailActivity - year"
private val EXTRA_MAKE: String = "VehicleDetailActivity - make"
private val EXTRA_MODEL: String = "VehicleDetailActivity - model"
private var EXTRA_ENGINE: String = "VehicleDetailActivity - engine"

/**
 * Created by mvalencia on 10/9/17.
 */

open class VehicleDetailActivity : AppCompatActivity() {

    private var mVin: TextView? = null
    private var mYear: TextView? = null
    private var mMake: TextView? = null
    private var mModel: TextView? = null
    private var mEngine: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)

        mVin = findViewById<TextView>(R.id.vin_value)
        mYear = findViewById<TextView>(R.id.year_value)
        mMake = findViewById<TextView>(R.id.make_value)
        mModel = findViewById<TextView>(R.id.model_value)
        mEngine = findViewById<TextView>(R.id.engine_value)

        extractDataFromIntent()

    }

    private fun extractDataFromIntent() {
        var intent: Intent = getIntent()

        var vin: String = intent.getStringExtra(EXTRA_VIN)
        var year: String = intent.getStringExtra(EXTRA_YEAR)
        var make: String = intent.getStringExtra(EXTRA_MAKE)
        var model: String = intent.getStringExtra(EXTRA_MODEL)
        var engine: String = intent.getStringExtra(EXTRA_ENGINE)

        mVin!!.setText(vin)
        mYear!!.setText(year)
        mMake!!.setText(make)
        mModel!!.setText(model)
        mEngine!!.setText(engine)
    }

    override fun onBackPressed() {
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_vin, menu);

//        hideBackButton()

        return true
    }

    private fun hideBackButton() {
        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false) // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false) // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false) // remove the icon
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // Add all of this activity's parents to the back stack
//                            .addNextIntentWithParentStack(upIntent)
//                            // Navigate up to the closest parent
//                            .startActivities()
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent!!)
                }
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }
}
