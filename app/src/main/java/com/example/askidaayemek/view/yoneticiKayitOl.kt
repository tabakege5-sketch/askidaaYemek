package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentYoneticiKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class yoneticiKayitOl : Fragment(R.layout.fragment_yonetici_kayit_ol) {

    private var _binding: FragmentYoneticiKayitOlBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val OZEL_YETKI_KODU = "egeninYeri1806"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYoneticiKayitOlBinding.bind(view)

        auth = Firebase.auth
        db = Firebase.firestore
        binding.yoneticiKayitBttonn.setOnClickListener {
            val adSoyad = binding.yonetininIsmiSoyIsmiEditText.text.toString().trim()
            val email = binding.yoneticiEmailEditText.text.toString().trim()
            val sifre = binding.yoneticiSifreEditText.text.toString().trim()
            val girilenKod = binding.yetkiKoduEditText.text.toString().trim()
            if (adSoyad.isEmpty() || email.isEmpty() || sifre.isEmpty() || girilenKod.isEmpty()) {
                Toast.makeText(context, "Eksik bilgi bırakma kavdes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (girilenKod != OZEL_YETKI_KODU) {
                Toast.makeText(context, "Geçersiz Kod", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.yoneticiKayitBttonn.isEnabled = false
            auth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val yoneticiMap = hashMapOf(
                        "adSoyad" to adSoyad,
                        "email" to email,
                        "rol" to "yonetici",
                        "uid" to uid
                    )
                    uid?.let {
                        db.collection("Yoneticiler").document(it).set(yoneticiMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Yönetici Girişi Başarılı", Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            .addOnFailureListener { e ->
                                binding.yoneticiKayitBttonn.isEnabled = true
                                Toast.makeText(context, "Firestore Sorunu: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    binding.yoneticiKayitBttonn.isEnabled = true
                    Toast.makeText(context, "Hata: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}