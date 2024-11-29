package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.dto.GroceryListWithProductLines
import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la tabla de listas de la compra
 *
 * @author Roberto Moreno
 */
@Dao
interface GroceryListDao : BaseDao<GroceryListEntity> {

    @Query("SELECT * FROM grocery_list WHERE user_id = :userUid ORDER BY date desc")
    @Transaction
    fun getGroceryListsFromUserId(userUid: String): Flow<List<GroceryListEntity>>

    @Query("SELECT * FROM grocery_list WHERE id = :groceryListId")
    @Transaction
    fun getGroceryListWithProductLines(groceryListId: Int): Flow<GroceryListWithProductLines>

    @Query("DELETE FROM grocery_list WHERE id = :groceryListId")
    @Transaction
    suspend fun deleteGroceryListWithId(groceryListId: Int)

}
