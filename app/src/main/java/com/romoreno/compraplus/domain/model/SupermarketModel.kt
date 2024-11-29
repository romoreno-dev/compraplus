package com.romoreno.compraplus.domain.model

import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.response.Result

/**
 * Modelo para logica de negocio de supermercados
 *
 * @author Roberto Moreno
 */
data class SupermarketModel(
    val supermarket: Supermarket,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

fun Result.toSupermarketModel(supermarket: Supermarket): SupermarketModel {
    return SupermarketModel(
        supermarket,
        name,
        geometry.location.latitude,
        geometry.location.longitude
    )
}
