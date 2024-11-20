package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.database.dto.GroceryListWithProductLines

data class GroceryListProductsModel(
    val name: String,
    val productsMap: Map<String, List<ProductGroceryList>>
)

data class ProductGroceryList(
    val name: String,
    val quantity: Int,
    val adquired: Boolean,
    val supermarket: String
)

fun GroceryListWithProductLines.toGroceryListProductsModel(): GroceryListProductsModel {
    return GroceryListProductsModel(
        name = groceryList.name,
        productsMap = lines.map { v ->
            ProductGroceryList(
                name = v.productWithSupermarket?.product?.name ?: "",
                quantity = v.productLine?.quantity ?: 0,
                adquired = v.productLine?.adquired ?: false,
                supermarket = v.productWithSupermarket?.supermarket?.name ?: ""
                )
        }.groupBy { it.supermarket }
    )
}