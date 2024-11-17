package com.romoreno.compraplus.data.network.repository.implementation

import com.romoreno.compraplus.chore.LogUtils
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.response.Result
import com.romoreno.compraplus.data.network.repository.PlaceRepository
import com.romoreno.compraplus.data.network.service.GooglePlacesApiService
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(private val googlePlacesApiService: GooglePlacesApiService) :
    PlaceRepository {

    override suspend fun findNearbyPlaces(
        location: String, radius: Int,
        query: String
    ): List<Result> {

        runCatching {
            googlePlacesApiService.findPlaces(
                location = location,
                radius = radius,
                query = query
            )
        }
            .onSuccess {
                return it.results.filter {
                    it.name.equals(query, true) ||
                            (Supermarket.Dia.name.equals(query) &&
                                    (it.name.contains("dia", true) || it.name.contains("día", true))) ||
                            (Supermarket.Eroski.name.equals(query) && it.name.contains(
                                "Eroski City",
                                true))
                }
            }
            .onFailure { LogUtils.networkLog(it) }
        return emptyList()
    }

}

