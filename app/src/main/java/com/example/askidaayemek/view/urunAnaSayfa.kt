package com.example.askidaayemek.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.R
import com.example.askidaayemek.adapter.urunAnaSayfaAdapter
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentUrunAnaSayfaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class urunAnaSayfa : Fragment(R.layout.fragment_urun_ana_sayfa) {
    private var _binding: FragmentUrunAnaSayfaBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var tamListe = ArrayList<urun>()
    private var filtreliListe = ArrayList<urun>()
    private lateinit var adapter: urunAnaSayfaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunAnaSayfaBinding.bind(view)
        db = Firebase.firestore
        auth = Firebase.auth

        kullaniciBilgisiniGetir()
        binding.siraliRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = urunAnaSayfaAdapter(filtreliListe) { secilenUrun ->
            val bundle = Bundle().apply {
                putString("urunId", secilenUrun.urunId)
            }
            findNavController().navigate(R.id.action_urunAnaSayfa_to_urunDetayfragment, bundle)
        }

        binding.siraliRecyclerView.adapter = adapter
        verileriGetir()

        binding.urunAraEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { aramaYap(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.tumuButton.setOnClickListener { kategoriFiltrele("Tümü") }
        binding.yemeklerButton.setOnClickListener { kategoriFiltrele("Yemekler") }
        binding.kiyafetlerButton.setOnClickListener { kategoriFiltrele("Kıyafetler") }
        binding.beyazEsyaButton.setOnClickListener { kategoriFiltrele("beyaz Eşyaları") }
        binding.bebekEsyasiButton.setOnClickListener { kategoriFiltrele("Bebek Eşyaları") }

        binding.urunDetayaGitFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_urunAnaSayfa_to_urunPaylasanFragment)
        }
    }

    private fun kullaniciBilgisiniGetir() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("Kullanicilar").document(uid).get()
                .addOnSuccessListener { document ->
                    if (_binding != null && document != null && document.exists()) {
                        val ad = document.getString("ad") ?: "Kullanıcı"
                        binding.kullaniciAdiTextView.text = " $ad"
                    }
                }
                .addOnFailureListener {
                    if (_binding != null) binding.kullaniciAdiTextView.text = " Misafir"
                }
        }
    }

    private fun aramaYap(text: String) {
        val query = text.lowercase().trim()
        filtreliListe.clear()
        if (query.isEmpty()) {
            filtreliListe.addAll(tamListe)
        } else {
            for (item in tamListe) {
                if (item.urunAdi?.lowercase()?.contains(query) == true) {
                    filtreliListe.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun kategoriFiltrele(kategori: String) {
        filtreliListe.clear()
        if (kategori == "Tümü") {
            filtreliListe.addAll(tamListe)
        } else {
            for (item in tamListe) {
                if (item.urunKategori == kategori) {
                    filtreliListe.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun verileriGetir() {
        db.collection("Urunler").orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null || _binding == null) return@addSnapshotListener
                if (value != null) {
                    tamListe.clear()
                    for (document in value.documents) {
                        val urunObj = document.toObject(urun::class.java)
                        if (urunObj != null) {
                            urunObj.urunId = document.id
                            tamListe.add(urunObj)
                        }
                    }
                    aramaYap(binding.urunAraEditText.text.toString())
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}