package com.romoreno.compraplus.ui.main.product_comparator.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.databinding.ItemProductComparatorBinding
import com.romoreno.compraplus.ui.main.Utils
import com.romoreno.compraplus.ui.main.product_comparator.pojo.Product
import com.romoreno.compraplus.ui.main.product_comparator.pojo.WhenProductItemSelected

class ProductComparatorViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemProductComparatorBinding.bind(view)

    fun render(product: Product, whenProductItemSelected: WhenProductItemSelected) {
        binding.tvProductTitle.text = product.name
        binding.tvProductPrice.text = product.prices.price
        binding.tvProductUnitPrice.text = product.prices.unitPrice
        binding.imageViewSupermarket
            .setImageResource(Utils.getSupermarketImageResource(product.supermarket))

        Glide.with(itemView)
            .load(product.image)
            .into(binding.imageViewProduct)


        binding.productCardView.setOnClickListener {
            whenProductItemSelected.onCardViewSelected(product)
        }

        binding.imageViewProduct.setOnClickListener {
            whenProductItemSelected.onProductImageSelected(product)
        }
    }

    //TODO Descripcion acccesibilidad. Fotografias...
}