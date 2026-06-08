package com.example.askidaayemek.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentHaritaBinding
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style

class haritaFragment : Fragment() {

    private var _binding: FragmentHaritaBinding? = null
    private val binding get() = _binding!!
    private var mapLibreMap: MapLibreMap? = null
    private var currentMarker: Marker? = null
    private var isYoneticiModu = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapLibre.getInstance(requireContext())
        _binding = FragmentHaritaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.gosterLoading(true)

        binding.haritaToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        val oncekiFragment = findNavController().previousBackStackEntry?.destination?.id
        isYoneticiModu = (oncekiFragment == R.id.urunEkleFragment)

        binding.haritaView.onCreate(savedInstanceState)
        binding.haritaView.getMapAsync { map ->
            mapLibreMap = map
            map.setStyle(
                Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")
            ) { style ->
                (activity as? MainActivity)?.gosterLoading(false)

                val ankara = LatLng(39.9334, 32.8597)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara, 12.0))

                arguments?.getString("hedefKonum")?.split(",")?.let { coords ->
                    if (coords.size == 2) {
                        val lat = coords[0].trim().toDoubleOrNull() ?: 39.9334
                        val lng = coords[1].trim().toDoubleOrNull() ?: 32.8597
                        iğneEkle(LatLng(lat, lng), "Ürün Konumu")
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15.0))
                    }
                }

                if (isYoneticiModu) {
                    map.addOnMapClickListener { latLng ->
                        iğneEkle(latLng, "Seçilen Konum")
                        true
                    }
                    map.addOnMapLongClickListener { _ ->
                        iğneSil()
                        true
                    }
                }
            }
        }
    }

    private fun iğneEkle(latLng: LatLng, title: String) {
        mapLibreMap?.let { map ->
            currentMarker?.let { map.removeMarker(it) }
            currentMarker = map.addMarker(MarkerOptions().position(latLng).title(title))
            map.selectMarker(currentMarker!!)

            if (isYoneticiModu) {
                Toast.makeText(context, "Konum belirlendi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iğneSil() {
        if (!isYoneticiModu) return
        mapLibreMap?.let { map ->
            currentMarker?.let {
                map.removeMarker(it)
                currentMarker = null
                Toast.makeText(context, "Konum silindi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart(); binding.haritaView.onStart()
    }

    override fun onResume() {
        super.onResume(); binding.haritaView.onResume()
    }

    override fun onPause() {
        super.onPause(); binding.haritaView.onPause()
    }

    override fun onStop() {
        super.onStop(); binding.haritaView.onStop()
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView(); binding.haritaView.onDestroy(); _binding = null
    }
}