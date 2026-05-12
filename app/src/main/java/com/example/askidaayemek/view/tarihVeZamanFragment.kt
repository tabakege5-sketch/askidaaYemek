package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding
import java.util.Calendar

class tarihVeZamanFragment : Fragment(R.layout.fragment_tarih_ve_zaman) {
    private var _binding: FragmentTarihVeZamanBinding? = null
    private val binding get() = _binding!!
    private var secilenTarih = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTarihVeZamanBinding.bind(view)

        // 1. Geri Butonu Tıklama Olayı (Yeni eklediğimiz buton)
        binding.tarihGeriButton.setOnClickListener {
            // Bir önceki sayfaya (Ürün Detay) geri döner
            findNavController().popBackStack()
        }

        // 2. Takvimden Tarih Seçimi
        binding.takvimView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            secilenTarih = "$dayOfMonth.${month + 1}.$year"
        }

        // Eğer kullanıcı tarih seçmezse bugünün tarihini varsayılan yapıyoruz
        if (secilenTarih.isEmpty()) {
            val bugün = Calendar.getInstance()
            secilenTarih = "${bugün.get(Calendar.DAY_OF_MONTH)}.${bugün.get(Calendar.MONTH) + 1}.${bugün.get(Calendar.YEAR)}"
        }

        // 3. Kaydet Butonu Tıklama Olayı
        binding.secimiKaydetButton.setOnClickListener {
            val saat = binding.tamSaatTimePicker.hour
            val dakika = binding.tamSaatTimePicker.minute
            val formatliZaman = String.format("%02d:%02d", saat, dakika)

            val ekBilgi = binding.bilgiEditText.text.toString()

            if (ekBilgi.isNotEmpty()) {
                // Burada işlemleri tamamlayıp kullanıcıya bilgi veriyoruz
                Toast.makeText(context, "Randevu: $secilenTarih saat $formatliZaman olarak kaydedildi.", Toast.LENGTH_LONG).show()

                // İşlem bitince ana sayfaya kadar temizleyerek dönüyoruz
                findNavController().popBackStack(R.id.urunAnaSayfa, false)
            } else {
                // Not girilmediyse uyarı veriyoruz
                Toast.makeText(context, "Lütfen bir açıklama veya not girin reisim!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}