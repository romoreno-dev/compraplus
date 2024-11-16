package com.romoreno.compraplus.ui.main.supermarket_locator.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.config.Supermarket
import com.romoreno.compraplus.domain.model.SupermarketModel
import javax.inject.Inject

class SupermarketLocatorUtils @Inject constructor() {

    fun initGoogleMaps(fragment: OnMapReadyCallback, childFragmentManager: FragmentManager) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(fragment)
    }

    fun calculateDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, results)
        return results[0]
    }

    fun getBitmap(supermarket: SupermarketModel): BitmapDescriptor {
        return when (supermarket.supermarket) {
            Supermarket.Dia -> BitmapDescriptorFactory.fromResource(R.drawable.dia)
            Supermarket.Eroski -> BitmapDescriptorFactory.fromResource(R.drawable.eroski)
            Supermarket.Mercadona -> BitmapDescriptorFactory.fromResource(R.drawable.mercadona)
        }
    }

    fun addMarkerToMap(map: GoogleMap, supermarket: SupermarketModel, bitmap: BitmapDescriptor) {
        map.addMarker(
            MarkerOptions()
                .icon(bitmap)
                .position(LatLng(supermarket.latitude, supermarket.longitude))
                .title(supermarket.name)
        )
    }

    fun moveMapToLocation(map: GoogleMap, latLng: LatLng, zoom: Float) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(
        fusedLocationClient: FusedLocationProviderClient,
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


}