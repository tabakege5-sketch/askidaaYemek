package com.example.askidaayemek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.TalepLayoutBinding
import com.example.askidaayemek.dataClass.urun

class talepAdapter(
    private val talepListesi: ArrayList<urun>,
    private val onItemClick: (urun) -> Unit,
    private val onOnaylaClick: (urun) -> Unit,
    private val onIptalClick: (urun, Int) -> Unit
) : RecyclerView.Adapter<talepAdapter.talepHolder>() {

    class talepHolder(val binding: TalepLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): talepHolder {
        val binding = TalepLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return talepHolder(binding)
    }

    override fun onBindViewHolder(holder: talepHolder, position: Int) {
        val talepItem = talepListesi[position]

        holder.binding.urununAdiTextView.text = "Ürün İsmi: ${talepItem.urunAdi ?: "Belirtilmedi"}"
        holder.binding.miktarTextView.text = "Miktar: ${talepItem.miktar ?: "Belirtilmedi"}"
        holder.binding.textViewTarih.text = "Tarih: ${talepItem.tarih ?: "Belirtilmedi"}"
        holder.binding.saatTextView.text = "Saat: ${talepItem.saat ?: "Belirtilmedi"}"
        holder.binding.durumuGosterTextView.text = "Durum: ${talepItem.durum ?: "Beklemede"}"

        Glide.with(holder.itemView.context)
            .load(talepItem.gorselUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.urununImageView)

        holder.itemView.setOnClickListener {
            onItemClick(talepItem)
        }

        holder.binding.iptalImageButton.setOnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.menuInflater.inflate(R.menu.onay_iptal_menuler, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    when (item.itemId) {
                        R.id.menu_onayla -> {
                            onOnaylaClick(talepItem)
                            true
                        }
                        R.id.menu_sil -> {
                            onIptalClick(talepItem, currentPosition)
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            popup.show()
        }
    }
    override fun getItemCount(): Int = talepListesi.size

    fun siraliElemanSil(position: Int) {
        if (position >= 0 && position < talepListesi.size) {
            talepListesi.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, talepListesi.size)
        }
    }
}