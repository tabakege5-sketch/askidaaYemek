package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.databinding.TaleplerRecyclerRowBinding

class yonetenListeAdapter(

    private val teslimatListesi: List<HashMap<String, String>>
) : RecyclerView.Adapter<yonetenListeAdapter.yonetenViewHolder>() {

    class yonetenViewHolder(val binding: TaleplerRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): yonetenViewHolder {
        val binding =
            TaleplerRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return yonetenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: yonetenViewHolder, position: Int) {
        val teslimat = teslimatListesi[position]

        holder.binding.musteriAdTextView.text = "Müşteri: ${teslimat["adSoyad"]}"
        holder.binding.urunBilgiTextView.text =
            "Ürün: ${teslimat["urunAdi"]} | Miktar: ${teslimat["miktar"]}"
    }

    override fun getItemCount(): Int = teslimatListesi.size
}