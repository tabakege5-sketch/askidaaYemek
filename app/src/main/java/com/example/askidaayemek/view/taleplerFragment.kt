package com.example.askidaayemek.view

import android.app.AlertDialog
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
import java.util.ArrayList

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
            if (_binding != null) {
                val params = binding.taleplerToolBar.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = 100
                binding.taleplerToolBar.layoutParams = params
            }
        }

        db = Firebase.firestore
        auth = Firebase.auth


        yoneticiKorumasiKontrolEt()

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
                qrIsleminiBaslat()
            },
            onOnaylaClick = { onaylanacakTalep ->
                talebiOnaylaVeMiktariDus(onaylanacakTalep)
            },
            onIptalClick = { iptalEdilecekTalep, position ->
                silmeOnayIletisimiGoster(iptalEdilecekTalep, position)
            }
        )
        binding.taleplerRecyclerView.adapter = adapter
        verileriGetir()
    }

    private fun talebiOnaylaVeMiktariDus(talep: urun) {
        val talepId = talep.urunId ?: return
        val asilIlanId = talep.ekNot ?: ""

        if (asilIlanId.isEmpty()) {
            Toast.makeText(context, "Asıl ilan kimliği bulunamadı!", Toast.LENGTH_SHORT).show()
            return
        }

        (activity as? MainActivity)?.gosterLoading(true)
        val asilUrunRef = db.collection("Urunler").document(asilIlanId)
        val talepRef = db.collection("Talepler").document(talepId)

        db.runTransaction { transaction ->
            val urunSnapshot = transaction.get(asilUrunRef)
            if (!urunSnapshot.exists()) {
                throw Exception("Asıl ilan artık mevcut değil!")
            }
            val guncelStokStr = urunSnapshot.get("miktar")?.toString() ?: "0"
            val mevcutStok = guncelStokStr.toIntOrNull() ?: 0

            val talepEdilenMiktar = talep.miktar?.toIntOrNull() ?: 1

            if (mevcutStok < talepEdilenMiktar) {
                throw Exception("Yetersiz stok! Mevcut: $mevcutStok, İstenen: $talepEdilenMiktar")
            }

            val yeniStok = mevcutStok - talepEdilenMiktar
            if (urunSnapshot.get("miktar") is Number) {
                transaction.update(asilUrunRef, "miktar", yeniStok)
            } else {
                transaction.update(asilUrunRef, "miktar", yeniStok.toString())
            }

            transaction.update(talepRef, "durum", "Onaylandı")

        }.addOnSuccessListener {
            (activity as? MainActivity)?.gosterLoading(false)
            if (_binding != null) {
                Toast.makeText(
                    context,
                    "${talep.urunAdi} onaylandı ve stok güncellendi!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener { e ->
            (activity as? MainActivity)?.gosterLoading(false)
            if (_binding != null) {
                Toast.makeText(context, "İşlem başarısız: ${e.localizedMessage}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun yoneticiKorumasiKontrolEt() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("Yoneticiler").document(uid).get().addOnSuccessListener { doc ->
            if (_binding == null) return@addOnSuccessListener
            if (doc.exists()) {
                Toast.makeText(
                    context,
                    "Yönetici Paneline Yönlendiriliyorsunuz...",
                    Toast.LENGTH_SHORT
                ).show()
                // NavGraph'ındaki action_taleplerFragment_to_yonetenListeFragment rotasını tetikliyoruz.
                findNavController().navigate(R.id.action_taleplerFragment_to_yonetenListeFragment)
            }
        }
    }

    private fun silmeOnayIletisimiGoster(talep: urun, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Talebi İptal Et")
            .setMessage("${talep.urunAdi} talebini iptal etmek istediğinize emin misiniz?")
            .setPositiveButton("Sil") { dialog, _ ->
                talepIptalEt(talep, position)
                dialog.dismiss()
            }
            .setNegativeButton("Vazgeç") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun talepIptalEt(talep: urun, position: Int) {
        val id = talep.urunId ?: return
        (activity as? MainActivity)?.gosterLoading(true)
        db.collection("Talepler").document(id).delete().addOnSuccessListener {
            (activity as? MainActivity)?.gosterLoading(false)
            if (_binding != null) {
                Toast.makeText(context, "Talebiniz iptal edildi", Toast.LENGTH_SHORT).show()
                adapter.siraliElemanSil(position)
                if (secilenAnlikTalep?.urunId == id) {
                    secilenAnlikTalep = null
                    secilenPosition = -1
                }
            }
        }.addOnFailureListener {
            (activity as? MainActivity)?.gosterLoading(false)
        }
    }

    private fun qrIsleminiBaslat() {
        if (secilenAnlikTalep == null) {
            Toast.makeText(
                context,
                "Lütfen işlem yapmak istediğiniz talebi seçin",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val bundle = Bundle().apply {
            putString("urunId", secilenAnlikTalep?.urunId)
            putString("urunAdi", secilenAnlikTalep?.urunAdi)
            putString("asilIlanId", secilenAnlikTalep?.ekNot)
            putString("miktar", secilenAnlikTalep?.miktar)
        }
        findNavController().navigate(R.id.action_taleplerFragment_to_musteriQrKodFragment, bundle)
    }

    private fun verileriGetir() {
        val currentUser = auth.currentUser ?: return
        (activity as? MainActivity)?.gosterLoading(true)
        db.collection("Talepler")
            .whereEqualTo("musteriUid", currentUser.uid)
            .orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                (activity as? MainActivity)?.gosterLoading(false)
                if (error != null) {
                    Log.e(
                        "FIRESTORE_ERROR",
                        "İndeks hatası, yedek sorguya geçiliyor: ${error.localizedMessage}"
                    )
                    yedekSorguCalistir()
                    return@addSnapshotListener
                }

                if (_binding == null) return@addSnapshotListener

                talepListesi.clear()
                snapshot?.documents?.forEach { doc ->
                    val itTalep = urun().apply {
                        this.urunId = doc.id
                        this.urunAdi = doc.getString("urunAdi") ?: "İsimsiz Ürün"
                        this.miktar = doc.get("miktar")?.toString() ?: "1"

                        this.gorselUrl = doc.getString("gorselUrl") ?: ""
                        this.konum = doc.getString("konum") ?: ""
                        this.durum = doc.getString("durum") ?: "Beklemede"
                        this.tarih = doc.getTimestamp("tarih")
                        this.ekNot = doc.getString("asilIlanId") ?: ""
                    }
                    talepListesi.add(itTalep)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun yedekSorguCalistir() {
        val currentUser = auth.currentUser ?: return
        db.collection("Talepler")
            .whereEqualTo("musteriUid", currentUser.uid)
            .addSnapshotListener { snapshot, _ ->
                if (_binding == null) return@addSnapshotListener
                talepListesi.clear()
                snapshot?.documents?.forEach { doc ->
                    val itTalep = urun().apply {
                        this.urunId = doc.id
                        this.urunAdi = doc.getString("urunAdi") ?: "İsimsiz Ürün"
                        this.miktar = doc.get("miktar")?.toString() ?: "1"

                        this.gorselUrl = doc.getString("gorselUrl") ?: ""
                        this.konum = doc.getString("konum") ?: ""
                        this.durum = doc.getString("durum") ?: "Beklemede"
                        this.tarih = doc.getTimestamp("tarih")
                        this.ekNot = doc.getString("asilIlanId") ?: ""
                    }
                    talepListesi.add(itTalep)
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}