package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class tarihVeZamanFragment : Fragment(R.layout.fragment_tarih_ve_zaman) {
    private var _binding: FragmentTarihVeZamanBinding? = null
    private val binding get() = _binding!!
    private var secilenTarih = ""
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTarihVeZamanBinding.bind(view)
        val secilenUrun = arguments?.getParcelable<urun>("secilenUrun")

        binding.tarihZamanToolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.takvimView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            secilenTarih = "$dayOfMonth.${month + 1}.$year"
        }

        if (secilenTarih.isEmpty()) {
            val bugün = Calendar.getInstance()
            secilenTarih = "${bugün.get(Calendar.DAY_OF_MONTH)}.${bugün.get(Calendar.MONTH) + 1}.${
                bugün.get(Calendar.YEAR)
            }"
        }

        binding.secimiKaydetButton.setOnClickListener {
            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                Toast.makeText(context, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (secilenUrun == null) {
                Toast.makeText(context, "Ürün bilgisi eksik", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val saat = binding.tamSaatTimePicker.hour
            val dakika = binding.tamSaatTimePicker.minute
            val formatliZaman = String.format("%02d:%02d", saat, dakika)
            val ekBilgi = binding.bilgiEditText.text.toString()

            if (ekBilgi.isNotEmpty()) {
                binding.secimiKaydetButton.isEnabled = false
                val randevuDetayi = "Randevu: $secilenTarih saat $formatliZaman - Not: $ekBilgi"
                val yeniTalepMap = hashMapOf(
                    "urunAdi" to secilenUrun.urunAdi,
                    "miktar" to secilenUrun.miktar,
                    "konum" to secilenUrun.konum,
                    "gorselUrl" to secilenUrun.gorselUrl,
                    "tarih" to Timestamp.now(),
                    "durum" to "Beklemede",
                    "talepTipi" to "Tarihli Rezervasyon",
                    "randevuZamani" to randevuDetayi,
                    "yukleyenUid" to currentUserUid,
                    "ekNot" to secilenUrun.ekNot,
                    "urunKategori" to secilenUrun.urunKategori
                )
                db.collection("Talepler")
                    .add(yeniTalepMap)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Talebiniz başarıyla oluşturuldu",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_urunAnaSayfa_to_taleplerFragment)
                    }
                    .addOnFailureListener { e ->
                        binding.secimiKaydetButton.isEnabled = true
                        Log.e("FIREBASE_DEBUG", "Randevu kaydedilemedi: ${e.localizedMessage}")
                        Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(context, "Lütfen açıklama giriniz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}