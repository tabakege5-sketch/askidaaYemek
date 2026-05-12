package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.UrunRecyclerRowBinding
import com.example.askidaayemek.view.urunAnaSayfaDirections

class urunAnaSayfaAdapter(private val urunListesi: ArrayList<urun>) : RecyclerView.Adapter<urunAnaSayfaAdapter.urunHolder>() {

    class urunHolder(val binding: UrunRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): urunHolder {
        val binding = UrunRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return urunHolder(binding)
    }

    override fun onBindViewHolder(holder: urunHolder, position: Int) {
        val mevcutUrun = urunListesi[position]

        // 1. Verileri UI'ya bağlama
        holder.binding.recyclerUrunAdiText.text = mevcutUrun.urunAdi ?: "İsimsiz Ürün"
        holder.binding.recyclerKonumText.text = "Konum: ${mevcutUrun.konum ?: "Belirtilmedi"}"
        holder.binding.recyclerMiktarText.text = "Miktar: ${mevcutUrun.miktar ?: "Belirtilmedi"}"
        holder.binding.recyclerTarihText.text = "Saat: ${mevcutUrun.saat ?: ""}"

        // 2. Görsel yükleme (Glide)
        val gorselUrl = mevcutUrun.gorselUrl
        if (!gorselUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(gorselUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .centerCrop()
                .into(holder.binding.recyclerImageView)
        } else {
            holder.binding.recyclerImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // 3. TIKLAMA OLAYI (GÜNCELLENEN KISIM)
        holder.itemView.setOnClickListener {
            // secilenUrun argümanına mevcutUrun nesnesini gönderiyoruz
            val action = urunAnaSayfaDirections.actionUrunAnaSayfaToUrunDetayfragment(mevcutUrun)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return urunListesi.size
    }
}