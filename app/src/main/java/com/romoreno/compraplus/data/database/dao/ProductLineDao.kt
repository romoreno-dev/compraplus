package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.ProductLineEntity

/**
 * DAO para la tabla de líneas de producto en las listas de la compra
 *
 * @author Roberto Moreno
 */
@Dao
interface ProductLineDao : BaseDao<ProductLineEntity> {

    @Query("SELECT * FROM product_line WHERE grocery_list_id = :groceryListId AND product_id = :idProduct")
    suspend fun getProductLine(groceryListId: Int, idProduct: Int): ProductLineEntity?

    @Query("DELETE FROM product_line WHERE grocery_list_id = :groceryListId AND product_id = :idProduct")
    @Transaction
    suspend fun deleteProductLine(groceryListId: Int, idProduct: Int)

}
