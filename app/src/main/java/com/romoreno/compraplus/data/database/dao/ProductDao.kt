package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.ProductEntity

/**
 * DAO para la tabla de productos
 *
 * @author Roberto Moreno
 */
@Dao
interface ProductDao : BaseDao<ProductEntity> {

    @Query(
        """
        SELECT p.* FROM product p
        JOIN supermarket s ON p.supermarket_id = s.id 
        WHERE p.name = :nameProduct 
        AND s.name = :supermarket
        """
    )
    suspend fun getProduct(nameProduct: String, supermarket: String): ProductEntity?

}
