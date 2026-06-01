package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class tarihVeZamanFragment : Fragment() {

    private var _binding: FragmentTarihVeZamanBinding? = null
    private val binding get() = _binding!!
    private var secilenTarih = ""
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var mevcutUrun: urun? = null

    // 1. onCreateView içerisinde inflate etmeyi unutma!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTarihVeZamanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val urunId = arguments?.getString("urunId")
        Log.d("DEBUG_TEST", "Gelen UrunID: $urunId")

        if (!urunId.isNullOrEmpty()) {
            db.collection("Urunler").document(urunId).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    mevcutUrun = doc.toObject(urun::class.java)?.apply { this.urunId = doc.id }
                }
            }
        }

        binding.tarihZamanToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.takvimView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            secilenTarih = "$dayOfMonth.${month + 1}.$year"
        }

        // 2. Butonun çalıştığından emin olmak için direkt listener
        binding.secimiKaydetButton.setOnClickListener {
            Log.d("DEBUG_TEST", "Kaydet butonuna basıldı!")
            Toast.makeText(context, "Kaydet butonuna basıldı!", Toast.LENGTH_SHORT).show()

            val currentMusteriUid = auth.currentUser?.uid
            if (currentMusteriUid == null) {
                Toast.makeText(context, "Giriş yapın", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mevcutUrun == null) {
                Toast.makeText(context, "Ürün yükleniyor, bekleyin...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ekBilgi = binding.bilgiEditText.text.toString()
            if (ekBilgi.isEmpty()) {
                Toast.makeText(context, "Açıklama giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val yeniTalepMap = hashMapOf(
                "urunId" to (mevcutUrun!!.urunId ?: ""),
                "urunAdi" to (mevcutUrun!!.urunAdi ?: ""),
                "yukleyenUid" to (mevcutUrun!!.yukleyenUid ?: ""),
                "musteriUid" to currentMusteriUid,
                "tarih" to Timestamp.now(),
                "durum" to "Beklemede"
            )

            db.collection("Talepler").add(yeniTalepMap).addOnSuccessListener {
                Toast.makeText(context, "Talep oluşturuldu", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_tarihVeZamanFragment_to_taleplerFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}