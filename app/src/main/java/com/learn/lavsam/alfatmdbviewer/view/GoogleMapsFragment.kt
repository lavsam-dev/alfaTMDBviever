package com.learn.lavsam.alfatmdbviewer.view

import android.graphics.Color
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
import java.io.IOException

private const val MAPS_ZOOM = 10f
private const val MAPS_LINE_WIDTH = 5f

class GoogleMapsFragment : Fragment() {
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private lateinit var actorBundle: Actor

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val placeOfBirth = actorBundle.placeOfBirth.toString()
        val initialPlace = searchLatLng(placeOfBirth)
        map.addMarker(
            MarkerOptions().position(initialPlace).title(placeOfBirth)
        )
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                initialPlace,
                MAPS_ZOOM
            )
        )
        map.setOnMapLongClickListener { latLng ->
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

    private fun searchLatLng(searchString: String): LatLng {
        val geoCoder = Geocoder(binding.root.context)
        try {
            val addresses = geoCoder.getFromLocationName(searchString, 1)
            if (addresses.size > 0) {
                val location = LatLng(
                    addresses.first().latitude,
                    addresses.first().longitude
                )
                return location
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return LatLng(0.0, 0.0)
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