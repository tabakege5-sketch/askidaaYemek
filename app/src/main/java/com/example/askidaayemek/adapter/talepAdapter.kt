package com.example.askidaayemek.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.TalepLayoutBinding
import com.example.askidaayemek.dataClass.urun
import java.text.SimpleDateFormat
import java.util.Locale

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

        val timestamp = talepItem.tarih as? com.google.firebase.Timestamp
        if (timestamp != null) {
            val dateObject = timestamp.toDate()

            val tarihFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val saatFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            holder.binding.textViewTarih.text = "Tarih: ${tarihFormat.format(dateObject)}"
            holder.binding.saatTextView.text = "Saat: ${saatFormat.format(dateObject)}"
        } else {
            holder.binding.textViewTarih.text = "Tarih: Tarih yok"
            holder.binding.saatTextView.text = "Saat: Belirtilmedi"
        }

        holder.binding.durumuGosterTextView.text = "Durum: ${talepItem.durum ?: "Beklemede"}"

        val gorselData = talepItem.gorselUrl
        if (!gorselData.isNullOrEmpty()) {
            try {
                val temizBase64 = if (gorselData.contains(",")) {
                    gorselData.substringAfter(",")
                } else {
                    gorselData
                }

                val bytes = Base64.decode(temizBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                if (bitmap != null) {
                    holder.binding.urununImageView.setImageBitmap(bitmap)
                } else {
                    holder.binding.urununImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                holder.binding.urununImageView.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.binding.urununImageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener {
            onItemClick(talepItem)
        }

        holder.binding.iptalImageButton.setOnClickListener { view ->
            val popup = PopupMenu(holder.itemView.context, view)
            popup.menuInflater.inflate(R.menu.onay_iptal_menuler, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                val currentPosition = holder.adapterPosition
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

    override fun getItemCount(): Int {
        return talepListesi.size
    }

    fun siraliElemanSil(position: Int) {
        if (position >= 0 && position < talepListesi.size) {
            talepListesi.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, talepListesi.size)
        }
    }
}