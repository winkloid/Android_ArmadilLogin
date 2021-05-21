package de.tuchemnitz.armadillogin

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.fido.Fido
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.model.UserDataViewModel

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private val userViewModel: UserDataViewModel by viewModels()
    private val sharedViewModel: ArmadilloViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

        /**
         * Sets theme colors depending on [sharedViewModel.colorMode]. Default is light mode.
         */
        sharedViewModel.colorMode.observe(this@MainActivity) { colorMode ->
            if(colorMode.equals(0)) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            } else if(colorMode.equals(1)) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.setFido2ApiClient(Fido.getFido2ApiClient(this))
    }

    override fun onPause() {
        super.onPause()
        userViewModel.setFido2ApiClient(null)
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navGraphIds = listOf(R.navigation.login_register_navigation, R.navigation.help_navigation, R.navigation.settings_navigation)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_fragment,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        // sign user out if he or she goes back to summaryFragment (maybe to change data)
        val currentFragmentId = currentNavController?.value?.currentDestination?.id
        Log.d("CURRENTFRAGMENTTEST", "${currentFragmentId == R.id.navigation_register_key}")

        if(currentFragmentId == R.id.navigation_register_key){
            userViewModel.signOut()
            Log.d("SIGN_OUT", "User has been signed out")
        }
        return currentNavController?.value?.navigateUp() ?: false
    }
}