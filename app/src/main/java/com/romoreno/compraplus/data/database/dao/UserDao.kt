package com.romoreno.compraplus.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.romoreno.compraplus.data.database.dao.base.BaseDao
import com.romoreno.compraplus.data.database.entities.UserEntity

@Dao
interface UserDao: BaseDao<UserEntity> {

    @Query("SELECT * FROM user WHERE id = :userUid")
    suspend fun getUserByUid(userUid: String): UserEntity?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>

}