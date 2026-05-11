package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class musteriQrKodFragment : Fragment(R.layout.fragment_musteri_qr_kod) {

    private var _binding: FragmentMusteriQrKodBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusteriQrKodBinding.bind(view)

        val currentUserUid = Firebase.auth.currentUser?.uid
        binding.askDanQrAlmaTextView.text = "Benim Kodum: $currentUserUid"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}