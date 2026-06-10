package com.example.askidaayemek.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.databinding.TaleplerRecyclerRowBinding

class yonetenListeAdapter(
    private val teslimatListesi: ArrayList<HashMap<String, String>>, // Fragment ile uyumlu olması için ArrayList yapıldı
    private val onItemClick: (HashMap<String, String>) -> Unit // 🛠️ EKLEME: Tıklama olayını yakalayan lambda fonksiyonu
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
        val context = holder.itemView.context
        holder.binding.musteriAdTextView.text = "Müşteri: ${teslimat["adSoyad"]}"
        val durum = teslimat["durum"] ?: "Beklemede"
        val tarihSaat = teslimat["tarih"] ?: "Zaman belirtilmedi"
        holder.binding.urunBilgiTextView.text =
            "Ürün: ${teslimat["urunAdi"]} | Miktar: ${teslimat["miktar"]}\nDurum: $durum\nZaman: $tarihSaat"

        val gorselData = teslimat["gorselUrl"]
        if (!gorselData.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(gorselData, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.binding.rowUrunImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.binding.rowUrunImageView.setImageResource(android.R.drawable.stat_notify_error)
            }
        } else {
            holder.binding.rowUrunImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        when (durum) {
            "Onaylandı", "Teslim Edildi" -> {
                holder.binding.urunBilgiTextView.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                )
            }

            "Silindi", "İptal Edildi" -> {
                holder.binding.urunBilgiTextView.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
                )
            }

            else -> {
                holder.binding.urunBilgiTextView.setTextColor(
                    ContextCompat.getColor(context, android.R.color.darker_gray)
                )
            }
        }
        holder.itemView.setOnClickListener {
            onItemClick(teslimat)
        }
    }

    override fun getItemCount(): Int = teslimatListesi.size
}