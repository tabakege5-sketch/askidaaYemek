package com.example.askidaayemek.adapter

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.UrunRecyclerRowBinding

class urunAnaSayfaAdapter(
    private val urunListesi: ArrayList<urun>,
    private val onItemClick: (urun) -> Unit
) : RecyclerView.Adapter<urunAnaSayfaAdapter.urunHolder>() {

    class urunHolder(val binding: UrunRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): urunHolder {
        val binding = UrunRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return urunHolder(binding)
    }

    override fun onBindViewHolder(holder: urunHolder, position: Int) {
        val mevcutUrun = urunListesi[position]

        holder.binding.recyclerUrunAdiText.text = mevcutUrun.urunAdi ?: "İsimsiz Ürün"
        holder.binding.recyclerKonumText.text = "Konum: ${mevcutUrun.konum ?: "Belirtilmedi"}"
        holder.binding.recyclerMiktarText.text = "Miktar: ${mevcutUrun.miktar ?: "Belirtilmedi"}"
        holder.binding.recyclerTarihText.text = "Saat: ${mevcutUrun.saat ?: "--:--"}"

        val gorselData = mevcutUrun.gorselUrl

        if (!gorselData.isNullOrEmpty()) {
            if (gorselData.startsWith("http")) {
                Glide.with(holder.itemView.context)
                    .load(gorselData)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.binding.recyclerImageView)
            } else {
                try {
                    val imageBytes = Base64.decode(gorselData, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    holder.binding.recyclerImageView.setImageBitmap(decodedImage)
                    holder.binding.recyclerImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                } catch (e: Exception) {
                    Log.e("BASE64_HATA", "Resim dönüştürülemedi: ${e.message}")
                    holder.binding.recyclerImageView.setImageResource(android.R.drawable.stat_notify_error)
                }
            }
        } else {
            holder.binding.recyclerImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }


        holder.itemView.setOnClickListener {
            onItemClick(mevcutUrun)
        }
    }

    override fun getItemCount(): Int = urunListesi.size
}