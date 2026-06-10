package com.example.askidaayemek.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class qrFragment : Fragment(R.layout.fragment_qr) {
    private val TAG = "QRFragment"
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Log.d(TAG, "Tarama kullanıcı tarafından iptal edildi veya geri basıldı")
            safePopBack()
        } else {
            val data = result.contents
            Log.d(TAG, "QR OKUNDU (ZXING): $data")

            if (!data.isNullOrEmpty()) {
                val bundle = Bundle().apply {
                    putString("qr_verisi", data)
                }

                findNavController().navigate(
                    R.id.action_qrFragment2_to_yoneticiQrKodFragment,
                    bundle
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "QR'dan veri okunamadı",
                    Toast.LENGTH_SHORT
                ).show()
                safePopBack()
            }
        }
    }


    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startBarcodeScanner()
        } else {
            Toast.makeText(requireContext(), "Kamera izni gerekiyor", Toast.LENGTH_LONG).show()
            safePopBack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "qrFragment açıldı")
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startBarcodeScanner()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startBarcodeScanner() {
        try {
            val options = ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setCameraId(0)
                setBeepEnabled(true)
                setBarcodeImageEnabled(false)
                setOrientationLocked(true)
            }
            barcodeLauncher.launch(options)

        } catch (e: Exception) {
            Log.e(TAG, "Tarayıcı başlatılamadı", e)
            Toast.makeText(requireContext(), "Tarayıcı başlatılamadı", Toast.LENGTH_LONG).show()
            safePopBack()
        }
    }

    private fun safePopBack() {
        try {
            if (isAdded) findNavController().popBackStack()
        } catch (_: Exception) {
        }
    }
}