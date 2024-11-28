package com.romoreno.compraplus.ui.main.grocery_list

import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

fun interface OnProductSelectedCallback {
    fun onProductSelected(quantity: Int, product: Product)
}