package com.romoreno.compraplus.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.databinding.ActivityLoginBinding
import com.romoreno.compraplus.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Actividad de Login. Aloja a LoginFragment (Login) y SignupFragment (Registro)
 * Si el login es exitoso, dirige a MainActivity
 *
 * @author Roberto Moreno
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewCompat()

        initUI()

        splashScreen.setKeepOnScreenCondition { false }
        // Si hay usuario de firebase, lo inserto en BBDD si no existe y lo llevo a MainActivity
        if (auth.currentUser != null) {
            lifecycleScope.launch {
                databaseRepository.insertUserIfNotExist(auth.currentUser!!)
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initUI() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentLogin) as NavHostFragment
        navController = navHost.navController
    }

    private fun setViewCompat() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.login) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

}
