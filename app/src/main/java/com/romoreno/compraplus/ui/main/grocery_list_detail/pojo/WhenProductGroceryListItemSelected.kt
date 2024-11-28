package com.romoreno.compraplus.ui.main.grocery_list_detail.pojo

import com.romoreno.compraplus.domain.model.ProductGroceryList

class WhenProductGroceryListItemSelected (
    val onClickListener: (ProductGroceryList) -> Unit,
    val onCheckButtonSelected: (ProductGroceryList, Boolean) -> Unit,
    val onDeleteButtonSelected: (ProductGroceryList) -> Unit
)