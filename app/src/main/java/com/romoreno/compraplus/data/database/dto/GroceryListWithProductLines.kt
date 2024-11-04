package com.romoreno.compraplus.data.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.ProductEntity
import com.romoreno.compraplus.data.database.entities.ProductLineEntity

data class GroceryListWithProductLines(
    @Embedded
    val groceryList: GroceryListEntity,
    @Relation(
        entity = ProductLineEntity::class,
        parentColumn = "id",
        entityColumn = "grocery_list_id"
    )
    val lines: List<ProductLineWithProduct>
)

data class ProductLineWithProduct(
    @Embedded
    val productLine: ProductLineEntity? = null,
    @Relation(
        entity = ProductEntity::class,
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: ProductEntity? = null
)

