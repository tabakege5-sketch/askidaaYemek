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
    private var paylasilanUrunlerListesi = ArrayList<urun>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunPaylasanBinding.bind(view)
        db = Firebase.firestore
        auth = Firebase.auth
        binding.listeReceyclerVew.layoutManager = LinearLayoutManager(context)
        adapter = urunAnaSayfaAdapter(paylasilanUrunlerListesi) { secilenUrun ->
            val bundle = Bundle().apply {
                putString("urunId", secilenUrun.urunId)
            }
            findNavController().navigate(R.id.action_urunAnaSayfa_to_urunDetayfragment, bundle)
        }
        binding.listeReceyclerVew.adapter = adapter

        verileriGetir()

        binding.paylasMYapButton.setOnClickListener {
            findNavController().navigate(R.id.action_urunPaylasanFragment_to_urunEkleFragment)
        }

        binding.yayinlananUrunlerToolBar.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun verileriGetir() {
        val currentUid = auth.currentUser?.uid ?: return

        db.collection("Urunler")
            .whereEqualTo("yukleyenUid", currentUid)
            .orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (value != null && _binding != null) {
                    paylasilanUrunlerListesi.clear()
                    for (doc in value.documents) {
                        doc.toObject(urun::class.java)?.let { paylasilanUrunlerListesi.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}