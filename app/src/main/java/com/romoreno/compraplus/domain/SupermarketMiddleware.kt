package com.romoreno.compraplus.domain

import com.google.android.gms.maps.model.LatLng
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.data.network.repository.PlaceRepository
import com.romoreno.compraplus.domain.model.SupermarketModel
import com.romoreno.compraplus.domain.model.toSupermarketModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SupermarketMiddleware @Inject constructor(private val repository: PlaceRepository) {

    suspend fun getNearbySupermarkets(latitude: Double, longitude: Double, radius: Int): List<SupermarketModel> {

        val location = "$latitude,$longitude"
        return coroutineScope {
            val supermarketDeferred = mutableListOf<Deferred<List<SupermarketModel>>>()

            for (market in Supermarket.values) {

                val supermarket = async {
                    runCatching {
                        repository.findNearbyPlaces(location, radius, market.name)
                            .map { it.toSupermarketModel(market) }
                    }
                        .getOrElse { emptyList() }
                }
                supermarketDeferred.add(supermarket)
            }
            supermarketDeferred.map { it.await() }.flatten()
        }
    }

}