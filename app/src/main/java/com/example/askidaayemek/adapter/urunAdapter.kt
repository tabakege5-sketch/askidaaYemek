package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.askidaayemek.databinding.PaylasimListesiBinding
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.view.urunAnaSayfaDirections

class urunAdapter(private val urunListesi: ArrayList<urun>) : RecyclerView.Adapter<urunAdapter.urunHolder>() {

    class urunHolder(val binding: PaylasimListesiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): urunHolder {
        val binding = PaylasimListesiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return urunHolder(binding)
    }

    override fun onBindViewHolder(holder: urunHolder, position: Int) {
        val urunListe = urunListesi[position]
        holder.binding.urunIsimleriTextView.text = urunListe.urunAdi
        holder.binding.urununAdediPorsiyonuTextView.text = urunListe.miktar
        holder.binding.urununKonumTextView.text = urunListe.konum
        Glide.with(holder.itemView.context)
            .load(urunListe.gorselUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.binding.urunlerinResmiImageView)
        holder.itemView.setOnClickListener {
            val action = urunAnaSayfaDirections.actionUrunAnaSayfaToUrunDetayfragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int = urunListesi.size
}