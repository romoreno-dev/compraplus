package com.romoreno.compraplus.ui.main.product_comparator.pojo

import com.romoreno.compraplus.data.database.entities.ProductEntity
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.domain.model.ProductModel
import java.math.RoundingMode

/**
 * Producto para mostrar en la vista
 *
 * @author: Roberto Moreno
 */
data class Product(
    val name: String,
    val prices: Prices,
    val image: String,
    val brand: String,
    val supermarket: Supermarket
)

data class Prices(
    val price: String,
    val unitPrice: String,
)

fun ProductModel.toProduct(): Product {
    val prices = Prices(
        price = "${prices.price.setScale(2, RoundingMode.HALF_UP)} €",
        unitPrice = if (prices.measureUnit.isNotBlank())
            "${prices.unitPrice.setScale(2, RoundingMode.HALF_UP)} €/${prices.measureUnit}" else ""
    )

    return Product(
        name, prices,
        image,
        brand,
        supermarket
    )
}

fun Product.toProductEntity(supermarketId: Int): ProductEntity {
    return ProductEntity(name = name, supermarketId = supermarketId, brand = brand, image = image)
}
