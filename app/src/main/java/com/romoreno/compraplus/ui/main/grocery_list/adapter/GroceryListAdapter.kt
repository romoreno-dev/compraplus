package com.romoreno.compraplus.ui.main.grocery_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.R
import com.romoreno.compraplus.domain.model.GroceryListModel
import com.romoreno.compraplus.ui.main.grocery_list.pojo.WhenGroceryListItemSelected
import com.romoreno.compraplus.ui.main.grocery_list.adapter.utils.GroceryListDiffUtil

class GroceryListAdapter(private val whenGroceryListItemSelected: WhenGroceryListItemSelected,
                         private var list:List<GroceryListModel> = emptyList()) :
    RecyclerView.Adapter<GroceryListViewHolder>() {

    fun updateList(newList: List<GroceryListModel>) {
        val groceryListDiff = GroceryListDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(groceryListDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        return GroceryListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_grocery_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroceryListViewHolder, position: Int) {
        holder.render(list[position], whenGroceryListItemSelected)
    }

    override fun getItemCount() = list.size

}