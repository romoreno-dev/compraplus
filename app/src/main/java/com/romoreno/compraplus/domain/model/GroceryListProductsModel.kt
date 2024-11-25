package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.database.dto.GroceryListWithProductLines
import com.romoreno.compraplus.data.network.config.Supermarket

data class GroceryListProductsModel(
    val name: String,
    val productsMap: Map<String, List<ProductGroceryList>>
)

data class ProductGroceryList(
    val name: String,
    val productId: Int,
    val groceryListId: Int,
    val quantity: Int,
    val adquired: Boolean,
    val supermarket: Supermarket?
)

fun GroceryListWithProductLines.toGroceryListProductsModel(): GroceryListProductsModel {
    return GroceryListProductsModel(
        name = groceryList.name,
        productsMap = lines.map { v ->
            ProductGroceryList(
                name = v.productWithSupermarket?.product?.name ?: "",
                quantity = v.productLine?.quantity ?: 0,
                adquired = v.productLine?.adquired ?: false,
                supermarket = Supermarket.fromString(v.productWithSupermarket?.supermarket?.name),
                productId = v.productLine?.productId ?: 0,
                groceryListId = v.productLine?.groceryListId ?: 0
                )
        }.groupBy { it.supermarket?.name ?: "" }
    )
}