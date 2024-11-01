package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.Product

@Dao
interface ProductDao : BaseDao<Product> {

    @Query("SELECT * FROM product WHERE name LIKE '%' || :keywordProduct || '%'")
    suspend fun getProducts(keywordProduct: String): List<Product>

}