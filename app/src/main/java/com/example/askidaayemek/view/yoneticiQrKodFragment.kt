package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class yoneticiQrKodFragment : Fragment(R.layout.fragment_yonetici_qr_kod) {

    private var _binding: FragmentYoneticiQrKodBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var aktifTalepId: String? = null
    private var asilUrunId: String? = null
    private var talepEdilenMiktar: Int = 1
    private val TAG = "YoneticiQrKodFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentYoneticiQrKodBinding.bind(view)

        Log.d(TAG, "Fragment onViewCreated çalıştı")


        binding.yoneticiQrToolbar.post {
            if (_binding != null) {
                val params = binding.yoneticiQrToolbar.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 100
                binding.yoneticiQrToolbar.layoutParams = params
            }
        }

        binding.yoneticiQrToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val gelenVeri = arguments?.getString("qr_verisi")
        Log.d(TAG, "Gelen argument (qr_verisi): '$gelenVeri'")

        if (!gelenVeri.isNullOrEmpty()) {
            Log.d(TAG, "Veri bulundu, işleniyor")
            veriyiIsle(gelenVeri)
        } else {
            Log.e(TAG, "QR verisi BOŞ veya NULL")
            Toast.makeText(
                context,
                "QR verisi alınamadı! QR scanner'dan veri gelmiyor.",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.buttonTeslimEt.setOnClickListener {
            if (aktifTalepId.isNullOrEmpty()) {
                Toast.makeText(context, "Aktif talep bulunamadı!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.buttonTeslimEt.isEnabled = false
            (activity as? MainActivity)?.gosterLoading(true)

            val batch = db.batch()
            val talepRef = db.collection("Talepler").document(aktifTalepId!!)
            batch.update(talepRef, "durum", "Teslim Edildi")

            if (!asilUrunId.isNullOrEmpty()) {
                val urunRef = db.collection("Urunler").document(asilUrunId!!)
                urunRef.get().addOnSuccessListener { urunDoc ->
                    if (urunDoc.exists()) {
                        val mevcutMiktar = urunDoc.get("miktar")?.toString()?.toIntOrNull() ?: 0
                        val yeniMiktar = mevcutMiktar - talepEdilenMiktar
                        if (yeniMiktar <= 0) batch.delete(urunRef)
                        else batch.update(urunRef, "miktar", yeniMiktar.toString())
                    }
                    commitBatch(batch)
                }.addOnFailureListener {
                    commitBatch(batch)
                }
            } else {
                commitBatch(batch)
            }
        }
    }

    private fun commitBatch(batch: com.google.firebase.firestore.WriteBatch) {
        batch.commit().addOnSuccessListener {
            (activity as? MainActivity)?.gosterLoading(false)
            Toast.makeText(context, "Teslimat yapıldı!", Toast.LENGTH_SHORT).show()
            navigateToAnaSayfa()
        }.addOnFailureListener { e ->
            (activity as? MainActivity)?.gosterLoading(false)
            binding.buttonTeslimEt.isEnabled = true
            Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun veriyiIsle(data: String) {
        Log.d(TAG, "=== QR VERİSİ İŞLENİYOR ===")
        Log.d(TAG, "Raw data: '$data'")

        val temizVeri = data.replace("MUSTERIQR_", "").trim()
        Log.d(TAG, "Temiz veri: '$temizVeri'")

        val parcalar = temizVeri.split("|")
        Log.d(TAG, "Parça sayısı: ${parcalar.size}")
        Log.d(TAG, "Parçalar: ${parcalar.joinToString(" | ")}")

        if (parcalar.size >= 6) {
            aktifTalepId = parcalar[0]
            asilUrunId = parcalar[1]
            val urunAdi = parcalar[2]
            val miktarGelen = parcalar[3]
            talepEdilenMiktar = miktarGelen.toIntOrNull() ?: 1
            val musteriAdSoyad = parcalar[4]
            val musteriEmail = parcalar[5]

            val zamanMilisString = parcalar.getOrNull(6)
            val formatliSaat = if (!zamanMilisString.isNullOrEmpty()) {
                try {
                    SimpleDateFormat("HH:mm - dd.MM.yyyy", Locale.getDefault()).format(
                        Date(
                            zamanMilisString.toLong()
                        )
                    )
                } catch (e: Exception) {
                    "Belirtilmedi"
                }
            } else "Belirtilmedi"
            binding.musterininAdSoyad.text = "AD / SOYAD: $musteriAdSoyad"
            binding.musterininEmail.text = "e-mail: $musteriEmail"
            binding.talepBilgileriTextView.text = "Ürün İsmi Ve Adedi: $urunAdi ($miktarGelen Adet)"
            binding.saatTextViewView.text = "Alacağı Saat / Aldığı Saat: $formatliSaat"
            if (!asilUrunId.isNullOrEmpty()) {
                db.collection("Urunler").document(asilUrunId!!).get()
                    .addOnSuccessListener { document ->
                        val aciklama = document.getString("ekNot") ?: document.getString("aciklama")
                        ?: "Açıklama yok"
                        binding.acKlamaTextView.text = "Açıklaması: $aciklama"
                    }
            } else {
                binding.acKlamaTextView.text = "Açıklaması: Belirtilmemiş"
            }

            binding.buttonTeslimEt.isEnabled = true
            Log.d(TAG, "QR verisi başarıyla yüklendi!")

        } else {
            Log.e(TAG, "Yetersiz parça: ${parcalar.size}")
            Toast.makeText(context, "Hatalı QR kodu! (${parcalar.size} parça)", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        }
    }

    private fun navigateToAnaSayfa() {
        if (isAdded) findNavController().navigate(R.id.action_yoneticiQrKodFragment_to_urunAnaSayfa)
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}