package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentIlanEkleBinding

class ilanEkleFragment : Fragment(R.layout.fragment_ilan_ekle) {

    private var _binding: FragmentIlanEkleBinding? = null
    private val binding get() = _binding!!
    private var secilenKategori: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIlanEkleBinding.bind(view)
        val kategoriler = arrayOf(
            "Ana Yemekler",
            "Çorbalar",
            "Tatlılar",
            "Hamur İşleri",
            "Ekmekler",
            "Soğuk İçecekler"
        )
        binding.kategoriTuruTextView.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Kategori Seç KRAL")

            builder.setItems(kategoriler) { dialog, which ->
                secilenKategori = kategoriler[which]
                binding.kategoriTuruTextView.setText(secilenKategori)
            }

            builder.show()
        }
        binding.ilanKaydetButton.setOnClickListener {
            val baslik = binding.ilanDetayVeAdresBilgisi.text.toString().trim()
            val detayAdres = binding.ilanDetayVeAdresBilgisi.text.toString().trim()
            if (baslik.isNotEmpty() && detayAdres.isNotEmpty() && secilenKategori.isNotEmpty()) {
                Toast.makeText(context, "İlanın $secilenKategori olarak paylaşıldı!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_ilanEkleFragment2_to_haritaFragment)
            } else {
                Toast.makeText(context, "Kategori dahil tüm alanları doldur KRAL", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}