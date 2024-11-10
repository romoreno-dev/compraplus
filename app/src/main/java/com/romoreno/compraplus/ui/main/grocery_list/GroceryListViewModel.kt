package com.romoreno.compraplus.ui.main.grocery_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryListViewModel @Inject constructor(private val databaseRepository: DatabaseRepository): ViewModel() {

    private var _state = MutableStateFlow<GroceryListState>(GroceryListState.Success(emptyList()))
    val state: StateFlow<GroceryListState> = _state

    fun getGroceryLists(user: FirebaseUser?) {
        viewModelScope.launch {
            _state.value = GroceryListState.Loading
            databaseRepository.getGroceryListsFromUser(user).collect { groceryLists ->
                if (groceryLists != null) {
                    _state.value = GroceryListState.Success(groceryLists)
                } else {
                    _state.value = GroceryListState.Error
                }
            }
        }
    }

}