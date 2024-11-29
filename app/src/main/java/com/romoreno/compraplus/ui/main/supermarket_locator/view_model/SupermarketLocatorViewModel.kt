package com.romoreno.compraplus.ui.main.supermarket_locator.view_model

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.romoreno.compraplus.domain.SupermarketMiddleware
import com.romoreno.compraplus.domain.model.SupermarketModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la localizacion de supermercados
 *
 * @author: Roberto Moreno
 */
@HiltViewModel
class SupermarketLocatorViewModel @Inject constructor(private val supermarketMiddleware: SupermarketMiddleware) :
    ViewModel() {

    companion object {
        const val PACKAGE_SCHEME = "package"
        const val MIN_ZOOM_LEVEL = 10f
        const val INITIAL_ZOOM_LEVEL = 15f
        const val WHEN_LOADED_ZOOM_LEVEL = 17f
        const val RADIUS = 2000
        const val DISTANCE_TO_NEW_CHARGE = 500
        const val RELOAD_POSITION_MILLISECONS_INTERVAL = 5000L
        val MALAGA_COORDINATES = LatLng(36.72110175408074, -4.422031742145413)
        val INITIAL_POSITION = MALAGA_COORDINATES
    }

    suspend fun getNearbySupermarkets(latitude: Double, longitude: Double): List<SupermarketModel> {
        return supermarketMiddleware.getNearbySupermarkets(latitude, longitude, RADIUS)
    }

}
