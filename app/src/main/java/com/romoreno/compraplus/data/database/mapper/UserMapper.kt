package com.romoreno.compraplus.data.database.mapper

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.data.database.entities.UserEntity

/**
 * Mapper de usuarios a nivel de base de datos
 *
 * @author Roberto Moreno
 */
object UserMapper {

    fun FirebaseUser.toUser(): UserEntity = UserEntity(uid, email!!)

}
