package com.romoreno.compraplus.domain

import android.util.Log
import com.romoreno.compraplus.data.database.dao.GroceryListDao
import com.romoreno.compraplus.data.database.dao.UserDao
import com.romoreno.compraplus.data.database.entities.UserEntity
import javax.inject.Inject


class UserMiddleware @Inject constructor(private val userDao: UserDao, private val groceryListDao: GroceryListDao) {

    suspend fun saveUser() {
        val user = UserEntity("123", "quinidio")
        userDao.insert(user)
    }

    suspend fun getAllUsers() {
        val list = userDao.getAllUsers()
        var list2 = groceryListDao.getGroceryListWithDetails(1)
        Log.i("romoreno", list.toString())
    }

}