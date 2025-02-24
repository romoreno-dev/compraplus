package com.romoreno.compraplus.ui.main.grocery_list_detail.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romoreno.compraplus.data.database.repository.DatabaseRepository
import com.romoreno.compraplus.domain.ProductUseCase
import com.romoreno.compraplus.domain.model.GroceryListProductsModel
import com.romoreno.compraplus.domain.model.ProductGroceryList
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel de la actividad dedicada a mostrar y administrar los productos contenidos en una
 * lista de la compra
 *
 * @author: Roberto Moreno
 */
@HiltViewModel
class GroceryListDetailViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val productUseCase: ProductUseCase
) :
    ViewModel() {

    private var _state = MutableStateFlow(
        ProductGroceryListState(
            GroceryListProductsModel(
                name = "", productsMap = emptyMap()
            ), false
        )
    )
    val state: StateFlow<ProductGroceryListState> = _state

    fun getGroceryListDetails(idGroceryList: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            databaseRepository.getGroceryListWithProductsFlow(idGroceryList)
                .collect { productsGroceryList ->
                    _state.value = ProductGroceryListState(productsGroceryList, false)
                }
        }
    }

    fun markProductAsAdquired(productGroceryList: ProductGroceryList, checked: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            databaseRepository.markProductAsAdquired(
                productGroceryList.groceryListId,
                productGroceryList.productId, checked
            )
            _state.value = _state.value.copy(loading = false)
        }
    }

    fun deleteProductInGroceryList(productGroceryList: ProductGroceryList) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            databaseRepository.deleteProduct(
                productGroceryList.groceryListId,
                productGroceryList.productId
            )
            _state.value = _state.value.copy(loading = false)
        }
    }

    fun saveProductLine(groceryListId: Int, quantity: Int, product: Product) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            withContext(Dispatchers.IO) {
                productUseCase.insertOrUpdateProductLine(groceryListId, quantity, product)
            }
            _state.value = _state.value.copy(loading = false)
        }
    }


}
