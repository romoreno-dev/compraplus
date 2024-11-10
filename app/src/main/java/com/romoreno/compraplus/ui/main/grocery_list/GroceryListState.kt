package com.romoreno.compraplus.ui.main.grocery_list

import com.romoreno.compraplus.domain.model.GroceryListModel

sealed class GroceryListState {

    data class Success(val groceryListModels: List<GroceryListModel>) : GroceryListState()
    data object Error : GroceryListState()
    data object Loading : GroceryListState()

}