package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.GroceryList
import com.romoreno.compraplus.data.database.entities.relations.GroceryListWithProductLines

@Dao
interface GroceryListDao: BaseDao<GroceryList> {

    @Query("SELECT * FROM grocery_list WHERE user_id = :userId")
    suspend fun getGroceryListsFromUser(userId: Int): GroceryList

    @Query("SELECT * FROM grocery_list WHERE id = :groceryListId")
    suspend fun getGroceryListWithProductLines(groceryListId: Int): GroceryListWithProductLines

}