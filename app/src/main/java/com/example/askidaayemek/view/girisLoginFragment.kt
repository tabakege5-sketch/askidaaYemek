package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentGirisLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class girisLoginFragment : Fragment(R.layout.fragment_giris_login) {

    private var _binding: FragmentGirisLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val TAG = "FIREBASE_KONTROL"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGirisLoginBinding.bind(view)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.girisButton.setOnClickListener {
            val email = binding.eMailText.text.toString().trim()
            val sifre = binding.editTextSifre.text.toString().trim()

            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                Log.d(TAG, "Giriş denemesi yapılıyor: $email")
                auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        Log.d(TAG, "Auth başarılı! Giriş yapan UID: $uid")
                        if (uid != null) {
                            kullaniciTipiniKontrolEtVeYonlendir(uid)
                        }
                    } else {
                        Log.e(TAG, "Auth Hatası: ${task.exception?.localizedMessage}")
                        Toast.makeText(context, "Hata: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(context, "Lütfen alanları doldur!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.kayitOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToKayitOlFragment())
        }

        binding.yoneticiOlButton.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToYoneticiKayitOl())
        }

        binding.sifremiUnuttumTextView.setOnClickListener {
            findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToParolaFragment())
        }
    }

    private fun kullaniciTipiniKontrolEtVeYonlendir(uid: String) {
        Log.d(TAG, "Firestore kontrolü başladı. Aranan Koleksiyon: Yoneticiler, Aranan UID: $uid")

        db.collection("Yoneticiler").document(uid).get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.d(TAG, "Yoneticiler koleksiyonunda kayıt BULUNDU!")
                Toast.makeText(context, "Yönetici Hoş Geldin", Toast.LENGTH_SHORT).show()
                findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToYonetenListeFragment())
            } else {
                Log.w(TAG, "Yoneticiler'de yok. Kullanicilar koleksiyonuna bakılıyor...")

                db.collection("Kullanicilar").document(uid).get().addOnSuccessListener { userDoc ->
                    if (userDoc.exists()) {
                        Log.d(TAG, "Kullanicilar koleksiyonunda kayıt BULUNDU!")
                        Toast.makeText(context, "Müşteri Hoş Geldin", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(girisLoginFragmentDirections.actionGirisLoginFragmentToUrunAnaSayfa())
                    } else {
                        Log.e(TAG, "HATA: İki koleksiyonda da bu UID ile eşleşen döküman yok!")
                        Toast.makeText(context, "Kullanıcı kaydı bulunamadı!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Firestore Veritabanı Hatası: ${exception.localizedMessage}")
            Toast.makeText(context, "Veritabanı hatası!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}