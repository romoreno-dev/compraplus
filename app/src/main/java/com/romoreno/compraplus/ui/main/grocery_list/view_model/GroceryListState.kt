package com.romoreno.compraplus.ui.main.grocery_list.view_model

import com.romoreno.compraplus.domain.model.GroceryListModel

/**
 * Estado de la vista del listado de listas de la compra
 * Se utiliza para comunicar por parte del GroceryListViewModel el listado de listas de la compra d
 * de un usuario, procedentes de la base de datos de la aplicación
 *
 * @author: Roberto Moreno
 */
sealed class GroceryListState {

    data class Success(val groceryListModels: List<GroceryListModel>) : GroceryListState()
    data object Error : GroceryListState()
    data object Loading : GroceryListState()

}
