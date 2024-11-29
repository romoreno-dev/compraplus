package com.romoreno.compraplus.ui.main.grocery_list.pojo

import android.view.View
import java.util.Date

/**
 * Contiene las lambdas que se pasan desde el GroceryListFragment al
 * GroceryListAdapter para que sean ejecutadas en los listeners de cada item en el
 * GroceryListViewHolder
 *
 * @author: Roberto Moreno
 */
class WhenGroceryListItemSelected(
    val onCardViewSelected: (Int) -> Unit,
    val whenMoreOptionsSelected: (Int, String, Date, View) -> Unit
)
