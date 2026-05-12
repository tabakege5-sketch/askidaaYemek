package com.example.askidaayemek.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.R
import com.example.askidaayemek.adapter.talepAdapter
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentTaleplerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class taleplerFragment : Fragment() {

    private var _binding: FragmentTaleplerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: talepAdapter
    private var talepListesi = ArrayList<urun>()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaleplerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.firestore
        auth = Firebase.auth
        binding.geriButtons.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.taleplerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = talepAdapter(talepListesi)
        binding.taleplerRecyclerView.adapter = adapter

        verileriGetir()
    }

    private fun verileriGetir() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("Talepler")
                .whereEqualTo("yukleyenUid", currentUser.uid)
                .orderBy("tarih", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) return@addSnapshotListener
                    if (value != null) {
                        talepListesi.clear()
                        for (doc in value.documents) {
                            val urunObjesi = doc.toObject(urun::class.java)
                            if (urunObjesi != null) talepListesi.add(urunObjesi)
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