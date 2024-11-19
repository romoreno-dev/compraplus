package com.romoreno.compraplus.ui.main.grocery_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.databinding.ItemGroceryListBinding
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.ui.main.grocery_list.pojo.WhenGroceryListItemSelected
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroceryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGroceryListBinding.bind(view)

    fun render(
        groceryListModel: GroceryListModel,
        whenGroceryListItemSelected: WhenGroceryListItemSelected
    ) {
        binding.tvGroceryListName.text = groceryListModel.name
        binding.tvGroceryListDate.text = getDateFormatted(groceryListModel.date)

        binding.groceryListCardView.setOnClickListener { view -> whenGroceryListItemSelected
            .onCardViewSelected(groceryListModel.id, view) }
        binding.ivMore.setOnClickListener { view ->
            whenGroceryListItemSelected.whenMoreOptionsSelected(groceryListModel.id, view)
        }
    }

    private fun getDateFormatted(date: Date): String {
        //TODO Constantes... funciones de extension
        val numberDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(date)
        val dayStrDate = SimpleDateFormat("E", Locale.getDefault()).format(date)
        return "$numberDate, $dayStrDate"
    }

}