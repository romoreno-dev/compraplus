package com.romoreno.compraplus.ui.main.grocery_list_detail.adapter

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.ItemProductGroceryListBinding
import com.romoreno.compraplus.domain.model.ProductGroceryList
import com.romoreno.compraplus.ui.main.Utils
import com.romoreno.compraplus.ui.main.grocery_list_detail.pojo.WhenProductGroceryListItemSelected

class ProductGroceryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemProductGroceryListBinding.bind(view)

    fun render(
        product: ProductGroceryList,
        whenProductGroceryListItemSelected: WhenProductGroceryListItemSelected
    ) {
        binding.tvProduct.text = product.name
        binding.tvQuantity.text = "${product.quantity}"
        binding.imageViewSupermarket
            .setImageResource(Utils.getSupermarketImageResource(product.supermarket))

        binding.cbAdquired.setOnCheckedChangeListener(null)
        binding.cbAdquired.isChecked = product.adquired

        binding.imageViewDelete
            .setImageResource(R.drawable.ic_delete)
        binding.imageViewDelete.setOnClickListener {
            whenProductGroceryListItemSelected.onDeleteButtonSelected(product)
        }

        if (binding.cbAdquired.isChecked) {
            binding.tvProduct.paintFlags =
                binding.tvQuantity.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.tvProduct.paintFlags =
                binding.tvQuantity.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        binding.cbAdquired.setOnCheckedChangeListener { _, isChecked ->
            whenProductGroceryListItemSelected.onCheckButtonSelected(product, isChecked)
        }
        binding.productCardView.setOnLongClickListener {
            whenProductGroceryListItemSelected.onClickListener(product)
            true
        }
    }

}