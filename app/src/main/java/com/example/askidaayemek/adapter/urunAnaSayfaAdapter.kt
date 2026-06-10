package com.example.askidaayemek.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.UrunRecyclerRowBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

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

        var gösterilecekSaat = "--:--"
        if (mevcutUrun.tarih is Timestamp) {
            try {
                val timestamp = mevcutUrun.tarih as Timestamp
                val date = timestamp.toDate()

                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                gösterilecekSaat = sdf.format(date)
            } catch (e: Exception) {
                Log.e("SAAT_HATA", "Saat dönüştürülürken hata: ${e.message}")
            }
        } else if (!mevcutUrun.saat.isNullOrEmpty()) {

            gösterilecekSaat = mevcutUrun.saat!!
        }
        holder.binding.recyclerTarihText.text = "Saat: $gösterilecekSaat"

        val gorselData = mevcutUrun.gorselUrl

        if (!gorselData.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(gorselData, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.binding.recyclerImageView.setImageBitmap(decodedImage)
                holder.binding.recyclerImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            } catch (e: Exception) {
                Log.e("BASE64_HATA", "Görsel çözülürken hata oluştu: ${e.message}")
                holder.binding.recyclerImageView.setImageResource(android.R.drawable.stat_notify_error)
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