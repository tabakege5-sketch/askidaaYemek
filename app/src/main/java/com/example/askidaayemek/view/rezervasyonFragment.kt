package com.example.askidaayemek.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentRezervasyonBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class rezervasyonFragment : Fragment() {

    private var _binding: FragmentRezervasyonBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val talepListesi = ArrayList<HashMap<String, Any>>()
    private lateinit var adapter: RezervasyonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRezervasyonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rezerveToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.rezervasyonRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = RezervasyonAdapter(talepListesi) { secilenTalep ->
            val bundle = Bundle().apply {
                putString("urunId", secilenTalep["docId"] as? String)
                putString("urunAdi", secilenTalep["urunAdi"] as? String)
            }
            findNavController().navigate(
                R.id.action_rezervasyonFragment_to_musteriQrKodFragment,
                bundle
            )
        }
        binding.rezervasyonRecyclerView.adapter = adapter

        talepleriYukleVeKontrolEt()
    }

    private fun talepleriYukleVeKontrolEt() {
        val currentUid = auth.currentUser?.uid ?: return
        val simdikiZaman = Timestamp.now()

        (activity as? MainActivity)?.gosterLoading(true)

        db.collection("Talepler")
            .whereEqualTo("musteriUid", currentUid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                talepListesi.clear()
                val batch = db.batch()
                var stokIadeGerekliMi = false

                for (doc in querySnapshot.documents) {
                    val data = doc.data
                    if (data != null) {
                        val map = HashMap(data)
                        map["docId"] = doc.id

                        val durum = data["durum"] as? String ?: "Beklemede"
                        val gecerlilikTarihi = data["gecerlilikTarihi"] as? Timestamp

                        if (durum == "Beklemede" && gecerlilikTarihi != null && simdikiZaman.seconds > gecerlilikTarihi.seconds) {
                            stokIadeGerekliMi = true
                            val talepRef = db.collection("Talepler").document(doc.id)
                            batch.update(talepRef, "durum", "Zaman Aşımı / İptal Edildi")
                            val asilIlanId = data["asilIlanId"] as? String ?: ""
                            val talepMiktari = (data["miktar"] as? String)?.toIntOrNull() ?: 1

                            if (asilIlanId.isNotEmpty()) {
                                val urunRef = db.collection("Urunler").document(asilIlanId)
                                val geriYuklenecekUrunMap = hashMapOf(
                                    "urunAdi" to (data["urunAdi"] ?: ""),
                                    "yukleyenUid" to (data["yukleyenUid"] ?: ""),
                                    "gorselUrl" to (data["gorselUrl"] ?: ""),
                                    "konum" to (data["konum"] ?: ""),
                                    "miktar" to talepMiktari.toString(),
                                    "tarih" to Timestamp.now()
                                )
                                urunRef.get().addOnSuccessListener { urunDoc ->
                                    val localBatch = db.batch()
                                    if (urunDoc.exists()) {
                                        val eskiMiktar =
                                            (urunDoc.get("miktar")?.toString())?.toIntOrNull() ?: 0
                                        val yeniMiktar = eskiMiktar + talepMiktari
                                        localBatch.update(urunRef, "miktar", yeniMiktar.toString())
                                    } else {
                                        localBatch.set(urunRef, geriYuklenecekUrunMap)
                                    }
                                    localBatch.update(talepRef, "durum", "Zaman Aşımı")
                                    localBatch.commit()
                                }
                            }
                        } else if (durum == "Beklemede") {
                            talepListesi.add(map)
                        }
                    }
                }
                if (stokIadeGerekliMi) {
                    batch.commit().addOnCompleteListener {
                        (activity as? MainActivity)?.gosterLoading(false)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    (activity as? MainActivity)?.gosterLoading(false)
                    adapter.notifyDataSetChanged()
                }

                if (talepListesi.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Aktif rezervasyonunuz bulunmamaktadır",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(
                    context,
                    "Veriler yüklenemedi: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }

    private class RezervasyonAdapter(
        private val liste: List<HashMap<String, Any>>,
        private val onItemClick: (HashMap<String, Any>) -> Unit
    ) : RecyclerView.Adapter<RezervasyonAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val urunIsmi: TextView = view.findViewById(R.id.urunIsmiTextTextView)
            val tarih: TextView = view.findViewById(R.id.tarihTextTextView)
            val miktar: TextView = view.findViewById(R.id.miktarTextTexttView)
            val saat: TextView = view.findViewById(R.id.saatTextTextView)
            val durum: TextView = view.findViewById(R.id.durumTextTextView)
            val resim: ImageView = view.findViewById(R.id.resimImageImageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rezervasyon_satir, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = liste[position]

            holder.urunIsmi.text = "Ürün İsmi: ${item["urunAdi"] ?: "Belirtilmedi"}"
            holder.tarih.text = "Tarih: ${item["secilenTarih"] ?: "Tarih Yok"}"
            holder.miktar.text = "Miktar: ${item["miktar"] ?: "1"}"
            holder.saat.text = "Saat: ${item["saat"] ?: "Belirtilmedi"}"
            holder.durum.text = "Durum: ${item["durum"] ?: "Beklemede"}"

            val gorselData = item["gorselUrl"] as? String
            if (!gorselData.isNullOrEmpty()) {
                try {
                    val bytes = Base64.decode(gorselData, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    holder.resim.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    holder.resim.setImageResource(android.R.drawable.stat_notify_error)
                }
            } else {
                holder.resim.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            holder.itemView.setOnClickListener { onItemClick(item) }
        }

        override fun getItemCount(): Int = liste.size
    }
}