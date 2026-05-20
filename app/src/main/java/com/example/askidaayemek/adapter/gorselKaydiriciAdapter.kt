package com.example.askidaayemek.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.ItemResimKaydirmaBinding

class gorselKaydiriciAdapter(private val resimListesi: List<String>) : RecyclerView.Adapter<gorselKaydiriciAdapter.SliderViewHolder>() {

    class SliderViewHolder(val binding: ItemResimKaydirmaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemResimKaydirmaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val resimUrl = resimListesi[position]
        Log.d("GLIDE_KONTROL", "Yüklenen URL: $resimUrl")

        Glide.with(holder.itemView.context)
            .load(resimUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.binding.yanaKaydRmaImageView)
    }

    override fun getItemCount(): Int = resimListesi.size
}