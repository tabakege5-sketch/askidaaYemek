package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.TalepLayoutBinding
import com.example.askidaayemek.dataClass.urun

class talepAdapter(private val talepListesi: ArrayList<urun>) : RecyclerView.Adapter<talepAdapter.talepHolder>() {

    class talepHolder(val binding: TalepLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): talepHolder {
        val binding = TalepLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return talepHolder(binding)
    }

    override fun onBindViewHolder(holder: talepHolder, position: Int) {
        val talepItem = talepListesi[position]
        holder.binding.urununAdiTextView.text = "Ürün İsmi: ${talepItem.urunAdi ?: "Belirtilmedi"}"
        holder.binding.textViewTarih.text = "Tarih: ${talepItem.tarih ?: "Belirtilmedi"}"
        holder.binding.saatTextView.text = "Saat: ${talepItem.saat ?: "Belirtilmedi"}"
        holder.binding.durumuGosterTextView.text = "Durum: Askıdan Alındı"
        Glide.with(holder.itemView.context)
            .load(talepItem.gorselUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.urununImageView)
        holder.binding.iptalImageButton.setOnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.menuInflater.inflate(R.menu.onay_iptal_menuler, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_onayla -> {
                        Toast.makeText(holder.itemView.context, "${talepItem.urunAdi} Onaylandı", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_sil -> {
                        val currentPosition = holder.adapterPosition
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            talepListesi.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                            notifyItemRangeChanged(currentPosition, talepListesi.size)
                            Toast.makeText(holder.itemView.context, "Silindi", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = talepListesi.size
}