package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.SupermarketEntity

/**
 * DAO para la tabla de supermercados
 *
 * @author Roberto Moreno
 */
@Dao
interface SupermarketDao : BaseDao<SupermarketEntity> {

    @Query("SELECT s.id FROM supermarket s WHERE name = :supermarketName")
    suspend fun getIdSupermarketFromName(supermarketName: String): Int

}
