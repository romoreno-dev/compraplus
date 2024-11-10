package com.romoreno.compraplus.data.database.mapper

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.entities.UserEntity

object ProductMapper {

    fun FirebaseUser.toUser(): UserEntity = UserEntity(uid, email!!)

//    fun com.romoreno.compraplus.data.database.entities.GroceryListEntity.toGroceryList() = GroceryList(id, name, date)
//
//    fun com.romoreno.compraplus.data.database.entities.ProductEntity.toProduct(): Product {
//
//        val prices = Prices(
//            price = price.toBigDecimal(),
//            unitPrice = price.toBigDecimal()
//        )
//
//        return Product(
//            name, prices,
//            Supermarket.Eroski.BASE_IMAGE_URL.plus(id).plus(".jpg"),
//            brand ?: "",
//            Supermarket.Eroski
//        )
//    }

}