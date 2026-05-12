package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.R
import com.example.askidaayemek.adapter.urunAnaSayfaAdapter
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentUrunPaylasanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class urunPaylasanFragment : Fragment(R.layout.fragment_urun_paylasan) {
    private var _binding: FragmentUrunPaylasanBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: urunAnaSayfaAdapter
    private lateinit var paylasilanUrunlerListesi: ArrayList<urun>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunPaylasanBinding.bind(view)
        db = Firebase.firestore
        auth = Firebase.auth
        paylasilanUrunlerListesi = ArrayList()
        binding.listeReceyclerVew.layoutManager = LinearLayoutManager(context)
        adapter = urunAnaSayfaAdapter(paylasilanUrunlerListesi)
        binding.listeReceyclerVew.adapter = adapter
        verileriGetir()
        binding.paylasMYapButton.setOnClickListener {
            val action = urunPaylasanFragmentDirections.actionUrunPaylasanFragmentToUrunEkleFragment()
            findNavController().navigate(action)
        }
        binding.geriGitResimView.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun verileriGetir() {
        val guncelKullaniciUid = auth.currentUser?.uid

        if (guncelKullaniciUid != null) {
            db.collection("Urunler")
                .whereEqualTo("yukleyenUid", guncelKullaniciUid)
                .orderBy("tarih", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Toast.makeText(context, "Hata: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        paylasilanUrunlerListesi.clear()
                        for (document in value.documents) {
                            val ad = document.get("urunAdi") as? String
                            val miktar = document.get("miktar") as? String
                            val konum = document.get("konum") as? String
                            val kategori = document.get("urunKategori") as? String
                            val saat = document.get("saat") as? String
                            val tarihRaw = document.get("tarih")
                            val tarihStr = if (tarihRaw is com.google.firebase.Timestamp) {
                                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                sdf.format(tarihRaw.toDate())
                            } else {
                                tarihRaw?.toString() ?: ""
                            }

                            val urunObj = urun(ad, miktar, konum, "", tarihStr, saat, kategori)
                            paylasilanUrunlerListesi.add(urunObj)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}