package com.romoreno.compraplus.ui.main.product_comparator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.R
import com.romoreno.compraplus.ui.main.product_comparator.utils.ProductComparatorDiffUtil
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.WhenItemRecyclerViewSelected

class ProductComparatorAdapter (private val whenItemRecyclerViewSelected: WhenItemRecyclerViewSelected,
                                private var list:List<Product> = emptyList()):
    RecyclerView.Adapter<ProductComparatorViewHolder>() {

    fun updateList(newList: List<Product>) {
        val productDiff = ProductComparatorDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(productDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductComparatorViewHolder {
        return ProductComparatorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_comparator, parent, false))
    }

    override fun onBindViewHolder(holder: ProductComparatorViewHolder, position: Int) {
        holder.render(list[position], whenItemRecyclerViewSelected)
    }

    override fun getItemCount() = list.size
}