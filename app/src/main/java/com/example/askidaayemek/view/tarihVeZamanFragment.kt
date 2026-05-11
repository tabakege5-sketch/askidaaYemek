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

        binding.takvimView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            secilenTarih = "$dayOfMonth.${month + 1}.$year"
        }
        if (secilenTarih.isEmpty()) {
            val bugün = Calendar.getInstance()
            secilenTarih = "${bugün.get(Calendar.DAY_OF_MONTH)}.${bugün.get(Calendar.MONTH) + 1}.${bugün.get(Calendar.YEAR)}"
        }

        binding.secimiKaydetButton.setOnClickListener {
            val saat = binding.tamSaatTimePicker.hour
            val dakika = binding.tamSaatTimePicker.minute
            val formatliZaman = String.format("%02d:%02d", saat, dakika)

            val ekBilgi = binding.bilgiEditText.text.toString()

            if (ekBilgi.isNotEmpty()) {

                Toast.makeText(context, "Randevu: $secilenTarih saat $formatliZaman alındı", Toast.LENGTH_LONG).show()
                findNavController().popBackStack(R.id.urunAnaSayfa, false)
            } else {
                Toast.makeText(context, "Örün Bilgisini gir", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}