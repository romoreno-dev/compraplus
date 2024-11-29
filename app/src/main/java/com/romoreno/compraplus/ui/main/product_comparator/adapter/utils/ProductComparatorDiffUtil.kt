package com.romoreno.compraplus.ui.main.product_comparator.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product

/**
 * DiffUtil para optimizar el renderizado del RecyclerView del comparador de productos
 *
 * @author: Roberto Moreno
 */
class ProductComparatorDiffUtil(
    private val oldList: List<Product>,
    private val newList: List<Product>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == (newList[newItemPosition].name)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
