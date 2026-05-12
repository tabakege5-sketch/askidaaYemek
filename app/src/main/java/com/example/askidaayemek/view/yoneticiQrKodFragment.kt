package com.example.askidaayemek.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class yoneticiQrKodFragment : Fragment(R.layout.fragment_yonetici_qr_kod) {
    private var _binding: FragmentYoneticiQrKodBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYoneticiQrKodBinding.bind(view)

        val adminUuid = Firebase.auth.currentUser?.uid

        if (adminUuid != null) {
            val qrIcerik = "ADMIN_$adminUuid"
            val bitmap = yoneticiQrOlustur(qrIcerik)
            binding.qrKodImageView.setImageBitmap(bitmap)
        }

        binding.geriGitImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonTeslimEt.setOnClickListener {
            Toast.makeText(context, "Teslimat Onay Ekranına Yönlendiriliyorsunuz", Toast.LENGTH_SHORT).show()
        }
    }

    private fun yoneticiQrOlustur(veriler: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(veriler, BarcodeFormat.QR_CODE, 600, 600)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}