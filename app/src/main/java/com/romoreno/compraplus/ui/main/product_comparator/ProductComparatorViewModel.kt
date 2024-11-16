package com.romoreno.compraplus.ui.main.product_comparator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.romoreno.compraplus.domain.ProductMiddleware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductComparatorViewModel @Inject constructor(private val productMiddleware: ProductMiddleware) :
    ViewModel() {

    private var _state = MutableStateFlow<ProductComparatorState>(
        ProductComparatorState
            .Success(emptyList())
    )
    val state: StateFlow<ProductComparatorState> = _state

    fun searchProduct(productKeyword: String) {
        viewModelScope.launch {
            _state.value = ProductComparatorState.Loading
            val products =
                withContext(Dispatchers.IO) { productMiddleware.getProducts(productKeyword) }
            _state.value = ProductComparatorState.Success(products)
        }
    }

}