package com.romoreno.compraplus.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.BuildConfig
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.ActivityMainBinding
import com.romoreno.compraplus.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var datastorePreferences: DatastorePreferences

    @Inject
    lateinit var auth: FirebaseAuth

    companion object {
        val URL_GITHUB = "https://github.com/romoreno-dev"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        val darkMode = runBlocking { datastorePreferences.getNightModePreference(auth.currentUser!!.uid) }
        setDarkMode(darkMode)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewCompat()
        initUI()
        splashScreen.setKeepOnScreenCondition { false }
    }

    private fun initUI() {
        initToolbar()
        initNavigation()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.topAppBar)
    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.groceryListFragment -> binding.topAppBar
                    .title = getString(R.string.grocery_list_menu)

                R.id.productComparatorFragment -> binding.topAppBar
                    .title = getString(R.string.product_comparator_menu)

                R.id.supermarketLocatorFragment -> binding.topAppBar
                    .title = getString(R.string.supermarket_locator_menu)
            }
        }
    }

    private fun setViewCompat() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                showAboutDialog()
                true
            }

            R.id.nightMode -> {
                changeDarkMode()
                true
            }

            R.id.logout -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showAboutDialog() {

        val message = "${getString(R.string.about_message)} ${BuildConfig.VERSION_NAME}"

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.about))
            .setMessage(
                HtmlCompat.fromHtml(
                    message,
                    HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                )
            )
            .setIcon(R.drawable.ic_ilerna)
            .setPositiveButton(getString(R.string.accept), null)
            .setNeutralButton(getString(R.string.see_github), { _, _ ->
                goToGithub()
            })
            .show()
    }

    private fun goToGithub() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_GITHUB))
        startActivity(intent)
    }


    private fun changeDarkMode() {
        val wantsDarkMode =
            !(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        setDarkMode(wantsDarkMode)
        CoroutineScope(Dispatchers.IO).launch {
            datastorePreferences.setNightModePreference(wantsDarkMode, auth.currentUser!!.uid)
        }
    }

    private fun setDarkMode(darkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}