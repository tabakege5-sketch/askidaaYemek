package com.example.askidaayemek.view

import android.app.AlertDialog
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
    private var secilenAnlikTalep: urun? = null
    private var secilenPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaleplerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taleplerToolBar.post {
            val params = binding.taleplerToolBar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.taleplerToolBar.layoutParams = params
        }

        db = Firebase.firestore
        auth = Firebase.auth
        binding.taleplerToolBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_taleplerFragment_to_urunAnaSayfa)
        }


        binding.taleplerToolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_sil || menuItem.title == "Sil") {
                if (secilenAnlikTalep != null && secilenPosition != -1) {
                    silmeOnayIletisimiGoster(secilenAnlikTalep!!, secilenPosition)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Lütfen önce silinecek talebi seçin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            } else {
                false
            }
        }

        binding.taleplerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = talepAdapter(
            talepListesi = talepListesi,
            onItemClick = { secilenTalep ->
                secilenAnlikTalep = secilenTalep
                secilenPosition = talepListesi.indexOf(secilenTalep)
                Toast.makeText(
                    requireContext(),
                    "${secilenTalep.urunAdi} seçildi. Durum: ${secilenTalep.ekNot ?: "Beklemede"}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onOnaylaClick = { onaylanacakTalep ->
                onaylaVeStokDus(onaylanacakTalep)
            },
            onIptalClick = { iptalEdilecekTalep, position ->
                silmeOnayIletisimiGoster(iptalEdilecekTalep, position)
            }
        )
        binding.taleplerRecyclerView.adapter = adapter
        binding.askidaQrAlmaFlootingButton.setOnClickListener {
            qrIsleminiBaslat()
        }

        verileriGetir()
    }

    private fun silmeOnayIletisimiGoster(talep: urun, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Talebi Sil")
            .setMessage("${talep.urunAdi} talebini silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet, Sil") { dialog, _ ->
                talepIptalEt(talep, position)
                dialog.dismiss()
            }
            .setNegativeButton("Vazgeç") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun onaylaVeStokDus(talep: urun) {
        val talepId = talep.urunId ?: return
        val asilIlanId = talep.ekNot ?: ""

        val batch = db.batch()
        val talepRef = db.collection("Talepler").document(talepId)
        batch.update(talepRef, "durum", "Onaylandı")

        if (asilIlanId.isNotEmpty()) {
            val urunRef = db.collection("Urunler").document(asilIlanId)
            urunRef.get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val mevcutMiktar = doc.getString("miktar")?.toIntOrNull() ?: 0
                    val talepMiktari = talep.miktar?.toIntOrNull() ?: 1
                    val yeniMiktar = mevcutMiktar - talepMiktari

                    if (yeniMiktar <= 0) batch.delete(urunRef)
                    else batch.update(urunRef, "miktar", yeniMiktar.toString())
                }
                batch.commit().addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Talep Onaylandı, Stok Güncellendi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            batch.commit()
        }
    }

    private fun talepIptalEt(talep: urun, position: Int) {
        talep.urunId?.let { id ->
            db.collection("Talepler").document(id).delete().addOnSuccessListener {
                Toast.makeText(context, "Talep başarıyla silindi.", Toast.LENGTH_SHORT).show()
                adapter.siraliElemanSil(position)
                if (secilenAnlikTalep?.urunId == id) {
                    secilenAnlikTalep = null
                    secilenPosition = -1
                }
            }
        }
    }

    private fun qrIsleminiBaslat() {
        if (secilenAnlikTalep == null) {
            Toast.makeText(
                context,
                "Lütfen listeden işlem yapmak istediğiniz talebi seçin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val uid = auth.currentUser?.uid ?: return
        db.collection("Yoneticiler").document(uid).get().addOnSuccessListener { doc ->
            val bundle = Bundle().apply {
                putString("urunId", secilenAnlikTalep?.urunId)
                putString("urunAdi", secilenAnlikTalep?.urunAdi)
            }
            if (doc.exists()) {
                findNavController().navigate(
                    R.id.action_taleplerFragment_to_yoneticiQrKodFragment,
                    bundle
                )
            } else {
                findNavController().navigate(
                    R.id.action_taleplerFragment_to_musteriQrKodFragment,
                    bundle
                )
            }
        }
    }

    private fun verileriGetir() {
        val currentUser = auth.currentUser ?: return
        db.collection("Talepler")
            .whereEqualTo("yukleyenUid", currentUser.uid)
            .orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || _binding == null) return@addSnapshotListener

                talepListesi.clear()
                snapshot?.documents?.forEach { doc ->
                    doc.toObject(urun::class.java)?.let {
                        it.urunId = doc.id
                        if (doc.getString("durum") == null) {
                            it.ekNot = "Beklemede"
                        } else {
                            it.ekNot = doc.getString("durum")
                        }
                        talepListesi.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}