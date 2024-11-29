package com.romoreno.compraplus.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de usuarios
 *
 * @author Roberto Moreno
 */
@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val uid: String,
    @ColumnInfo(name = "name") val name: String
)
