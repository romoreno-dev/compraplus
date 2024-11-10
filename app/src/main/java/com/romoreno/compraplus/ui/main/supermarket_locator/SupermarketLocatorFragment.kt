package com.romoreno.compraplus.ui.main.supermarket_locator

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.romoreno.compraplus.R
import com.romoreno.compraplus.data.network.pojo.response.Result
import com.romoreno.compraplus.data.network.repository.GooglePlacesRepository
import com.romoreno.compraplus.databinding.FragmentSupermarketLocatorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO Implementar ViewModel
// TODO Refactorizar, "" Hacerlo mio ""

@AndroidEntryPoint
class SupermarketLocatorFragment : Fragment(), OnMapReadyCallback {

    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject lateinit var placesClient: PlacesClient
    @Inject lateinit var googlePlacesRepository: GooglePlacesRepository

    private val MALAGA = LatLng(36.72110175408074, -4.422031742145413)
    private val NEIGHBOORHOODS_ZOOM_LEVEL =15f

    private lateinit var map: GoogleMap

    private var _binding: FragmentSupermarketLocatorBinding? = null
    private val binding get() = _binding!!

    // Registra el lanzador para solicitar múltiples permisos
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableUserLocation()
            initializePlaces()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionExplanationDialog()
            } else {
                showSettingsDialog()
            }
        }
    }

    private fun initializePlaces() {
        //TODO
    }

    override fun onStart() {
        super.onStart()
        // Iniciar las actualizaciones de ubicación cuando el fragmento esté visible
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        // Detener las actualizaciones de ubicación cuando el fragmento no esté visible
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        // Solicitar las actualizaciones de ubicación
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        // Detener las actualizaciones de ubicación
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    val locationRequest = LocationRequest
        .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .build()


    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.location_permission_request_title))
            .setMessage(getString(R.string.location_permission_request_description))
            .setPositiveButton(getString(R.string.location_permission_request_action_grant)) { _, _ ->
                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton(getString(R.string.location_permission_request_action_deny)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initGoogleMaps()
        initListeners()
        initUIState()
    }

    private fun initGoogleMaps() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initUIState() {
        //todo Pendiente de implementar
    }

    private fun initListeners() {
        //todo Pendiente de implementar
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMinZoomPreference(10f)
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MALAGA, NEIGHBOORHOODS_ZOOM_LEVEL))
        activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupermarketLocatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        // Verifica los permisos al reanudar el fragmento
//        checkLocationPermission()
//    }

    private fun ifLocationPermissionIsGranted(onPermissionGranted: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun enableUserLocation() {
        if (::map.isInitialized) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

            centerMapOnUserLocation()  // Centra el mapa en la ubicación del usuario
            searchPlaces()
        }
    }

    private fun searchPlaces() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            location?.let {
                searchSupermarket("mercadona", LatLng(location.latitude, location.longitude), 4000)
            } ?: run {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.location_could_be_obtained),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    val moveCameraToLocation: (Location) -> Unit = { location ->
        val userLocation = LatLng(location.latitude, location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
    }

    private fun centerMapOnUserLocation() {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                location?.let {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
                } ?: run {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.location_could_be_obtained),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.location_permission_request_title))
            .setMessage(getString(R.string.location_permission_request_not_rationale_description))
            .setPositiveButton(getString(R.string.location_permission_request_not_rationale_action_grant)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            }
            .setCancelable(false)
            .setNegativeButton(getString(R.string.location_permission_request_not_rationale_action_deny)) { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.groceryListFragment)
            }
            .show()
    }

    private fun searchSupermarket(query: String, latLng: LatLng, radius: Int) {

        val diaBitmap = BitmapDescriptorFactory.fromResource(R.drawable.dia)
        val mercadonaBitmap = BitmapDescriptorFactory.fromResource(R.drawable.mercadona)
        val eroskiBitmap = BitmapDescriptorFactory.fromResource(R.drawable.eroski)

        lifecycleScope.launch {
            var placesResponse = googlePlacesRepository.get(latLng.latitude.toString()+","+latLng.longitude, radius, "Mercadona")
            placesResponse.let {
                withContext(Dispatchers.Main) {
                    map.clear()
                    addMarkersToMap(it, mercadonaBitmap)
                }
            }
            placesResponse = googlePlacesRepository.get(latLng.latitude.toString()+","+latLng.longitude, radius, "Eroski")
            placesResponse.let {
                withContext(Dispatchers.Main) {
                    addMarkersToMap(it, eroskiBitmap)
                }
            }
            placesResponse = googlePlacesRepository.get(latLng.latitude.toString()+","+latLng.longitude, radius, "Dia")
            placesResponse.let {
                withContext(Dispatchers.Main) {
                    addMarkersToMap(it, diaBitmap)
                }
            }
//            placesResponse = googlePlacesRepository.get(latLng.latitude.toString()+","+latLng.longitude, radius, "Carrefour Express")
//            placesResponse.let {
//                withContext(Dispatchers.Main) {
//                    addMarkersToMap(it, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                }
//            }

        }
    }

    private fun addMarkersToMap(places: List<Result>, bitmap: BitmapDescriptor) {
            // Limpiar los marcadores anteriores del mapa (si los hubiera)

            // Iterar por cada LatLng y agregar un marcador
            for (location in places) {
                // Agregar marcador
                map.addMarker(
                    MarkerOptions()
                        .icon(bitmap)
                        //.icon(if(location.openingHours.openNow) {BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE) } else {bitmap})
                        .position(LatLng(location.geometry.location.latitude, location.geometry.location.longitude))
                        .title(location.name) // Título que desees para cada marcador
                )
            }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            locationResult?.let {
                // Aquí se obtiene una lista de nuevas ubicaciones
                for (location in it.locations) {
                    // Cada vez que la ubicación cambia, actualiza la consulta a Google Places si es necesario
                    onLocationUpdated(location)
                }
            }
        }
    }

    // Función para calcular la distancia entre dos ubicaciones
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    var lastLocation: Location? = null
    var lastUpdateTime: Long = 0

    fun onLocationUpdated(location: Location) {
        val currentTime = System.currentTimeMillis()
        if (lastLocation == null || calculateDistance(lastLocation!!.latitude, lastLocation!!.longitude, location.latitude, location.longitude) > 500 || currentTime - lastUpdateTime > 60000) {
            // Si han pasado más de 60 segundos desde la última actualización o si la distancia es mayor a 500 metros
            lastLocation = location
            lastUpdateTime = currentTime
            // Hacer la consulta a la API de Google Places con la nueva ubicación
            searchPlaces()
            moveMapToLocation(location)
        }
    }

    private fun moveMapToLocation(location: Location) {
        // Mover la cámara del mapa a la nueva ubicación
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)  // 15f es el nivel de zoom
        map.animateCamera(cameraUpdate)
    }



}