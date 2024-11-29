package com.romoreno.compraplus.data.network.repository.implementation

import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.pojo.response.Result
import com.romoreno.compraplus.data.network.repository.PlaceRepository
import com.romoreno.compraplus.data.network.service.GooglePlacesApiService
import javax.inject.Inject

/**
 * Implementacion de repositorio de lugares para realizar las llamadas HTTP a Google Maps
 *
 * Se realizan filtros especificos para los resultados de algunos supermercados porque devuelve
 * algunos lugares incorrectos
 *
 * @author Roberto Moreno
 */
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
                            (Supermarket.Dia.name == query &&
                                    (it.name.contains("dia", true) || it.name.contains(
                                        "día",
                                        true
                                    ))) ||
                            (Supermarket.Eroski.name == query && it.name.contains(
                                "Eroski City",
                                true
                            ))
                }
            }
        return emptyList()
    }

}

