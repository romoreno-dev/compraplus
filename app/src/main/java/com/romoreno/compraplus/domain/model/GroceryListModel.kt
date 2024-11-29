package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.database.entities.GroceryListEntity
import java.util.Date

/**
 * Modelo para logica de negocio de las listas de la compra
 *
 * @author Roberto Moreno
 */
data class GroceryListModel(
    val id: Int,
    val name: String,
    val date: Date
)

fun GroceryListEntity.toGroceryListModel(): GroceryListModel {
    return GroceryListModel(id, name, Date(date))
}
