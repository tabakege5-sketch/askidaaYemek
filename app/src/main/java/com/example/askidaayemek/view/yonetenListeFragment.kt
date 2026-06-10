package com.example.askidaayemek.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.R
import com.example.askidaayemek.adapter.yonetenListeAdapter
import com.example.askidaayemek.databinding.FragmentYonetenListeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class yonetenListeFragment : Fragment(R.layout.fragment_yoneten_liste) {

    private var _binding: FragmentYonetenListeBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val teslimatListesi = arrayListOf<HashMap<String, String>>()
    private lateinit var adapter: yonetenListeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYonetenListeBinding.bind(view)

        binding.yoneticiListeToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")

        if (rol != "YONETICI") {
            Toast.makeText(context, "Bu sayfaya sadece yöneticiler girebilir", Toast.LENGTH_SHORT)
                .show()
            findNavController().popBackStack()
            return
        }

        binding.yonetenListeRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter = yonetenListeAdapter(teslimatListesi) { secilenTalep ->
            talepDetayiniGoster(secilenTalep)
        }

        binding.yonetenListeRecyclerView.adapter = adapter
        verileriGetir()
    }

    private fun verileriGetir() {
        (activity as? MainActivity)?.gosterLoading(true)
        val bitmisDurumlar =
            listOf("Onaylandı", "Teslim Edildi", "Silindi", "İptal Edildi", "Zaman Aşımı")

        db.collection("Talepler")
            .whereIn("durum", bitmisDurumlar)
            .orderBy("tarih", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                (activity as? MainActivity)?.gosterLoading(false)

                if (_binding == null) return@addOnSuccessListener
                teslimatListesi.clear()
                for (doc in documents) {
                    val musteriAdi = doc.getString("musteriAdSoyad")
                        ?: doc.getString("adSoyad")
                        ?: "Bilinmeyen Kullanıcı"
                    val timestamp = doc.getTimestamp("tarih")
                    val formatliTarih = if (timestamp != null) {
                        val sfd = java.text.SimpleDateFormat(
                            "dd.MM.yyyy - HH:mm",
                            java.util.Locale.getDefault()
                        )
                        sfd.format(timestamp.toDate())
                    } else {
                        "Tarih Bilgisi Yok"
                    }
                    val map = hashMapOf(
                        "adSoyad" to musteriAdi,
                        "urunAdi" to (doc.getString("urunAdi") ?: "Ürün Adı Yok"),
                        "miktar" to (doc.get("miktar")?.toString() ?: "1"),
                        "durum" to (doc.getString("durum") ?: "Belirtilmedi"),
                        "gorselUrl" to (doc.getString("gorselUrl") ?: ""),
                        "tarih" to formatliTarih
                    )
                    teslimatListesi.add(map)
                }

                adapter.notifyDataSetChanged()
                if (teslimatListesi.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Gösterilebilecek geçmiş işlem bulunamadı",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                (activity as? MainActivity)?.gosterLoading(false)
                android.util.Log.e(
                    "FIRESTORE_HATA",
                    "Yönetici listesi çekilemedi: ${e.localizedMessage}"
                )

                if (e.localizedMessage?.contains("FAILED_PRECONDITION") == true) {
                    Toast.makeText(
                        context,
                        "Firestore İndeksi Eksik Logcat'teki linke tıklayıp indeksi oluşturun",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Veriler getirilirken bir hata oluştu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun talepDetayiniGoster(talep: HashMap<String, String>) {
        AlertDialog.Builder(requireContext())
            .setTitle("İşlem Geçmişi Detayı:")
            .setMessage(
                "Müşteri: ${talep["adSoyad"]}\n" +
                        "Ürün Adı: ${talep["urunAdi"]}\n" +
                        "Miktar: ${talep["miktar"]} Adet\n" +
                        "Durum: ${talep["durum"]}\n" +
                        "Alındığı Tarih/" +
                        "Alındığı Saat: " + "" +
                        "${talep["tarih"]}"
            )
            .setPositiveButton("Tamam", null)
            .show()
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}