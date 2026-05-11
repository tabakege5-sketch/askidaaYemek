package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentUrunAnaSayfaBinding

class urunAnaSayfa : Fragment(R.layout.fragment_urun_ana_sayfa) {
    private var _binding: FragmentUrunAnaSayfaBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunAnaSayfaBinding.bind(view)
        binding.siraliRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.askDaQrAlmaFloatingActionButton.setOnClickListener {
            findNavController().navigate(urunAnaSayfaDirections.actionUrunAnaSayfaToMusteriQrKodFragment())
        }
        binding.urunDetayaGitFloatingActionButton.setOnClickListener {
            findNavController().navigate(urunAnaSayfaDirections.actionUrunAnaSayfaToUrunEkleFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}