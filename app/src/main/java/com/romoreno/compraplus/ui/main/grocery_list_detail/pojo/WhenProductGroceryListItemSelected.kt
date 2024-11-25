package com.romoreno.compraplus.ui.main.grocery_list_detail.pojo

import com.romoreno.compraplus.domain.model.ProductGroceryList

class WhenProductGroceryListItemSelected (
    val onCheckButtonSelected: (ProductGroceryList, Boolean) -> Unit
)