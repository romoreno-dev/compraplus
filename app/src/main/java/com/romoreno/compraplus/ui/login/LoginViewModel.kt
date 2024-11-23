package com.romoreno.compraplus.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    fun insertUserIfNotExist(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertUserIfNotExist(user)
        }
    }

}