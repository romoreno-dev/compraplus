package com.romoreno.compraplus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.entities.GroceryList
import com.romoreno.compraplus.data.database.entities.Product
import com.romoreno.compraplus.data.database.entities.ProductLine
import com.romoreno.compraplus.data.database.entities.Supermarket
import com.romoreno.compraplus.data.database.entities.User

@Database(entities = [GroceryList::class, Product::class, ProductLine::class,
                     Supermarket::class, User::class], version = 1)
abstract class CompraPlusDatabase: RoomDatabase() {

    abstract fun getGroceryListDao():GroceryListDao
    abstract fun getProductDao():ProductDao
    abstract fun getUserDao():UserDao

}