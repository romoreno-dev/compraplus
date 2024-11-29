package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.data.network.pojo.response.Result

/**
 * Repositorio de lugares para obtener lugares en base a las coordenadas, la query y el radio
 *
 * @author Roberto Moreno
 */
fun interface PlaceRepository {

    suspend fun findNearbyPlaces(location: String, radius: Int, query: String): List<Result>

}
