package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding

class yoneticiQrKodFragment : Fragment(R.layout.fragment_yonetici_qr_kod) {
    private var _binding: FragmentYoneticiQrKodBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYoneticiQrKodBinding.bind(view)

        binding.geriGitImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonTeslimEt.setOnClickListener {
            Toast.makeText(context, "Ürün Teslim Edildi", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}