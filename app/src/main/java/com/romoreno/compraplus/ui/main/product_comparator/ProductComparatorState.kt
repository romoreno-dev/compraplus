package com.romoreno.compraplus.ui.main.product_comparator

import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

sealed class ProductComparatorState {

    data class Success(val products:List<Product>):ProductComparatorState()
    data object Error:ProductComparatorState()
    data object Loading:ProductComparatorState()
    data object Swipping:ProductComparatorState()

}