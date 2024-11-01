package com.romoreno.compraplus.ui.finder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romoreno.compraplus.domain.ProductMiddleware
import com.romoreno.compraplus.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FinderViewModel @Inject constructor(private val productMiddleware: ProductMiddleware) :
    ViewModel() {

    private val _state = MutableStateFlow<FinderState>(FinderState.Success(""))
    val state: StateFlow<FinderState> = _state


    fun getProducts(productKeyword: String) {
        viewModelScope.launch {
            _state.value = FinderState.Loading
            val products =
                withContext(Dispatchers.IO) { productMiddleware.getProducts(productKeyword) }
            if (products != null) {
                val text = processProductsList(products)
                _state.value = FinderState.Success(text)
            } else {
                _state.value = FinderState.Error("Ha ocurrido un error, intentelo mas tarde")
            }
        }
    }

    private fun processProductsList(products: List<Product>): String {
        val sb: StringBuilder = StringBuilder()
        var i = 0
        products.forEach { product ->
            sb.append("${i++} - Nombre: ${product.name} Precio: ${product.prices.price} Marca: ${product.brand} Supermercado: ${product.supermarket.name}\n")
        }
        return sb.toString()
    }
}