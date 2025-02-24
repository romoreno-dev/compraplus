package com.romoreno.compraplus.ui.main.grocery_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.databinding.ItemGroceryListBinding
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.ui.main.grocery_list.pojo.WhenGroceryListItemSelected
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewHolder del RecyclerView del listado de listas de la compra
 *
 * @author: Roberto Moreno
 */
class GroceryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGroceryListBinding.bind(view)

    fun render(
        groceryListModel: GroceryListModel,
        whenGroceryListItemSelected: WhenGroceryListItemSelected
    ) {
        binding.tvGroceryListName.text = groceryListModel.name
        binding.tvGroceryListDate.text = getDateFormatted(groceryListModel.date)

        binding.groceryListCardView.setOnClickListener {
            whenGroceryListItemSelected
                .onCardViewSelected(groceryListModel.id)
        }
        binding.ivMoreEvent.setOnClickListener { view ->
            whenGroceryListItemSelected.whenMoreOptionsSelected(
                groceryListModel.id, groceryListModel.name,
                groceryListModel.date, view
            )
        }
    }

    private fun getDateFormatted(date: Date): String {
        val numberDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(date)
        val dayStrDate = SimpleDateFormat("E", Locale.getDefault()).format(date)
        return "$numberDate, $dayStrDate"
    }

}
