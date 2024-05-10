package org.wit.gym.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.donationx.databinding.ContentGymMapsBinding
import ie.wit.donationx.main.DonationXApp
import ie.wit.donationx.R


class GymMapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var contentBinding: ContentGymMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var app: DonationXApp

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentBinding = ContentGymMapsBinding.inflate(inflater, container, false)
        return contentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = requireActivity().application as DonationXApp
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        configureMap()
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.gyms.findAll().forEach { gym ->
            val loc = LatLng(gym.lat, gym.lng)
            val options = MarkerOptions().position(loc).title(gym.title)
            map.addMarker(options)
        }

        // Optionally, move camera to the first marker
        if (app.gyms.findAll().isNotEmpty()) {
            val firstGym = app.gyms.findAll().first()
            val firstLoc = LatLng(firstGym.lat, firstGym.lng)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 10f))
        }
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        contentBinding.currentTitle.text = marker.title
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure you handle onDestroy for the map fragment properly if needed
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // Handle onLowMemory for the map fragment if needed
    }
}
