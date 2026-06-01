package com.example.askidaayemek.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class yoneticiQrKodFragment : Fragment(R.layout.fragment_yonetici_qr_kod) {
    private var _binding: FragmentYoneticiQrKodBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var snapshotListener: ListenerRegistration? = null
    private var aktifTalepId: String? = null
    private var asilUrunId: String? = null
    private var talepEdilenMiktar: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYoneticiQrKodBinding.bind(view)

        binding.yoneticiQrToolbar.post {
            val params = binding.yoneticiQrToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.yoneticiQrToolbar.layoutParams = params
        }

        binding.buttonTeslimEt.isEnabled = false
        binding.yoneticiQrToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val adminUuid = Firebase.auth.currentUser?.uid

        if (adminUuid != null) {
            val qrIcerik = "ADMIN_$adminUuid"
            val bitmap = yoneticiQrOlustur(qrIcerik)
            binding.qrKodImageView.setImageBitmap(bitmap)
            anlikMusteriTakibi(adminUuid)
        } else {
            val testAdminId = "TEST_EMULATOR_ADMIN_ID"
            val bitmap = yoneticiQrOlustur("ADMIN_$testAdminId")
            binding.qrKodImageView.setImageBitmap(bitmap)
            anlikMusteriTakibi(testAdminId)
        }
        binding.buttonTeslimEt.setOnClickListener {
            if (!aktifTalepId.isNullOrEmpty()) {
                binding.buttonTeslimEt.isEnabled = false
                val batch = db.batch()
                val talepRef = db.collection("Talepler").document(aktifTalepId!!)
                batch.delete(talepRef)
                if (!asilUrunId.isNullOrEmpty()) {
                    val urunRef = db.collection("Urunler").document(asilUrunId!!)

                    urunRef.get().addOnSuccessListener { urunDoc ->
                        if (urunDoc.exists()) {
                            val mevcutMiktarString = urunDoc.get("miktar")?.toString() ?: "0"
                            val mevcutMiktar = mevcutMiktarString.toIntOrNull() ?: 0
                            val yeniMiktar = mevcutMiktar - talepEdilenMiktar

                            if (yeniMiktar <= 0) {
                                batch.delete(urunRef)
                            } else {
                                batch.update(urunRef, "miktar", yeniMiktar.toString())
                            }
                        }
                        batch.commit()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Teslimat yapıldı ve stok güncellendi!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            }
                            .addOnFailureListener { e ->
                                binding.buttonTeslimEt.isEnabled = true
                                Toast.makeText(
                                    context,
                                    "Hata: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }.addOnFailureListener {
                        batch.commit()
                        findNavController().popBackStack()
                    }
                } else {
                    batch.commit().addOnSuccessListener {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun anlikMusteriTakibi(adminId: String) {
        snapshotListener?.remove()

        snapshotListener = db.collection("Talepler")
            .whereEqualTo("onaylayacakAdminUid", adminId)
            .whereEqualTo("durum", "Okutuldu")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("FirebaseError", "Dinleyici hatası: ${error.localizedMessage}")
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty && value.documents.isNotEmpty()) {
                    val document = value.documents[0]
                    aktifTalepId = document.id
                    asilUrunId = document.getString("urunId")
                    val miktarGelen = document.get("miktar")?.toString() ?: "1"
                    talepEdilenMiktar = miktarGelen.toIntOrNull() ?: 1

                    val urunAdi = document.getString("urunAdi") ?: "Belirtilmedi"
                    val musteriAdSoyad =
                        document.getString("musteriAdSoyad") ?: "Bilinmeyen Müşteri"

                    binding.musterininAdSoyad.text = "Müşteri: $musteriAdSoyad"
                    binding.talepBilgileriTextView.text =
                        "Ürün Bilgileri: $urunAdi ($miktarGelen Adet)"
                    binding.buttonTeslimEt.isEnabled = true
                } else {
                    binding.musterininAdSoyad.text = "Bekleniyor"
                    binding.talepBilgileriTextView.text = "Müşterinin QR kodu okutması bekleniyor"
                    binding.buttonTeslimEt.isEnabled = false
                    aktifTalepId = null
                    asilUrunId = null
                }
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
        snapshotListener?.remove()
        snapshotListener = null
        super.onDestroyView()
        _binding = null
    }
}