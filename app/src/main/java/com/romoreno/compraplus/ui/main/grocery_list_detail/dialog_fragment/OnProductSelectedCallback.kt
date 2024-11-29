package com.romoreno.compraplus.ui.main.grocery_list_detail.dialog_fragment

import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

/**
 * Interfaz Callback implementada por GroceryListDetailActivity y mediante la cual los DialogFragment
 * ProductAdderDialogFragment y ProductComparatorFragment pueden devolverle el valor del
 * producto seleccionado
 *
 * Para ello, el DialogFragment guardará el contexto de procedencia cuando ejecuta su
 * función onAttach
 *
 * @author: Roberto Moreno
 */
fun interface OnProductSelectedCallback {
    fun onProductSelected(quantity: Int, product: Product)
}
