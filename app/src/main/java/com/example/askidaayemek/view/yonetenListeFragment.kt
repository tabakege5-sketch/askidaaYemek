package com.example.askidaayemek.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        verileriGetir()
    }

    private fun verileriGetir() {
        (activity as? MainActivity)?.gosterLoading(true)
        db.collection("Talepler")
            .orderBy("tarih", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                (activity as? MainActivity)?.gosterLoading(false)
                val liste = arrayListOf<HashMap<String, String>>()

                for (doc in documents) {
                    val map = hashMapOf(
                        "adSoyad" to (doc.getString("musteriAdSoyad") ?: "Bilinmeyen Kullanıcı"),
                        "urunAdi" to (doc.getString("urunAdi") ?: "Ürün Adı Yok"),
                        "miktar" to (doc.getString("miktar") ?: "0"),
                        "durum" to (doc.getString("durum") ?: "Beklemede")
                    )
                    liste.add(map)
                }

                if (isAdded && _binding != null) {
                    binding.yonetenListeRecyclerView.adapter = yonetenListeAdapter(liste)
                }
            }
            .addOnFailureListener {
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(context, "Veriler getirilirken bir hata oluştu", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}