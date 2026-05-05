package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class kayitOlFragment : Fragment(R.layout.fragment_kayit_ol) {

    private var _binding: FragmentKayitOlBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKayitOlBinding.bind(view)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.kaydolButton.setOnClickListener {
            val adSoyad = binding.adSoyadEditTextView.text.toString().trim()
            val email = binding.ePostaEditText.text.toString().trim()
            val sifre = binding.parolaEditText.text.toString().trim()
            val profilTipi = binding.editTextViewProfilTipi.text.toString().trim()

            if (email.isNotEmpty() && sifre.isNotEmpty() && adSoyad.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, sifre)
                    .addOnSuccessListener {
                        // Firestore'a kullanıcı bilgilerini kaydedelim
                        val userMap = hashMapOf(
                            "adSoyad" to adSoyad,
                            "email" to email,
                            "profilTipi" to profilTipi,
                            "uid" to auth.currentUser?.uid
                        )

                        db.collection("Kullanicilar").document(auth.currentUser!!.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Kaydolma Başarılı!", Toast.LENGTH_SHORT).show()
                                val action = kayitOlFragmentDirections.actionKayitOlFragmentToUrunAnaSayfa()
                                findNavController().navigate(action)
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(context, "Eksik bilgi bırakma kral!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}