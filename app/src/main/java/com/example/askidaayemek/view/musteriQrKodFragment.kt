package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
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

        binding.musteriQrKodOkutmaToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val secilenUrunAdi = arguments?.getString("urunAdi") ?: "Test Yemeği"
        secilenUrunId = arguments?.getString("urunId")

        binding.urununIsmi.text = "Ürünün İsmi: $secilenUrunAdi"
        val currentUser = Firebase.auth.currentUser
        binding.btnTeslimatiOnayla.isEnabled = false
        binding.btnTeslimatiOnayla.text = "Teslimatı Onayla (Barkod Bekleniyor)"

        if (currentUser != null) {
            db.collection("Kullanicilar").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        kullaniciAdSoyad = document.getString("adSoyad") ?: "Bilinmeyen Kullanıcı"
                        binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
                    }
                    hizliQrTarayiciyiAc()
                }
                .addOnFailureListener {
                    hizliQrTarayiciyiAc()
                }
        } else {
            kullaniciAdSoyad = "Gerçek Telefon Test Kullanıcısı"
            binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
            hizliQrTarayiciyiAc()
        }
        binding.btnTeslimatiOnayla.setOnClickListener {
            if (secilenUrunId.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Hata: Seçilen ürünün ID bilgisi alınamadı",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (tarananAdminUuid.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Hata: Geçerli barkod verisi bulunamadı",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            db.collection("Talepler").document(secilenUrunId!!)
                .update(
                    mapOf(
                        "onaylayacakAdminUid" to tarananAdminUuid,
                        "durum" to "Okutuldu",
                        "musteriAdSoyad" to kullaniciAdSoyad
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Teslimat Onaylandı! Yönetici ekranı tetiklendi.",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun hizliQrTarayiciyiAc() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val okunanVeri = barcode.rawValue

                if (!okunanVeri.isNullOrEmpty()) {
                    if (okunanVeri.startsWith("ADMIN_")) {
                        tarananAdminUuid = okunanVeri.substring(6)
                        binding.btnTeslimatiOnayla.isEnabled = true
                        binding.btnTeslimatiOnayla.text = "Teslimatı Onayla ve Tamamla"

                        Toast.makeText(
                            context,
                            "Barkod Okundu! Şimdi aşağıdaki butona basarak onaylayın.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Geçersiz Yönetici Barkodu!", Toast.LENGTH_SHORT)
                            .show()
                        binding.btnTeslimatiOnayla.isEnabled = false
                        binding.btnTeslimatiOnayla.text = "Barkod okutulmadı Tekrar Deneyin"
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QR_TARAMA", "Tarama iptal veya hata: ${e.localizedMessage}")
                Toast.makeText(context, "Tarama İptal Edildi veya Başarısız", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}