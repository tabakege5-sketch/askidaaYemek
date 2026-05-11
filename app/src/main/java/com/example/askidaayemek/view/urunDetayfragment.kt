package com.example.askidaayemek.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.adapter.gorselKaydiriciAdapter
import com.example.askidaayemek.databinding.FragmentUrunDetayfragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class urunDetayfragment : Fragment(R.layout.fragment_urun_detayfragment) {

    private var _binding: FragmentUrunDetayfragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunDetayfragmentBinding.bind(view)

        val ornekResimler = listOf(
            "resim_url_1", "resim_url_2", "resim_url_3",
            "resim_url_4", "resim_url_5", "resim_url_6"
        )
        val sliderAdapter = gorselKaydiriciAdapter(ornekResimler)
        binding.kaydirmaliResim.adapter = sliderAdapter

        TabLayoutMediator(binding.noktaLayout, binding.kaydirmaliResim) { _, _ -> }.attach()
        binding.duzenleSilPaylasImageButton.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.detay_menuler, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_duzenle -> {
                        Toast.makeText(requireContext(), "Düzenlendi", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_sil -> {
                        Toast.makeText(requireContext(), "Ürün Silindi", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_paylas -> {
                        paylasimYap("Bu yemeği Paylaşcaksın Ye Diye")
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
        binding.askDanAllButton.setOnClickListener {
            if (binding.tarihZamanRadioButton.isChecked) {
                val action =
                    urunDetayfragmentDirections.actionUrunDetayfragmentToTarihVeZamanFragment()
                findNavController().navigate(action)
            } else if (binding.hemenRadioButton.isChecked) {
                Toast.makeText(requireContext(), "Ürünü Aldın", Toast.LENGTH_SHORT).show()
                val action = urunDetayfragmentDirections.actionUrunDetayfragmentToTaleplerFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Lütfen bir alım yöntemi seç", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun paylasimYap(mesaj: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, mesaj)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}