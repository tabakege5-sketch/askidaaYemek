package com.example.askidaayemek.view

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class musteriQrKodFragment : Fragment(R.layout.fragment_musteri_qr_kod) {

    private var _binding: FragmentMusteriQrKodBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var kullaniciAdSoyad: String = "Bilinmeyen Kullanıcı"
    private var kullaniciEmail: String = "E-posta Bulunamadı"
    private var secilenUrunId: String? = null
    private var asilIlanId: String? = null
    private var musteriTalepDinleyici: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusteriQrKodBinding.bind(view)

        binding.musteriQrKodOkutmaToolBar.post {
            if (_binding != null) {
                val params =
                    binding.musteriQrKodOkutmaToolBar.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 100
                binding.musteriQrKodOkutmaToolBar.layoutParams = params
            }
        }

        binding.musteriQrKodOkutmaToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        secilenUrunId = arguments?.getString("urunId")
        asilIlanId = arguments?.getString("asilIlanId")
        val secilenUrunAdi = arguments?.getString("urunAdi") ?: "Test Yemeği"
        val talepEdilenMiktar =
            arguments?.getString("miktar") ?: arguments?.getInt("miktar")?.toString() ?: "1"
        binding.urununIsmi.text = "Ürünün İsmi: $secilenUrunAdi"

        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            (activity as? MainActivity)?.gosterLoading(true)
            db.collection("Kullanicilar").document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    (activity as? MainActivity)?.gosterLoading(false)
                    if (_binding != null && doc.exists()) {
                        kullaniciAdSoyad = doc.getString("adSoyad") ?: "Bilinmeyen Kullanıcı"
                        kullaniciEmail =
                            doc.getString("email") ?: doc.getString("eMail") ?: "E-posta Bulunamadı"
                        binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
                        binding.emailTextView.text = "E-Mail: $kullaniciEmail"
                    }
                    qrKoduUretVeEkranaBas(talepEdilenMiktar, secilenUrunAdi)
                }.addOnFailureListener {
                    (activity as? MainActivity)?.gosterLoading(false)
                    qrKoduUretVeEkranaBas(talepEdilenMiktar, secilenUrunAdi)
                }
        } else {
            qrKoduUretVeEkranaBas(talepEdilenMiktar, secilenUrunAdi)
        }
    }

    private fun qrKoduUretVeEkranaBas(talepEdilenMiktar: String, secilenUrunAdi: String) {
        if (_binding == null) return
        if (secilenUrunId == null) {
            Toast.makeText(context, "Gerekli ürün kimlikleri bulunamadı!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val ilanId = asilIlanId ?: "BilinmeyenIlan"
        val suAnkiZamanMs = System.currentTimeMillis().toString()
        val qrIcerik =
            "MUSTERIQR_${secilenUrunId!!}|$ilanId|$secilenUrunAdi|$talepEdilenMiktar|$kullaniciAdSoyad|$kullaniciEmail|$suAnkiZamanMs"
        val bitmap = musteriQrOlustur(qrIcerik)
        binding.qrKodOkuyucusu.setImageBitmap(bitmap)
        takipEtYoneticiOnayini()
    }
    private fun musteriQrOlustur(veriler: String): Bitmap {
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

    private fun takipEtYoneticiOnayini() {
        if (secilenUrunId.isNullOrEmpty()) return

        musteriTalepDinleyici?.remove()
        musteriTalepDinleyici = db.collection("Talepler").document(secilenUrunId!!)
            .addSnapshotListener { snapshot, error ->
                if (error != null || !isAdded) return@addSnapshotListener
                if (snapshot != null && !snapshot.exists()) {
                    musteriTalepDinleyici?.remove()
                    musteriTalepDinleyici = null
                    findNavController().popBackStack(R.id.urunAnaSayfa, false)
                }
            }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        musteriTalepDinleyici?.remove()
        musteriTalepDinleyici = null
        super.onDestroyView()
        _binding = null
    }
}