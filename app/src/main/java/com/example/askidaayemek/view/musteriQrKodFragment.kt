package com.example.askidaayemek.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class musteriQrKodFragment : Fragment(R.layout.fragment_musteri_qr_kod) {

    private var _binding: FragmentMusteriQrKodBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusteriQrKodBinding.bind(view)

        binding.geriDonImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            val qrBitmap = qrOlustur(currentUser.uid)
            binding.qrKodOkuyucusu.setImageBitmap(qrBitmap)
        } else {
            val testBitmap = qrOlustur("TEST_VERISI_OTURUM_YOK")
            binding.qrKodOkuyucusu.setImageBitmap(testBitmap)
        }
    }

    private fun qrOlustur(veriler: String): Bitmap {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(veriler, BarcodeFormat.QR_CODE, 512, 512)
            val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
            for (x in 0 until 512) {
                for (y in 0 until 512) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}