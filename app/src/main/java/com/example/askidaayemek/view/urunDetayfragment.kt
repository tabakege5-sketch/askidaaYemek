package com.example.askidaayemek.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentUrunDetayfragmentBinding

class urunDetayfragment : Fragment(R.layout.fragment_urun_detayfragment) {

    private var _binding: FragmentUrunDetayfragmentBinding? = null
    private val binding get() = _binding!!

    // Navigasyondan gelen veriyi yakalamak için
    private val args: urunDetayfragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunDetayfragmentBinding.bind(view)

        // Gelen ürünü değişken olarak alalım
        val secilenUrun = args.secilenUrun

        secilenUrun?.let { urun ->
            // --- Verileri UI'ya Bağlıyoruz ---
            binding.urunDetayTextView.text = urun.urunAdi ?: "Ürün Detayı"
            binding.miktarTextView.text = "Miktar: ${urun.miktar ?: "Belirtilmedi"}"
            binding.aciklamaTextView.text = "Açıklama: ${urun.ekNot ?: "Açıklama bulunmuyor."}"
            binding.konumTextView.text = urun.konum ?: "Konum belirtilmedi"

            // --- Resmi Basıyoruz (Garantili Yöntem) ---
            Glide.with(requireContext())
                .load(urun.gorselUrl)
                .placeholder(android.R.drawable.ic_menu_gallery) // Yüklenirken çıkacak resim
                .error(android.R.drawable.stat_notify_error)    // Hata olursa çıkacak resim
                .into(binding.detayImageView) // XML'deki yeni ImageView ID'si
        }

        // Popup Menü İşlemleri
        binding.duzenleSilPaylasImageButton.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.detay_menuler, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_paylas -> {
                        paylasimYap("${secilenUrun?.urunAdi} askıda! Hemen alabilirsin.")
                        true
                    }
                    // Buraya ileride Silme veya Düzenleme menülerini de ekleyebilirsin kral
                    else -> false
                }
            }
            popup.show()
        }

        // Alt taraftaki "Askıdan Al" Butonu
        binding.askDanAllButton.setOnClickListener {
            if (binding.tarihZamanRadioButton.isChecked) {
                // Tarih ve Zaman seçiliyse oraya git
                val action = urunDetayfragmentDirections.actionUrunDetayfragmentToTarihVeZamanFragment()
                findNavController().navigate(action)
            } else if (binding.hemenRadioButton.isChecked) {
                // Hemen al seçiliyse QR kod sayfasına git
                val action = urunDetayfragmentDirections.actionUrunDetayfragmentToMusteriQrKodFragment()
                findNavController().navigate(action)
            } else {
                // Hiçbiri seçili değilse uyar
                Toast.makeText(requireContext(), "Lütfen bir alım yöntemi seçin reisim!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Telefonun paylaşma sistemini açar
    private fun paylasimYap(mesaj: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, mesaj)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Ürünü Paylaş"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}