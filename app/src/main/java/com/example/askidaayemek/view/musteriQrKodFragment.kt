package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class musteriQrKodFragment : Fragment(R.layout.fragment_musteri_qr_kod) {

    private var _binding: FragmentMusteriQrKodBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var kullaniciAdSoyad: String = "Bilinmeyen Kullanıcı"
    private var tarananAdminUuid: String? = null
    private var secilenUrunId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusteriQrKodBinding.bind(view)

        binding.musteriQrKodOkutmaToolBar.post {
            val params =
                binding.musteriQrKodOkutmaToolBar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.musteriQrKodOkutmaToolBar.layoutParams = params
        }

        binding.musteriQrKodOkutmaToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        secilenUrunId = arguments?.getString("urunId")
        val secilenUrunAdi = arguments?.getString("urunAdi") ?: "Test Yemeği"
        binding.urununIsmi.text = "Ürünün İsmi: $secilenUrunAdi"

        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            (activity as? MainActivity)?.gosterLoading(true)
            db.collection("Kullanicilar").document(currentUser.uid).get()
                .addOnSuccessListener { doc ->
                    (activity as? MainActivity)?.gosterLoading(false)
                    if (_binding != null && doc.exists()) {
                        kullaniciAdSoyad = doc.getString("adSoyad") ?: "Bilinmeyen Kullanıcı"
                        binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
                    }
                    hizliQrTarayiciyiAc()
                }.addOnFailureListener {
                    (activity as? MainActivity)?.gosterLoading(false)
                    hizliQrTarayiciyiAc()
                }
        } else {
            hizliQrTarayiciyiAc()
        }

        binding.buttonnTeslimatiOnayla.setOnClickListener {
            if (tarananAdminUuid.isNullOrEmpty()) {
                hizliQrTarayiciyiAc()
                return@setOnClickListener
            }

            binding.buttonnTeslimatiOnayla.isEnabled = false
            (activity as? MainActivity)?.gosterLoading(true)

            db.collection("Talepler").document(secilenUrunId!!)
                .update(
                    mapOf(
                        "onaylayacakAdminUid" to tarananAdminUuid,
                        "durum" to "Okutuldu",
                        "musteriAdSoyad" to kullaniciAdSoyad
                    )
                )
                .addOnSuccessListener {
                    (activity as? MainActivity)?.gosterLoading(false)
                    if (_binding != null) {
                        Toast.makeText(context, "İşlem Tamamlandı!", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack(R.id.urunAnaSayfa, false)
                    }
                }
                .addOnFailureListener { e ->
                    (activity as? MainActivity)?.gosterLoading(false)
                    binding.buttonnTeslimatiOnayla.isEnabled = true
                    Toast.makeText(context, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun hizliQrTarayiciyiAc() {
        if (_binding == null) return
        val options =
            GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val data = barcode.rawValue
                if (!data.isNullOrEmpty() && data.startsWith("ADMIN_")) {
                    tarananAdminUuid = data.substring(6)
                    binding.buttonnTeslimatiOnayla.text = "Teslimatı Onayla ve Tamamla"
                    binding.buttonnTeslimatiOnayla.isEnabled = true
                } else {
                    Toast.makeText(context, "Geçersiz Barkod", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}