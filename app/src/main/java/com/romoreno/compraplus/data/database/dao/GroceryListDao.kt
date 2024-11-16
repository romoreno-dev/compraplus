package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.dto.GroceryListWithProductLines
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryListDao: BaseDao<GroceryListEntity> {

    @Query("SELECT * FROM grocery_list WHERE user_id = :userUid")
    @Transaction
    fun getGroceryListsFromUser(userUid: Int): Flow<List<GroceryListEntity>>

    @Query("SELECT * FROM grocery_list WHERE id = :id")
    @Transaction
    fun getGroceryListWithDetails(id: Int): Flow<GroceryListWithProductLines>
}