package com.romoreno.compraplus.ui.main.grocery_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.databinding.ItemGroceryListBinding
import com.romoreno.compraplus.domain.model.GroceryListModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroceryListViewHolder(view: View): RecyclerView.ViewHolder(view)  {

    private val binding = ItemGroceryListBinding.bind(view)

    fun render(groceryListModel: GroceryListModel) {
        binding.tvGroceryListName.text = groceryListModel.name
        binding.tvGroceryListDate.text = getDateFormatted(groceryListModel.date)
    }

    private fun getDateFormatted(date: Date): String {
        //TODO Constantes... funciones de extension
        val numberDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(date)
        val dayStrDate = SimpleDateFormat("E", Locale.getDefault()).format(date)
        return "$numberDate, $dayStrDate"
    }

}