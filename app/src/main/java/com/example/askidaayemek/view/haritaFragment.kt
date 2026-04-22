package com.example.askidaayemek.view

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.askidaayemek.databinding.FragmentHaritaBinding
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import java.util.Locale

class haritaFragment : Fragment() {

    private var _binding: FragmentHaritaBinding? = null
    private val binding get() = _binding!!
    private var mapLibreMap: MapLibreMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapLibre.getInstance(requireContext())
        _binding = FragmentHaritaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.haritaView.onCreate(savedInstanceState)

        binding.haritaView.getMapAsync { map ->
            mapLibreMap = map
            map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.9334, 32.8597), 11.0))
            }
        }

        binding.aramaButonu.setOnClickListener {
            val arananMekan = binding.aramaEditText.text.toString().trim()
            if (arananMekan.isNotEmpty()) {
                mekanBulVeDetayliIsaretle(arananMekan)
                binding.aramaEditText.setText("")
            }
        }

        binding.aramaEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.aramaButonu.performClick()
                true
            } else false
        }
    }

    private fun mekanBulVeDetayliIsaretle(mekanAdi: String) {
        val geocoder = Geocoder(requireContext(), Locale("tr"))

        try {
            val sonuclar = geocoder.getFromLocationName("$mekanAdi Ankara", 1)

            if (!sonuclar.isNullOrEmpty()) {
                val adresNesnesi = sonuclar[0]
                val konum = LatLng(adresNesnesi.latitude, adresNesnesi.longitude)
                val tamAdres = adresNesnesi.getAddressLine(0)

                mapLibreMap?.let { map ->
                    map.clear()
                    val marker = map.addMarker(
                        MarkerOptions()
                            .position(konum)
                            .title(mekanAdi.uppercase())
                            .snippet(tamAdres)
                    )
                    map.selectMarker(marker)

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(konum, 16.0))
                }
            } else {
                Toast.makeText(context, "Adres bulunamadı Reis", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "İnternetin Gitti yeniden bağlan KRAL", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onStart()  //Başlama
    {
        super.onStart();
        binding.haritaView.onStart()
    }

    override fun onResume() //Sürdürme
    {
        super.onResume();
        binding.haritaView.onResume()
    }

    override fun onPause() //Duraklatma
    {
        super.onPause();
        binding.haritaView.onPause()
    }

    override fun onStop() //Durmak
    {
        super.onStop();
        binding.haritaView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.haritaView.onDestroy()
        _binding = null
    }
}