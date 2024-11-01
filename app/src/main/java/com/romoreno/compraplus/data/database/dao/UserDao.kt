package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.User

@Dao
interface UserDao: BaseDao<User> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(quotes: User)

}