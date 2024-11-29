package com.romoreno.compraplus.ui.main.product_comparator.view_model

import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

/**
 * Estado de la vista del comparador de productos.
 * Diferenciamos entre peticion exitosa, peticion en curso, peticion errónea y peticion para actualizar
 * los datos que ya existen en el RecyclerView de la vista.
 *
 * @author: Roberto Moreno
 */
sealed class ProductComparatorState {

    data class Success(val products: List<Product>) : ProductComparatorState()
    data object Error : ProductComparatorState()
    data object Loading : ProductComparatorState()
    data object Swipping : ProductComparatorState()

}
