package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
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
    private var secilenAnlikTalep: urun? = null

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

        binding.taleplerToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.taleplerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = talepAdapter(
            talepListesi = talepListesi,
            onItemClick = { secilenTalep ->
                secilenAnlikTalep = secilenTalep
                Toast.makeText(
                    requireContext(),
                    "Seçildi: ${secilenTalep.urunAdi} (Artık sağ alttan QR işlemini başlatabilirsin)",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onOnaylaClick = { onaylanacakTalep ->
                if (!onaylanacakTalep.urunId.isNullOrEmpty()) {
                    val batch = db.batch()
                    val talepRef = db.collection("Talepler").document(onaylanacakTalep.urunId!!)
                    batch.delete(talepRef)

                    val asilIlanId = onaylanacakTalep.ekNot

                    if (!asilIlanId.isNullOrEmpty()) {
                        val urunRef = db.collection("Urunler").document(asilIlanId)

                        urunRef.get().addOnSuccessListener { urunDoc ->
                            if (urunDoc.exists()) {
                                val mevcutMiktarString = urunDoc.get("miktar")?.toString() ?: "0"
                                val mevcutMiktar = mevcutMiktarString.toIntOrNull() ?: 0
                                val talepEdilenMiktar = onaylanacakTalep.miktar?.toIntOrNull() ?: 1

                                val yeniMiktar = mevcutMiktar - talepEdilenMiktar

                                if (yeniMiktar <= 0) {
                                    batch.delete(urunRef)
                                } else {
                                    batch.update(urunRef, "miktar", yeniMiktar.toString())
                                }
                            }
                            batch.commit().addOnSuccessListener {
                                Toast.makeText(context, "${onaylanacakTalep.urunAdi} onaylandı, stoktan düşüldü ve işlem tamamlandı.", Toast.LENGTH_SHORT).show()
                                if (secilenAnlikTalep?.urunId == onaylanacakTalep.urunId) {
                                    secilenAnlikTalep = null
                                }
                            }.addOnFailureListener { e ->
                                Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            batch.commit().addOnSuccessListener {
                                if (secilenAnlikTalep?.urunId == onaylanacakTalep.urunId) {
                                    secilenAnlikTalep = null
                                }
                            }
                        }
                    } else {
                        batch.commit().addOnSuccessListener {
                            Toast.makeText(context, "Talep başarıyla tamamlandı ve temizlendi.", Toast.LENGTH_SHORT).show()
                            if (secilenAnlikTalep?.urunId == onaylanacakTalep.urunId) {
                                secilenAnlikTalep = null
                            }
                        }
                    }
                }
            },
            onIptalClick = { iptalEdilecekTalep, position ->
                if (!iptalEdilecekTalep.urunId.isNullOrEmpty()) {
                    db.collection("Talepler").document(iptalEdilecekTalep.urunId!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "İptal edildi", Toast.LENGTH_SHORT).show()
                            adapter.siraliElemanSil(position)
                            if (secilenAnlikTalep?.urunId == iptalEdilecekTalep.urunId) {
                                secilenAnlikTalep = null
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Silme başarısız: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        )
        binding.taleplerRecyclerView.adapter = adapter

        binding.askidaQrAlmaFlootingButton.setOnClickListener {
            if (secilenAnlikTalep == null) {
                Toast.makeText(context, "Lütfen önce listeden QR kodunu alacağınız talebe tıklayın", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid != null) {
                db.collection("Yoneticiler").document(currentUserUid).get()
                    .addOnSuccessListener { doc ->
                        val qrBundle = Bundle().apply {
                            putString("urunId", secilenAnlikTalep?.urunId)
                            putString("urunAdi", secilenAnlikTalep?.urunAdi)
                        }

                        if (doc.exists()) {
                            findNavController().navigate(R.id.action_taleplerFragment_to_yoneticiQrKodFragment, qrBundle)
                        } else {
                            findNavController().navigate(R.id.action_taleplerFragment_to_musteriQrKodFragment, qrBundle)
                        }
                    }
                    .addOnFailureListener {
                        val qrBundle = Bundle().apply {
                            putString("urunId", secilenAnlikTalep?.urunId)
                            putString("urunAdi", secilenAnlikTalep?.urunAdi)
                        }
                        findNavController().navigate(R.id.action_taleplerFragment_to_musteriQrKodFragment, qrBundle)
                    }
            } else {
                Toast.makeText(context, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
            }
        }

        verileriGetir()
    }

    private fun verileriGetir() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("Talepler")
                .whereEqualTo("yukleyenUid", currentUser.uid)
                .orderBy("tarih", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null || _binding == null) return@addSnapshotListener

                    if (value != null) {
                        talepListesi.clear()
                        for (doc in value.documents) {
                            val urunObjesi = doc.toObject(urun::class.java)
                            if (urunObjesi != null) {
                                urunObjesi.urunId = doc.id
                                talepListesi.add(urunObjesi)
                            }
                        }
                        binding.taleplerRecyclerView.post {
                            if (_binding != null) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}