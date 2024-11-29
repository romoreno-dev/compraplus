package com.romoreno.compraplus.ui.main.grocery_list_detail.view_model

import com.romoreno.compraplus.domain.model.GroceryListProductsModel

/**
 * Estado de la vista del listado de productos contenido en una lista de la compra
 * Se utiliza para comunicar por parte del GroceryListDetailViewModel los productos contenidos en una lista
 * de la compra determinada, procedentes de la base de datos de la aplicación
 *
 * @author: Roberto Moreno
 */
data class ProductGroceryListState(
    val groceryListProductsModel: GroceryListProductsModel,
    val loading: Boolean
)
