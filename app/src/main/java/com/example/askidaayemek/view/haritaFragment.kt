package com.example.askidaayemek.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentHaritaBinding
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style

class haritaFragment : Fragment(R.layout.fragment_harita) {

    private var _binding: FragmentHaritaBinding? = null
    private val binding get() = _binding!!
    private var mapLibreMap: MapLibreMap? = null
    private val TAG = "HaritaFragment"
    private var isYonetici: Boolean = false
    private var yoneticiKayitKoordinat: LatLng? = null
    private var hedefKoordinat = LatLng(39.9208, 32.8541)
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapLibreMap?.getStyle { style ->
                konumServisiniBaslat(style)
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Konum izni reddedildi. Yol tarifi gösterilemiyor.",
                Toast.LENGTH_SHORT
            ).show()
            if (!isYonetici) sadeceHedefiGoster() else varsayilanKonumYukle()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(requireContext())


        arguments?.let { bundle ->
            if (bundle.containsKey("isYonetici")) {
                isYonetici = bundle.getBoolean("isYonetici", false)
            }

            val gelenKonum = bundle.getString("hedefKonum")
            if (!gelenKonum.isNullOrEmpty()) {
                try {
                    val parts = gelenKonum.split(",")
                    if (parts.size == 2) {
                        hedefKoordinat =
                            LatLng(parts[0].trim().toDouble(), parts[1].trim().toDouble())
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Koordinat format hatası: $gelenKonum", e)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHaritaBinding.bind(view)
        binding.haritaView.onCreate(savedInstanceState)


        if (isYonetici) {
            binding.haritaToolbar.title = "Konum Belirle"
            binding.buttonKonumuKaydet.text = "KONUMU KAYDET"
            binding.buttonKonumuKaydet.visibility = View.VISIBLE
        } else {
            binding.haritaToolbar.title = "Harita"
            binding.buttonKonumuKaydet.visibility = View.GONE


            val params = binding.haritaView.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = 0
            binding.haritaView.layoutParams = params
        }

        setupListeners()

        binding.haritaView.getMapAsync { map ->
            if (_binding == null) return@getMapAsync
            mapLibreMap = map

            if (!isYonetici) {
                map.uiSettings.isScrollGesturesEnabled = false
                map.uiSettings.isZoomGesturesEnabled = true
                map.uiSettings.isTiltGesturesEnabled = false
                map.uiSettings.isRotateGesturesEnabled = false
            }

            val styleUrl = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
            map.setStyle(styleUrl) { style ->
                if (_binding == null) return@setStyle
                Log.d(TAG, "Harita stili başarıyla yüklendi")
                konumKontrolunuBaslat(style)
            }

            map.addOnMapClickListener {
                if (isYonetici && _binding != null) {
                    Toast.makeText(
                        requireContext(),
                        "Mevcut GPS konumunuz otomatik kilitlenmiştir. Değiştirmek için cihaz konumunu yenileyin.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
        }
    }

    private fun setupListeners() {
        binding.haritaToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonKonumuKaydet.setOnClickListener {
            if (isYonetici) {
                yoneticiKayitKoordinat?.let { koordinat ->
                    val konumMetni = "${koordinat.latitude}, ${koordinat.longitude}"
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "secilenKonum",
                        konumMetni
                    )
                    Toast.makeText(requireContext(), "Konum Askıya Alındı", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "GPS konumunuz hala saptanıyor lütfen açık alana çıkıp bekleyin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun konumKontrolunuBaslat(style: Style) {
        if (_binding == null) return
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            konumServisiniBaslat(style)
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun konumServisiniBaslat(style: Style) {
        val map = mapLibreMap ?: return
        try {
            val locationComponent = map.locationComponent
            val options = LocationComponentActivationOptions.builder(requireContext(), style)
                .useDefaultLocationEngine(true)
                .build()
            locationComponent.activateLocationComponent(options)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.NONE
            locationComponent.renderMode = RenderMode.COMPASS

            val mevcutKonum = locationComponent.lastKnownLocation
            if (mevcutKonum != null) {
                val canliLatLng = LatLng(mevcutKonum.latitude, mevcutKonum.longitude)
                haritaModunuTetikle(canliLatLng)
            } else {
                map.addOnCameraIdleListener(object : MapLibreMap.OnCameraIdleListener {
                    override fun onCameraIdle() {
                        if (_binding == null) return
                        val yedekKonum = locationComponent.lastKnownLocation
                        if (yedekKonum != null && yoneticiKayitKoordinat == null) {
                            val canliLatLng = LatLng(yedekKonum.latitude, yedekKonum.longitude)
                            haritaModunuTetikle(canliLatLng)
                            map.removeOnCameraIdleListener(this)
                        }
                    }
                })

                if (!isYonetici) sadeceHedefiGoster() else varsayilanKonumYukle()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Konum katmanı başlatılamadı", e)
            if (!isYonetici) sadeceHedefiGoster() else varsayilanKonumYukle()
        }
    }

    private fun haritaModunuTetikle(canliLatLng: LatLng) {
        if (_binding == null) return
        if (isYonetici) {
            yoneticiKonumunuSabitle(canliLatLng)
        } else {
            rotaVeSureHesapla(canliLatLng, hedefKoordinat)
        }
    }

    private fun yoneticiKonumunuSabitle(canliKonum: LatLng) {
        val map = mapLibreMap ?: return
        map.clear()
        yoneticiKayitKoordinat = canliKonum

        val marker = map.addMarker(
            MarkerOptions()
                .position(canliKonum)
                .title("Konum")
                .snippet("Kaydet butonuna basarak bu noktayı sabitleyin")
        )
        map.selectMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(canliKonum, 16.0))
    }

    private fun rotaVeSureHesapla(baslangic: LatLng, bitis: LatLng) {
        val map = mapLibreMap ?: return
        map.clear()
        map.addMarker(MarkerOptions().position(baslangic).title("Sizin Konumunuz"))
        val mesafeMetre = baslangic.distanceTo(bitis)
        val tahminiDakika = (mesafeMetre / 600).toInt().coerceAtLeast(1)
        val sureMetni = if (tahminiDakika >= 60) {
            val saat = tahminiDakika / 60
            val dk = tahminiDakika % 60
            "Mesafe: ${(mesafeMetre / 1000).toInt()} km | Tahmini: $saat saat $dk dk sonra varılır"
        } else {
            "Mesafe: ${mesafeMetre.toInt()} metre | Tahmini: $tahminiDakika dk sonra varılır"
        }
        val marker = map.addMarker(
            MarkerOptions()
                .position(bitis)
                .title("Teslima Alış Noktası")
                .snippet(sureMetni)
        )
        map.selectMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(bitis, 14.5))
    }

    private fun sadeceHedefiGoster() {
        val map = mapLibreMap ?: return
        map.clear()
        val marker = map.addMarker(
            MarkerOptions().position(hedefKoordinat).title("Hedef Noktası")
                .snippet("Konumunuza erişilemedi.")
        )
        map.selectMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(hedefKoordinat, 13.5))
    }

    private fun varsayilanKonumYukle() {
        val map = mapLibreMap ?: return
        val varsayilanLatLng = LatLng(39.9334, 32.8597)
        yoneticiKayitKoordinat = varsayilanLatLng
        map.clear()
        val marker =
            map.addMarker(MarkerOptions().position(varsayilanLatLng).title("Konum Aranıyor..."))
        map.selectMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(varsayilanLatLng, 12.0))
    }

    override fun onStart() {
        super.onStart()
        _binding?.haritaView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        _binding?.haritaView?.onResume()
    }

    override fun onPause() {
        _binding?.haritaView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        _binding?.haritaView?.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.haritaView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.haritaView?.onLowMemory()
    }

    override fun onDestroyView() {
        _binding?.haritaView?.onDestroy()
        super.onDestroyView()
        _binding = null
    }
}