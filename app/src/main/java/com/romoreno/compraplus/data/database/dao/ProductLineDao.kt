package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.ProductLineEntity

@Dao
interface ProductLineDao: BaseDao<ProductLineEntity> {

    @Query("SELECT * FROM grocery_list")
    suspend fun getProductLines(): List<GroceryListEntity>

}