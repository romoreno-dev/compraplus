package com.romoreno.compraplus.ui.main.grocery_list_detail

import com.romoreno.compraplus.domain.model.GroceryListProductsModel

data class ProductGroceryListState (
    val groceryListProductsModel: GroceryListProductsModel,
    val loading: Boolean
)