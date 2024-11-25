package com.romoreno.compraplus.ui.main.grocery_list.pojo

import android.view.View
import java.util.Date

class WhenGroceryListItemSelected (
    val onCardViewSelected:(Int) -> Unit,
    val whenMoreOptionsSelected:(Int, String, Date, View) -> Unit
)