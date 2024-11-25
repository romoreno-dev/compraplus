package com.romoreno.compraplus.ui.main.grocery_list_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.R
import com.romoreno.compraplus.domain.model.ProductGroceryList
import com.romoreno.compraplus.ui.main.grocery_list_detail.adapter.utils.ProductGroceryListDiffUtil
import com.romoreno.compraplus.ui.main.grocery_list_detail.pojo.WhenProductGroceryListItemSelected

class ProductGroceryListAdapter(private val whenProductGroceryListItemSelected: WhenProductGroceryListItemSelected,
    private var list:List<ProductGroceryList> = emptyList()) :
        RecyclerView.Adapter<ProductGroceryListViewHolder>() {

     fun updateList(newList: List<ProductGroceryList>) {
         val groceryListDetailsDiff = ProductGroceryListDiffUtil(list, newList)
         val result = DiffUtil.calculateDiff(groceryListDetailsDiff)
         list = newList
         result.dispatchUpdatesTo(this)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGroceryListViewHolder {
        return ProductGroceryListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_grocery_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductGroceryListViewHolder, position: Int) {
        holder.render(list[position], whenProductGroceryListItemSelected)
    }

    override fun getItemCount(): Int = list.size
}