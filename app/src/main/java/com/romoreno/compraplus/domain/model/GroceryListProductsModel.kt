package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.database.dto.GroceryListWithProductLines

data class GroceryListProductsModel(
    val name: String,
    val products: List<ProductGroceryList>
)

data class ProductGroceryList(
    val name: String,
    val quantity: Int,
    val adquired: Boolean,
)

fun GroceryListWithProductLines.toGroceryListProductsModel(): GroceryListProductsModel {
    return GroceryListProductsModel(
        name = groceryList.name,
        products = lines.map { v ->
            ProductGroceryList(
                name = v.product?.name ?: "",
                quantity = v.productLine?.quantity ?: 0,
                adquired = v.productLine?.adquired ?: false,
                )
        }
    )
}