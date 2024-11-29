package com.romoreno.compraplus.ui.main.grocery_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel del fragmento dedicado a mostrar el listado de listas de la compra
 *
 * @author: Roberto Moreno
 */
@HiltViewModel
class GroceryListViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) :
    ViewModel() {

    private var _state = MutableStateFlow<GroceryListState>(GroceryListState.Success(emptyList()))
    val state: StateFlow<GroceryListState> = _state

    fun getGroceryLists(user: FirebaseUser?) {
        viewModelScope.launch {
            _state.value = GroceryListState.Loading
            databaseRepository.getGroceryListsFromUser(user).collect { groceryLists ->
                _state.value = GroceryListState.Success(groceryLists)
            }
        }
    }

    suspend fun getGroceryListsWithProducts(idGroceryList: Int): GroceryListProductsModel? {
        return databaseRepository.getGroceryListWithProducts(idGroceryList)
    }

    suspend fun removeGroceryList(idGroceryList: Int) {
        withContext(Dispatchers.IO) {
            databaseRepository.deleteGroceryList(idGroceryList)
        }
    }

}
