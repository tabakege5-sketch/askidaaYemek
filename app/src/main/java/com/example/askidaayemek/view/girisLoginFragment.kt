package com.example.askidaayemek.view

import android.content.Context
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(org.maplibre.android.R.string.maplibre_offline_error_region_definition_invalid))
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
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            kullaniciTipiniKontrolEtVeYonlendir(uid)
                        }
                    } else {
                        binding.girisButton.isEnabled = true
                        Toast.makeText(
                            context,
                            "Giriş Başarısız e-Mail veya Şifre Hatalı",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(context, "Lütfen bosluğu doldur", Toast.LENGTH_SHORT).show()
            }
        }

        binding.kayitOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToKayitOlFragment())
        }

        binding.yoneticiOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToYoneticiKayitOl())
        }

        binding.googleTextView.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }
    }

    private fun kullaniciTipiniKontrolEtVeYonlendir(uid: String) {
        Log.d("LOGIN_DEBUG", "Giriş yapan UID: $uid")
        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)

        db.collection("Yoneticiler").document(uid).get()
            .addOnSuccessListener { adminDoc ->
                if (adminDoc.exists()) {
                    Log.d("LOGIN_DEBUG", "Yönetici başarılı giriş yaptı ana sayfaya aktarılıyo")
                    sharedPref.edit().putString("kullanici_rolu", "YONETICI").apply()

                    findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
                } else {
                    db.collection("Kullanicilar").document(uid).get()
                        .addOnSuccessListener { userDoc ->
                            if (userDoc.exists()) {
                                Log.d(
                                    "LOGIN_DEBUG",
                                    "Müşteri başarılı giriş yaptı ana sayfaya gidiyor"
                                )
                                sharedPref.edit().putString("kullanici_rolu", "MUSTERI").apply()

                                findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
                            } else {
                                Log.d("LOGIN_DEBUG", "Kayıt bulunamadı kullanıcı oluşturuluyor")
                                varsayilanKullaniciOlustur(uid)
                            }
                        }
                        .addOnFailureListener {
                            binding.girisButton.isEnabled = true
                            sharedPref.edit().putString("kullanici_rolu", "MUSTERI").apply()
                            findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
                        }
                }
            }
            .addOnFailureListener {
                binding.girisButton.isEnabled = true
                Toast.makeText(
                    context,
                    "Veritabanı hatası: ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun varsayilanKullaniciOlustur(uid: String) {
        val yeniKullanici = hashMapOf(
            "e-posta" to auth.currentUser?.email,
            "kullaniciTipi" to "Müşteri",
            "uid" to uid
        )
        db.collection("Kullanicilar").document(uid).set(yeniKullanici)
            .addOnSuccessListener {
                val sharedPref =
                    requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
                sharedPref.edit().putString("kullanici_rolu", "MUSTERI").apply()

                findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
            }
            .addOnFailureListener {
                binding.girisButton.isEnabled = true
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) kullaniciTipiniKontrolEtVeYonlendir(auth.currentUser!!.uid)
                }
            } catch (e: Exception) {
                Log.e("LOGIN_DEBUG", "Google Hatası: ${e.message}")
                binding.girisButton.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}