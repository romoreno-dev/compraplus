package com.romoreno.compraplus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.getCredential
import com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber
import com.google.firebase.auth.UserProfileChangeRequest
import com.romoreno.compraplus.databinding.ActivityMainBinding
import com.romoreno.compraplus.domain.model.Product
import com.romoreno.compraplus.ui.finder.FinderState
import com.romoreno.compraplus.ui.finder.FinderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val finderViewModel: FinderViewModel by viewModels()
    private var products: List<Product> = emptyList() // Almacena los productos

    private val RC_SIGN_IN = 9001
    private var storeId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

//        // Configurar el botón de inicio de sesión
//        binding.googleSignInButtonId.setOnClickListener {
//            //signIn()
//        }
//        binding.btSend.setOnClickListener {
//            //startPhoneNumberVerification(binding.etPhoneNumber.text.toString())
//        }
//        binding.btCodigo.setOnClickListener {
//            //verifyCode(binding.etCodeSend.text.toString(), storeId)
//        }
//        binding.btLogEmail.setOnClickListener {
//            //logEmail()
//        }
//        binding.btRegistrate.setOnClickListener {
//            //registrate()
//        }
        binding.btBuscar.setOnClickListener{
            buscar()
        }

        initUIState()

    }

    fun initUIState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                finderViewModel.state.collect { state ->
                    when (state) {
                        FinderState.Loading -> loadingState()
                        is FinderState.Error -> errorState()
                        is FinderState.Success -> successState(state)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.progress.isVisible = true
    }

    private fun errorState() {
        binding.progress.isVisible = false
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
    }

    private fun successState(state: FinderState.Success) {
        binding.progress.isVisible = false
        binding.tvTexto.text = state.products
    }


    fun buscar() {
        binding.tvMessage.text = "Mi mensaje"
        val productKeyword = binding.etKeyword.text.toString()
        if (productKeyword.isNotEmpty()) {
            finderViewModel.getProducts(productKeyword)
        }
    }

//    fun logEmail() {
//        auth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Toast.makeText(this, "SIIIIIIIIIIU", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "NOP KO", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    fun registrate() {
//        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnCompleteListener {
//            if (it.isSuccessful) {
//                Toast.makeText(this, "Te registraste wei", Toast.LENGTH_LONG).show()
//                val user = auth.currentUser
//                user?.sendEmailVerification()
//                    ?.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("VerificationEmail", "Correo de verificación enviado a: ${user.email}")
//                            Toast.makeText(this, "Correo de verificación enviado. Por favor, verifica tu correo.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.e("VerificationEmail", "Error al enviar correo de verificación: ${task.exception?.message}")
//                        }
//                    }
//            } else {
//                Toast.makeText(this, "NOP KO", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Ya estabas logado!!!", Toast.LENGTH_LONG)
        }
    }

    // Iniciar el Intent de Google Sign-In
    private fun signIn() {

        // Cierra sesión de Firebase
        FirebaseAuth.getInstance().signOut()

        // Cierra sesión de Google y revoca el acceso para solicitar una nueva cuenta
        googleSignInClient.signOut().addOnCompleteListener(this) {
            googleSignInClient.revokeAccess().addOnCompleteListener(this) {
                // Inicia el flujo de inicio de sesión después de cerrar la sesión y revocar acceso
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                println("Error en Google Sign-In: ${e.message}")
            }
        }
    }

    // Autenticar con Firebase usando el token de Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser
                    Toast.makeText(
                        this,
                        "Inicio de sesión exitoso: ${user?.displayName}",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, OtherActivity::class.java)
                    startActivity(intent)
                } else {
                    println("Error en Firebase Authentication: ${task.exception}")
                }
            }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Número de teléfono (incluye código de país)
            .setTimeout(60L, TimeUnit.SECONDS) // Tiempo de espera para el SMS
            .setActivity(this) // Actividad actual
            .setCallbacks(callbacks) // Callbacks definidos
            .build()
        verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto-retrieval or instant verification succeeded
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Verification failed
            Log.e("Auth", "Error en la verificación: ${e.message}")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The code has been sent to the phone number, save verification ID and resend token
            storeId = verificationId
//            resendToken = token
            Log.d("Auth", "Código enviado: $verificationId")
        }
    }

    // Sign in with phone credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let {
                        Log.d("CurrentDisplayName", "Nombre actual: ${it.displayName}")

                        // Crear la solicitud de actualización
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName("Pepepotamo") // Nombre nuevo
                            .build()

                        // Actualizar el perfil del usuario
                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("ProfileUpdate", "Perfil actualizado exitosamente")
                                    Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show()

                                    // Verifica el nuevo nombre de visualización
                                    Log.d("NewDisplayName", "Nuevo nombre: ${it.displayName}")
                                } else {
                                    Log.e("ProfileUpdate", "Error al actualizar perfil: ${task.exception?.message}")
                                    Toast.makeText(this, "Error al actualizar perfil: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } ?: run {
                        Log.e("ProfileUpdate", "No se encontró al usuario")
                    }

                } else {
                    // Sign in failed
                    Log.e("Auth", "Error de inicio de sesión", task.exception)
                }
            }
    }

    // Verify the code manually if the user enters it
    fun verifyCode(code: String, phone: String) {
        val credential = getCredential(phone, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()
        verifyPhoneNumber(options)
    }


    private fun updateDisplayName() {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("UserAuth", "Usuario autenticado: ${user?.uid}")

        user?.let {
            Log.d("CurrentDisplayName", "Nombre actual: ${it.displayName}")

            // Crear la solicitud de actualización
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("Roberto") // Nombre nuevo
                .build()

            // Actualizar el perfil del usuario
            it.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ProfileUpdate", "Perfil actualizado exitosamente")
                        Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT)
                            .show()

                        // Verifica el nuevo nombre de visualización
                        Log.d("NewDisplayName", "Nuevo nombre: ${it.displayName}")
                    } else {
                        Log.e(
                            "ProfileUpdate",
                            "Error al actualizar perfil: ${task.exception?.message}"
                        )
                        Toast.makeText(
                            this,
                            "Error al actualizar perfil: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } ?: run {
            Log.e("ProfileUpdate", "No se encontró al usuario")
        }
    }

}