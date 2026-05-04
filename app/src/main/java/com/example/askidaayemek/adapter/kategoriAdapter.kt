package com.example.askidaayemek.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.dataClass.kategori
import com.example.askidaayemek.databinding.KategoriRecyclerViewBinding

class kategoriAdapter(
    private var kategoriListesi: List<kategori>,
    private val onItemClick: (kategori) -> Unit
) : RecyclerView.Adapter<kategoriAdapter.KategoriViewHolder>() {

    class KategoriViewHolder(val binding: KategoriRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun listeyiGuncelle(yeniListe: List<kategori>) {
        kategoriListesi = yeniListe
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val binding = KategoriRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return KategoriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val item = kategoriListesi[position]
        holder.binding.stokMiktariTextView.text = item.isim.uppercase()
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = kategoriListesi.size
}