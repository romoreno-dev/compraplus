package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import com.romoreno.compraplus.data.database.entities.SupermarketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupermarketDao: BaseDao<SupermarketEntity> {

    @Query("SELECT s.id FROM supermarket s WHERE name = :name")
    fun getIdFromName(name: String): Int

}
