package com.romoreno.compraplus.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.romoreno.compraplus.data.database.entities.GroceryList
import com.romoreno.compraplus.data.database.entities.ProductLine

data class GroceryListWithProductLines(
    @Embedded val groceryList: GroceryList,
    @Relation(
        parentColumn = "id",
        entityColumn = "grocery_list_id"
    )
    val productLines: List<ProductLine>
)