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

private val DEFAULT_TEXT: String = "Information not available"
private val EXTRA_VIN: String = "VehicleDetailActivity - vin"
private val EXTRA_YEAR: String = "VehicleDetailActivity - year"
private val EXTRA_MAKE: String = "VehicleDetailActivity - make"
private val EXTRA_MODEL: String = "VehicleDetailActivity - model"
private var EXTRA_ENGINE: String = "VehicleDetailActivity - engine"

/**
 * Created by mvalencia on 10/9/17.
 */

class VehicleDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mVin: TextView? = null
    private var mYear: TextView? = null
    private var mMake: TextView? = null
    private var mModel: TextView? = null
    private var mEngine: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)

        mVin = findViewById(R.id.vin_value) as TextView
        mYear = findViewById(R.id.year_value) as TextView
        mMake = findViewById(R.id.make_value) as TextView
        mModel = findViewById(R.id.model_value) as TextView
        mEngine = findViewById(R.id.engine_value) as TextView

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

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
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_logout) {
            PreferenceData.clearJwt(applicationContext);


            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
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

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
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