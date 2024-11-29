package com.romoreno.compraplus.ui.login

import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.romoreno.compraplus.R
import javax.inject.Inject

/**
 * Utilidades del modulo de login
 *
 * @author Roberto Moreno
 */
class LoginUtils @Inject constructor() {

    companion object {
        const val MULTIPLE_INDEX_CHARACTERS_THAT_ARE_VISIBLE = 3
    }

    fun catchFirebaseException(fragmentActivity: FragmentActivity, exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthEmailException -> fragmentActivity.getString(R.string.auth_email_exception)
            is FirebaseAuthWeakPasswordException -> fragmentActivity.getString(R.string.auth_weak_password_exception)
            is FirebaseAuthInvalidCredentialsException -> fragmentActivity
                .getString(R.string.auth_invalid_credentials_exception)

            is FirebaseAuthInvalidUserException -> fragmentActivity.getString(R.string.auth_invalid_user_exception)
            is FirebaseAuthUserCollisionException -> fragmentActivity.getString(R.string.auth_user_collision_exception)
            is FirebaseNetworkException -> fragmentActivity.getString(R.string.network_exception)
            else -> fragmentActivity.getString(R.string.auth_exception)
        }
    }

    fun validateCompletedFields(
        fragmentActivity: FragmentActivity,
        vararg fields: TextInputLayout
    ): Boolean {
        var completed = true
        for (field in fields) {
            val text = field.editText?.text?.toString()
            if (text.isNullOrEmpty()) {
                field.error = fragmentActivity.getString(R.string.required_field)
                completed = false
            } else {
                field.error = null
            }
        }
        return completed
    }

    fun validateEqualsPassword(
        firstPassword: TextInputLayout,
        secondPassword: TextInputLayout
    ): Boolean {
        firstPassword.error = null
        secondPassword.error = null
        return firstPassword.editText?.text?.toString()
            .equals(secondPassword.editText?.text?.toString())
    }

    fun ofuscateEmail(email: String): String {
        var passedArroba = false
        var ofuscatedEmail = ""
        for (i in email.indices) {
            if (!passedArroba && email[i] != '@') {
                if (i % MULTIPLE_INDEX_CHARACTERS_THAT_ARE_VISIBLE == 0) {
                    ofuscatedEmail += email[i]
                } else {
                    ofuscatedEmail += "*"
                }
            } else {
                ofuscatedEmail += email[i]
                if (!passedArroba) passedArroba = true
            }
        }
        return ofuscatedEmail
    }

}
