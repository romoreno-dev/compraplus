package com.romoreno.compraplus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "product_line",
    primaryKeys = ["grocery_list_id", "product_id"],
    foreignKeys = [ForeignKey(
        entity = GroceryListEntity::class,
        parentColumns = ["id"],
        childColumns = ["grocery_list_id"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["product_id"])])
data class ProductLineEntity(
    @ColumnInfo(name = "grocery_list_id") val groceryListId: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "adquired") var adquired: Boolean
)
