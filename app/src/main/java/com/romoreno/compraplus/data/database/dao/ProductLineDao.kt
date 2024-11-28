package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.ProductLineEntity

@Dao
interface ProductLineDao : BaseDao<ProductLineEntity> {

    @Query("SELECT * FROM product_line WHERE grocery_list_id = :groceryListId AND product_id = :idProduct")
    suspend fun getProductLine(groceryListId: Int, idProduct: Int): ProductLineEntity?

    @Query("DELETE FROM product_line WHERE grocery_list_id = :groceryListId AND product_id = :idProduct")
    suspend fun deleteProductLine(groceryListId: Int, idProduct: Int)

}