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

    @Query("SELECT * FROM grocery_list WHERE user_id = :userUid ORDER BY date desc")
    @Transaction
    fun getGroceryListsFromUserId(userUid: String): Flow<List<GroceryListEntity>>

    @Query("SELECT * FROM grocery_list WHERE id = :id")
    @Transaction
    fun getGroceryListWithDetails(id: Int): Flow<GroceryListWithProductLines>

    @Query("DELETE FROM grocery_list WHERE id = :id")
    @Transaction
    fun deleteGroceryListWithId(id: Int)



}