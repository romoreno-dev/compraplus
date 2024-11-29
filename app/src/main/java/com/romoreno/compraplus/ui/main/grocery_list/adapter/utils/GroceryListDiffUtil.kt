package com.romoreno.compraplus.ui.main.grocery_list.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.romoreno.compraplus.domain.model.GroceryListModel

/**
 * DiffUtil para optimizar el renderizado del RecyclerView del listado de listas de la compra
 *
 * @author: Roberto Moreno
 */
class GroceryListDiffUtil(
    private val oldList: List<GroceryListModel>,
    private val newList: List<GroceryListModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
