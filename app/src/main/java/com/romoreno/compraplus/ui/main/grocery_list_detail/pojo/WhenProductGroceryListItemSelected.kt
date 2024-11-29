package com.romoreno.compraplus.ui.main.grocery_list_detail.pojo

import com.romoreno.compraplus.domain.model.ProductGroceryList

/**
 * Contiene las lambdas que se pasan desde el GroceryListDetailActivity al
 * ProductGroceryListAdapter para que sean ejecutadas en los listeners de cada item en el
 * ProductGroceryListViewHolder
 *
 * @author: Roberto Moreno
 */
class WhenProductGroceryListItemSelected(
    val onClickListener: (ProductGroceryList) -> Unit,
    val onCheckButtonSelected: (ProductGroceryList, Boolean) -> Unit,
    val onDeleteButtonSelected: (ProductGroceryList) -> Unit
)
