package com.romoreno.compraplus.ui.main.product_comparator.pojo

data class WhenProductItemSelected (
    val onCardViewSelected:(Product) -> Unit,
    val onProductImageSelected:(Product) -> Unit,
)
