package com.romoreno.compraplus.ui.main.grocery_list_detail.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.romoreno.compraplus.domain.model.ProductGroceryList

class ProductGroceryListDiffUtil (
    private val oldList: List<ProductGroceryList>,
    private val newList: List<ProductGroceryList>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].productId == newList[newItemPosition].productId)
                && (oldList[oldItemPosition].groceryListId == newList[newItemPosition].groceryListId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}