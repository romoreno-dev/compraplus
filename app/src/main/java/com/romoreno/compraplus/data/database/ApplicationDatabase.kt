package com.romoreno.compraplus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.ProductDao
import com.romoreno.compraplus.data.database.dao.ProductLineDao
import com.romoreno.compraplus.data.database.dao.SupermarketDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.ProductEntity
import com.romoreno.compraplus.data.database.entities.ProductLineEntity
import com.romoreno.compraplus.data.database.entities.SupermarketEntity
import com.romoreno.compraplus.data.database.entities.UserEntity

@Database(entities = [GroceryListEntity::class, ProductEntity::class, ProductLineEntity::class,
                     SupermarketEntity::class, UserEntity::class], version = 1, exportSchema = false
    )
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun getGroceryListDao():GroceryListDao
    abstract fun getProductDao():ProductDao
    abstract fun getProductLineDao():ProductLineDao
    abstract fun getSupermarketDao():SupermarketDao
    abstract fun getUserDao():UserDao

}