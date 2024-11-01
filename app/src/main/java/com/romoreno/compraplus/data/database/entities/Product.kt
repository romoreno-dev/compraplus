package com.romoreno.compraplus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "product",
    foreignKeys = [ForeignKey(
        entity = Supermarket::class,
        parentColumns = ["id"],
        childColumns = ["supermarket_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "supermarket_id") val supermarketId: Int,
    @ColumnInfo(name = "unit_price") val unitPrice: Double?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "brand") val brand: String?
)
//fixme no admite BigDecimal?
