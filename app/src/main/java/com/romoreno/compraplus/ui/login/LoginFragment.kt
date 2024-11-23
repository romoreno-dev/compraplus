package com.romoreno.compraplus.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var utils: LoginUtils

    @Inject
    lateinit var databaseRepository: DatabaseRepository

    val args: LoginFragmentArgs by navArgs()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initTexts()
        initListeners()
    }

    private fun initTexts() {
        binding.tvInfoMessage.text = args.infoMessage
        if (args.infoMessage.isNotBlank()) {
            binding.etEmail.editText?.text = null
            binding.etPassword.editText?.text = null
        }
    }

    private fun initListeners() {
        binding.tvSignUp.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        binding.btLogin.setOnClickListener { login() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun login() {
        binding.tvInfoMessage.text = null
        val completedFields =
            utils.validateCompletedFields(requireActivity(), binding.etEmail, binding.etPassword)

        if (completedFields) {
            auth.signInWithEmailAndPassword(
                binding.etEmail.editText!!.text!!.toString(),
                binding.etPassword.editText!!.text!!.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {

                    if (!auth.currentUser!!.isEmailVerified) {
                        auth.signOut()
                        val message = getString(R.string.auth_must_verify_email)
                        findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentSelf(infoMessage = message))
                    } else {
                        lifecycleScope.launch {
                            databaseRepository.insertUserIfNotExist(auth.currentUser!!)
                        }
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                    }
                } else {
                    val message = utils.catchFirebaseException(requireActivity(), it.exception)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}