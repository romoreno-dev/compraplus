package com.romoreno.compraplus.data.network.repository

import com.romoreno.compraplus.data.network.pojo.response.Result

interface PlaceRepository {

    suspend fun findNearbyPlaces(location: String, radius: Int, query: String): List<Result>

}