package com.example.askidaayemek.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentGirisLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class girisLoginFragment : Fragment(R.layout.fragment_giris_login) {

    private var _binding: FragmentGirisLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGirisLoginBinding.bind(view)

        auth = Firebase.auth
        db = Firebase.firestore

        alanlariTemizle()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.girisButton.setOnClickListener {
            val email = binding.eMailText.text.toString().trim()
            val sifre = binding.editTextSifre.text.toString().trim()

            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                binding.girisButton.isEnabled = false
                auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        alanlariTemizle()
                        auth.currentUser?.uid?.let { kullaniciTipiniKontrolEtVeYonlendir(it) }
                    } else {
                        binding.girisButton.isEnabled = true
                        Toast.makeText(context, "Hata VAR: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(context, "E-posta ve parola giriniz", Toast.LENGTH_SHORT).show()
            }
        }
        binding.yoneticiOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToYoneticiKayitOl())
        }
        binding.sifremiUnuttumTextView.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToParolaFragment())
        }
        binding.googleTextView.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }
        binding.kayitOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToKayitOlFragment())
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("HATA OLDU", "Google girişi iptal edildi: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                alanlariTemizle()
                auth.currentUser?.uid?.let { kullaniciTipiniKontrolEtVeYonlendir(it) }
            } else {
                Toast.makeText(context, "Google Giriş Başarısız", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun kullaniciTipiniKontrolEtVeYonlendir(uid: String) {
        db.collection("Yoneticiler").document(uid).get().addOnSuccessListener { document ->
            if (document.exists()) {
                findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToYonetenListeFragment())
            } else {
                db.collection("Kullanicilar").document(uid).get().addOnSuccessListener { userDoc ->
                    if (userDoc.exists()) {
                        findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToUrunAnaSayfa())
                    } else {
                        Toast.makeText(context, "Kayıt bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun alanlariTemizle() {
        if (_binding != null) {
            binding.eMailText.setText("")
            binding.editTextSifre.setText("")
            binding.girisButton.isEnabled = true
            binding.yoneticiOlButton.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}