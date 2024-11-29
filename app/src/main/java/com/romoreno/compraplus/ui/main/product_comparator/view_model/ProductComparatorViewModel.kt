package com.romoreno.compraplus.ui.main.product_comparator.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romoreno.compraplus.domain.ProductMiddleware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel del comparador de precios de productos
 *
 * @author: Roberto Moreno
 */
@HiltViewModel
class ProductComparatorViewModel @Inject constructor(private val productMiddleware: ProductMiddleware) :
    ViewModel() {

    private var _state = MutableStateFlow<ProductComparatorState>(
        ProductComparatorState
            .Success(emptyList())
    )
    val state: StateFlow<ProductComparatorState> = _state

    fun searchProduct(productKeyword: String) {
        searchProduct(productKeyword, false)
    }

    fun searchProduct(productKeyword: String, withSwipe: Boolean) {
        viewModelScope.launch {
            _state.value = if (withSwipe) {
                ProductComparatorState.Swipping
            } else {
                ProductComparatorState.Loading
            }
            val products =
                withContext(Dispatchers.IO) { productMiddleware.getProducts(productKeyword) }
            _state.value = ProductComparatorState.Success(products)
        }
    }

    fun toSuccessState() {
        viewModelScope.launch {
            _state.value = ProductComparatorState.Success(emptyList())
        }
    }

}
