package com.example.askidaayemek.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentParolaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class parolaFragment : Fragment(R.layout.fragment_parola) {

    private var _binding: FragmentParolaBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentParolaBinding.bind(view)

        auth = Firebase.auth

        binding.gonderButton.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()

            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Sıfırlama linki gönderildi kral!", Toast.LENGTH_LONG).show()
                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(context, "Hata: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(context, "E-posta girmen lazım!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.textView4.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}