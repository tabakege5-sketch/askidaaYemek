package com.example.askidaayemek.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth

class profilFragment : Fragment(R.layout.fragment_profil) {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfilBinding.bind(view)

        binding.kullaniciEmailTextView.text = auth.currentUser?.email ?: "Giriş yapılmamış"
        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")
        binding.kullaniciRolTextView.text = "Rol: $rol"
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.koyuModSvitch.isChecked = isDarkMode

        binding.koyuModSvitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.cikisYapButtonn.setOnClickListener {
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Çıkış Yap")
                .setMessage("Hesabınızdan çıkış yapmak istediğinize emin misin Reiss")
                .setPositiveButton("Çıkış Yap") { _, _ ->
                    (activity as? MainActivity)?.gosterLoading(true)

                    auth.signOut()
                    sharedPref.edit().remove("kullanici_rolu").apply()
                    (activity as? MainActivity)?.gosterLoading(false)

                    findNavController().navigate(R.id.action_profilFragment_to_girisLoginFragment)
                }
                .setNegativeButton("İptal", null)
                .show()
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}