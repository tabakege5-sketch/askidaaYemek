package com.example.askidaayemek.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
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
import java.util.ArrayList

class urunAnaSayfa : Fragment(R.layout.fragment_urun_ana_sayfa) {
    private var _binding: FragmentUrunAnaSayfaBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var tamListe = ArrayList<urun>()
    private var filtreliListe = ArrayList<urun>()
    private lateinit var adapter: urunAnaSayfaAdapter
    private var kullaniciRolu: String = "MUSTERI"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunAnaSayfaBinding.bind(view)

        binding.anaSayfaToolbAR.post {
            if (_binding != null) {
                val params = binding.anaSayfaToolbAR.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 100
                binding.anaSayfaToolbAR.layoutParams = params
            }
        }
        db = Firebase.firestore
        auth = Firebase.auth

        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        kullaniciRolu = sharedPref.getString("kullanici_rolu", "MUSTERI") ?: "MUSTERI"

        arayuzuRoleGoreDuzenle()
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
            override fun afterTextChanged(s: Editable?) {
                aramaYap(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.tumuButton.setOnClickListener { kategoriFiltrele("Tümü") }
        binding.yemeklerButton.setOnClickListener { kategoriFiltrele("Yemekler") }
        binding.kiyafetlerButton.setOnClickListener { kategoriFiltrele("Kıyafetler") }
        binding.beyazEsyaButton.setOnClickListener { kategoriFiltrele("Beyaz Eşya") }
        binding.bebekEsyasiButton.setOnClickListener { kategoriFiltrele("Bebek Eşyaları") }

        binding.urunDetayaGitFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_urunAnaSayfa_to_urunPaylasanFragment)
        }


        binding.qrGitButton.setOnClickListener {
            findNavController().navigate(R.id.action_urunAnaSayfa_to_yoneticiQrKodFragment)
        }
    }

    private fun listeyiGuncelleVeMesajKontrolEt() {
        adapter.notifyDataSetChanged()
        if (filtreliListe.isEmpty()) {
            binding.sonucBulunamadiTextView.visibility = View.VISIBLE
        } else {
            binding.sonucBulunamadiTextView.visibility = View.GONE
        }
    }

    private fun arayuzuRoleGoreDuzenle() {
        if (_binding == null) return
        binding.anaSayfaToolbAR.menu.clear()
        if (kullaniciRolu == "YONETICI") {
            binding.urunDetayaGitFloatingActionButton.visibility = View.VISIBLE
            binding.qrGitButton.visibility = View.VISIBLE
            binding.anaSayfaToolbAR.inflateMenu(R.menu.yonetici_menuler)
        } else {
            binding.urunDetayaGitFloatingActionButton.visibility = View.GONE
            binding.qrGitButton.visibility = View.GONE
            binding.anaSayfaToolbAR.inflateMenu(R.menu.musteri_menu)
        }

        binding.anaSayfaToolbAR.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.taleplerFragment -> {
                    findNavController().navigate(R.id.action_urunAnaSayfa_to_taleplerFragment)
                    true
                }

                R.id.urunEkleFragment -> {
                    findNavController().navigate(R.id.action_urunAnaSayfa_to_urunPaylasanFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun kullaniciBilgisiniGetir() {
        val uid = auth.currentUser?.uid ?: return
        (activity as? MainActivity)?.gosterLoading(true)
        db.collection("Yoneticiler").document(uid).get()
            .addOnSuccessListener { document ->
                if (_binding == null) return@addOnSuccessListener
                if (document != null && document.exists()) {
                    kullaniciRolu = "YONETICI"
                    val ad = document.getString("ad") ?: document.getString("e-posta")
                        ?.substringBefore("@") ?: "Yönetici"
                    binding.kullaniciAdiTextView.text = " $ad ($kullaniciRolu)"
                    requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
                        .edit().putString("kullanici_rolu", "YONETICI").apply()
                    arayuzuRoleGoreDuzenle()
                    (activity as? MainActivity)?.gosterLoading(false)
                } else {
                    db.collection("Kullanicilar").document(uid).get()
                        .addOnSuccessListener { userDoc ->
                            (activity as? MainActivity)?.gosterLoading(false)
                            if (_binding == null) return@addOnSuccessListener
                            if (userDoc.exists()) {
                                kullaniciRolu = "MUSTERI"
                                val ad = userDoc.getString("ad") ?: userDoc.getString("e-posta")
                                    ?.substringBefore("@") ?: "Kullanıcı"
                                binding.kullaniciAdiTextView.text = " $ad ($kullaniciRolu)"
                                requireActivity().getSharedPreferences(
                                    "AskidaYemekPref",
                                    Context.MODE_PRIVATE
                                )
                                    .edit().putString("kullanici_rolu", "MUSTERI").apply()
                                arayuzuRoleGoreDuzenle()
                            }
                        }.addOnFailureListener { (activity as? MainActivity)?.gosterLoading(false) }
                }
            }
            .addOnFailureListener {
                (activity as? MainActivity)?.gosterLoading(false)
                if (_binding != null) binding.kullaniciAdiTextView.text = " Misafir"
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
        listeyiGuncelleVeMesajKontrolEt()
    }

    private fun kategoriFiltrele(kategori: String) {
        filtreliListe.clear()
        if (kategori.equals("Tümü", ignoreCase = true)) {
            filtreliListe.addAll(tamListe)
        } else {
            val arananKategori = kategori.lowercase().trim()
            for (item in tamListe) {
                val dbKategori = item.urunKategori?.lowercase()?.trim() ?: ""
                if (dbKategori.contains(arananKategori) || arananKategori.contains(dbKategori)) {
                    filtreliListe.add(item)
                }
            }
        }
        listeyiGuncelleVeMesajKontrolEt()
    }

    private fun verileriGetir() {
        (activity as? MainActivity)?.gosterLoading(true)
        db.collection("Urunler").orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                (activity as? MainActivity)?.gosterLoading(false)
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
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}