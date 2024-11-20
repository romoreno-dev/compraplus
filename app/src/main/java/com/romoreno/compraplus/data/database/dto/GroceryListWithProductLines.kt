package com.romoreno.compraplus.data.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.ProductEntity
import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.data.database.entities.SupermarketEntity

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
    val productWithSupermarket: ProductWithSupermarket? = null
)

data class ProductWithSupermarket(
    @Embedded
    val product: ProductEntity? = null,
    @Relation(
        entity = SupermarketEntity::class,
        parentColumn = "supermarket_id",
        entityColumn = "id"
    )
    val supermarket: SupermarketEntity? = null
)



