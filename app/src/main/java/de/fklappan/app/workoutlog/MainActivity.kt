package de.fklappan.app.workoutlog

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import de.fklappan.app.workoutlog.common.AppBarHeader
import de.fklappan.app.workoutlog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AppBarHeader {

    private lateinit var binding : ActivityMainBinding
    lateinit var navController: NavController
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.menu[0].isChecked = true
        navView.menu[4].title = "V" + BuildConfig.VERSION_NAME
        navView.menu[4].isEnabled = false

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.findNavController()
        // make sure that the correct item is selected after returning from another fragment
        navController.addOnDestinationChangedListener { _, destination, _ -> updateNavItem(destination) }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_workout -> {
                // setPopUpTo the root of the given navigation graph takes care of clearing the back stack
                if (navController.currentDestination?.id != R.id.overviewFragment) {
                    navController.navigate(R.id.overviewFragment, null,
                        NavOptions.Builder().setPopUpTo(R.id.navigation_graph, true).build()
                    )
                }
            }
            R.id.nav_statistic -> {
                if (navController.currentDestination?.id != R.id.overviewStatisticFragment) {
                    navController.navigate(R.id.overviewStatisticFragment)
                }
            }
            R.id.nav_result -> {
                if (navController.currentDestination?.id != R.id.overviewResultFragment) {
                    navController.navigate(R.id.overviewResultFragment)
                }
            }
//            R.id.nav_personalrecord -> {
//
//            }
            R.id.nav_licenses -> {
                if (navController.currentDestination?.id != R.id.licensesFragment) {
                    navController.navigate(R.id.licensesFragment)
                }
            }
//            R.id.nav_send -> {
//
//            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateNavItem(destination: NavDestination) {
        // TODO 02.01.2020 Flo maybe it's sufficient to check only for overviewFragment as this is the only fragment to which we can return to
        when(destination.id) {
            R.id.overviewFragment -> navView.menu.findItem(R.id.nav_workout).isChecked = true
            R.id.overviewResultFragment -> navView.menu.findItem(R.id.nav_result).isChecked = true
            R.id.overviewStatisticFragment -> navView.menu.findItem(R.id.nav_statistic).isChecked = true
            R.id.licensesFragment -> navView.menu.findItem(R.id.nav_licenses).isChecked = true
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AppBarHeader impl

    override fun setHeaderText(text: String) {
        binding.appBar.toolbar.title = text
    }

    override fun setHeaderText(resource: Int) {
        binding.appBar.toolbar.title = getString(resource)
    }
}
