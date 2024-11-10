package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import java.util.Date

data class GroceryListModel(
    val id: Int,
    val name: String,
    val date: Date
)

fun GroceryListEntity.toGroceryListModel(): GroceryListModel {
    return GroceryListModel(id, name, Date(date))
}

//data class ProductLine(
//    val quantity: Int ,
//    val adquired: Boolean,
//    val product: ProductList
//)
//
//data class ProductList(
//    val name: String,
//    val brand: String,
//    val supermarket: String,
//    val urlImage: String
//)
//
//data class Supermarket(
//    val id: Int,
//    val name: String
//)


