package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
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
            val params = binding.musteriQrKodOkutmaToolBar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.musteriQrKodOkutmaToolBar.layoutParams = params
        }

        binding.musteriQrKodOkutmaToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val secilenUrunAdi = arguments?.getString("urunAdi") ?: "Test Yemeği"
        secilenUrunId = arguments?.getString("urunId")

        binding.urununIsmi.text = "Ürünün İsmi: $secilenUrunAdi"
        val currentUser = Firebase.auth.currentUser

        binding.btnTeslimatiOnayla.isEnabled = true
        binding.btnTeslimatiOnayla.text = "Barkod Okutmak İçin Kamerayı Aç"

        if (currentUser != null) {
            db.collection("Kullanicilar").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (_binding != null && document != null && document.exists()) {
                        kullaniciAdSoyad = document.getString("adSoyad") ?: "Bilinmeyen Kullanıcı"
                        binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
                    }
                    hizliQrTarayiciyiAc()
                }
                .addOnFailureListener {
                    binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
                    hizliQrTarayiciyiAc()
                }
        } else {
            kullaniciAdSoyad = "Gerçek Telefon Test Kullanıcısı"
            binding.isimSoyIsimTextView.text = "Ad - Soyad: $kullaniciAdSoyad"
            hizliQrTarayiciyiAc()
        }
        binding.btnTeslimatiOnayla.setOnClickListener {
            if (tarananAdminUuid.isNullOrEmpty()) {
                hizliQrTarayiciyiAc()
                return@setOnClickListener
            }

            if (secilenUrunId.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Hata: Seçilen ürünün ID bilgisi alınamadı",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            binding.btnTeslimatiOnayla.isEnabled = false

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
                        "Teslimat Onaylandı! Ana Sayfaya Yönlendiriliyorsunuz.",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_musteriQrKodFragment_to_urunAnaSayfa)
                }
                .addOnFailureListener { e ->
                    binding.btnTeslimatiOnayla.isEnabled = true
                    Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun hizliQrTarayiciyiAc() {
        if (_binding == null) return

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)
        binding.btnTeslimatiOnayla.text = "Kamera Açık, Barkod Bekleniyor..."

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
                        Toast.makeText(context, "Geçersiz Yönetici Barkodu!", Toast.LENGTH_SHORT).show()
                        tarananAdminUuid = null
                        binding.btnTeslimatiOnayla.isEnabled = true
                        binding.btnTeslimatiOnayla.text = "Hatalı Barkod! Tekrar Aramak İçin Tıklayın"
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QR_TARAMA", "Tarama iptal veya hata: ${e.localizedMessage}")
                if (_binding != null) {
                    tarananAdminUuid = null
                    binding.btnTeslimatiOnayla.isEnabled = true
                    binding.btnTeslimatiOnayla.text = "Tarama İptal Edildi! Yeniden Taramak İçin Tıklayın"
                    Toast.makeText(context, "Tarama İptal Edildi", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}