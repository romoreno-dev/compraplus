package com.romoreno.compraplus.ui.main.product_comparator.utils

import androidx.recyclerview.widget.DiffUtil
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

class ProductComparatorDiffUtil (
    private val oldList: List<Product>,
    private val newList: List<Product>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int  = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name.equals(newList[newItemPosition].name)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}