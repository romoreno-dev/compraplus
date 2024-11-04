package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.SupermarketEntity

@Dao
interface SupermarketDao: BaseDao<SupermarketEntity> {
}
