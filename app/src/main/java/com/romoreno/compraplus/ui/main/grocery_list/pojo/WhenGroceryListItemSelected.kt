package com.romoreno.compraplus.ui.main.grocery_list.pojo

import android.view.View

class WhenGroceryListItemSelected (
    val onCardViewSelected:(Int, View) -> Unit,
    val whenMoreOptionsSelected:(Int, View) -> Unit
)