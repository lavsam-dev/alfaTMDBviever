package com.learn.lavsam.alfatmdbviewer.view

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.GoogleMapsFragmentBinding
import com.learn.lavsam.alfatmdbviewer.model.data.Actor
import kotlinx.android.synthetic.main.google_maps_fragment.*
import java.io.IOException

private const val MAPS_ZOOM = 15f
private const val MAPS_LINE_WIDTH = 5f

// Beirut
private const val INITIAL_PLACE_LATITUDE = 33.89491309445804
private const val INITIAL_PLACE_LONGITUDE = 35.503766602426815

class GoogleMapsFragment : Fragment() {
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private lateinit var actorBundle: Actor

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(INITIAL_PLACE_LATITUDE, INITIAL_PLACE_LONGITUDE)
        googleMap.addMarker(
            MarkerOptions().position(initialPlace).title(getString(R.string.marker_start))
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng)
            addMarkerToArray(latLng)
            drawLine()
        }
    }

    private var _binding: GoogleMapsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GoogleMapsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorBundle = arguments?.getParcelable<Actor>(BUNDLE_EXTRA) ?: Actor()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress(actorBundle.placeOfBirth.toString())
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.apply {
                        textAddress.post { textAddress.text = addresses[0].getAddressLine(0) }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(MAPS_LINE_WIDTH)
            )
        }
    }

    private fun initSearchByAddress(searchString: String) {
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
//            val searchText = searchString
            val searchText = binding.searchAddress.text.toString()
            searchAddress.setText(searchText)
            Thread {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.size > 0) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String
    ) {
        val location = LatLng(
            addresses.first().latitude,
            addresses.first().longitude
        )
        view.post {
            setMarker(location, searchText, R.drawable.ic_map_marker)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    MAPS_ZOOM
                )
            )
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "actor"
        fun newInstance(bundle: Bundle): GoogleMapsFragment {
            val fragment = GoogleMapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}