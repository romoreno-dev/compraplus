package com.romoreno.compraplus.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var utils: LoginUtils

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
        }

        binding.btSignup.setOnClickListener { signUp() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun signUp() {
        val completedFields = utils.validateCompletedFields(
            requireActivity(),
            binding.etEmail, binding.etPassword, binding.etRepeatPassword
        )
        if (!completedFields) return

        val equalsPasswords = utils.validateEqualsPassword(
            binding.etPassword, binding.etRepeatPassword
        )
        if (!equalsPasswords) {
            binding.etEmail.error = getString(R.string.passwords_are_not_equals)
            return
        }

        auth.createUserWithEmailAndPassword(
            binding.etEmail.editText!!.text.toString(),
            binding.etPassword.editText!!.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val message = getString(
                            R.string.verification_email_sent,
                            utils.ofuscateEmail(binding.etEmail.editText!!.text.toString())
                        )
                        findNavController()
                            .navigate(
                                SignupFragmentDirections.actionSignupFragmentToLoginFragment(
                                    message
                                )
                            )
                    } else {
                        val message = utils.catchFirebaseException(requireActivity(), it.exception)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                val message = utils.catchFirebaseException(requireActivity(), it.exception)
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }


}