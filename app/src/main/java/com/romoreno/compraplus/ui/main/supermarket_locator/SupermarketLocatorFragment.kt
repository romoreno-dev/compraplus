package com.romoreno.compraplus.ui.main.supermarket_locator

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.romoreno.compraplus.R
import com.romoreno.compraplus.databinding.FragmentSupermarketLocatorBinding
import com.romoreno.compraplus.ui.main.supermarket_locator.utils.SupermarketLocatorUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SupermarketLocatorFragment : Fragment(), OnMapReadyCallback {

    private val supermarketLocatorViewModel: SupermarketLocatorViewModel by viewModels()

    private lateinit var map: GoogleMap

    private var lastLocation: Location? = null

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var utils: SupermarketLocatorUtils

    private var _binding: FragmentSupermarketLocatorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupermarketLocatorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utils.initGoogleMaps(this, childFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        utils.startLocationUpdates(fusedLocationClient, locationRequest, locationCallback)
    }

    override fun onStop() {
        super.onStop()
        utils.stopLocationUpdates(fusedLocationClient, locationCallback)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMinZoomPreference(SupermarketLocatorViewModel.MIN_ZOOM_LEVEL)
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
        )
        utils.moveMapToLocation(
            map, SupermarketLocatorViewModel.INITIAL_POSITION,
            SupermarketLocatorViewModel.INITIAL_ZOOM_LEVEL
        )
        activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Lanzador para solicitar el permiso de ubicacion
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableUserLocationAndDrawPlaces()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionExplanationDialog()
            } else {
                showSettingsDialog()
            }
        }
    }

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

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.location_permission_request_title))
            .setMessage(getString(R.string.location_permission_request_not_rationale_description))
            .setPositiveButton(getString(R.string.location_permission_request_not_rationale_action_grant)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts(
                        SupermarketLocatorViewModel.PACKAGE_SCHEME,
                        requireContext().packageName,
                        null
                    )
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

    @SuppressLint("MissingPermission")
    private fun enableUserLocationAndDrawPlaces() {
        if (::map.isInitialized) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

            centerMapOnUserLocation()
            searchAndDrawSupermarkets()
        }
    }

    @SuppressLint("MissingPermission")
    private fun centerMapOnUserLocation() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            location?.let {
                val userLocation = LatLng(location.latitude, location.longitude)
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        userLocation,
                        SupermarketLocatorViewModel.WHEN_LOADED_ZOOM_LEVEL
                    )
                )
            } ?: run {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.location_could_be_obtained),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchAndDrawSupermarkets() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            location?.let {
                lifecycleScope.launch {
                    val supermarkets = supermarketLocatorViewModel
                        .getNearbySupermarkets(location.latitude, location.longitude)
                    withContext(Dispatchers.Main) {
                        supermarkets.forEach {
                            utils.addMarkerToMap(map, it, utils.getBitmap(it))
                        }
                    }
                }
            } ?: run {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.location_could_be_obtained),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val locationRequest = LocationRequest
        .Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            SupermarketLocatorViewModel.RELOAD_POSITION_MILLISECONS_INTERVAL
        )
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            locationResult.let {
                for (location in it.locations) {
                    if (lastLocation == null || utils.calculateDistance(
                            lastLocation!!.latitude,
                            lastLocation!!.longitude,
                            location.latitude,
                            location.longitude
                        ) > SupermarketLocatorViewModel.DISTANCE_TO_NEW_CHARGE
                    ) {
                        lastLocation = location
                        searchAndDrawSupermarkets()
                        utils.moveMapToLocation(
                            map, LatLng(location.latitude, location.longitude),
                            SupermarketLocatorViewModel.WHEN_LOADED_ZOOM_LEVEL
                        )
                    }
                }
            }
        }
    }

}