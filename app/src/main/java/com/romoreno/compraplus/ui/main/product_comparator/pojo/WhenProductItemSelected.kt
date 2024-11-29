package com.romoreno.compraplus.ui.main.product_comparator.pojo

/**
 * Contiene las lambdas que se pasan desde el ProductComparatorFragment al
 * ProductComparadorAdapter para que sean ejecutadas en los listeners de cada item en el
 * ProductComparatorViewHolder
 *
 * @author: Roberto Moreno
 */
data class WhenProductItemSelected(
    val onCardViewSelected: (Product) -> Unit,
    val onProductImageSelected: (Product) -> Unit,
)
