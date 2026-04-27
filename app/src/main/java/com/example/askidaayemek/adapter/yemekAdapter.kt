package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.dataClass.yemek
import com.example.askidaayemek.databinding.YemekLayoutBinding

class yemekAdapter(private var yemekListesi: ArrayList<yemek>) :
    RecyclerView.Adapter<yemekAdapter.YemekViewHolder>() {
        class YemekViewHolder(val binding: YemekLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val binding = YemekLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YemekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {
        val yemek = yemekListesi[position]
        holder.binding.yemekAdiTextView.text = yemek.yemekIsmi
        holder.binding.kategoriTextView.text = yemek.kategoriAdi
    }

    override fun getItemCount(): Int = yemekListesi.size

    fun listeyiGuncelle(yeniListe: List<yemek>) {
        yemekListesi.clear()
        yemekListesi.addAll(yeniListe)
        notifyDataSetChanged()
    }
}