package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Binding Bağlantısı
        _binding = FragmentYoneticiKayitOlBinding.bind(view)
        Log.d("yoneticiKayit", "View Created ve Binding bağlandı")

        auth = Firebase.auth
        db = Firebase.firestore

        // 2. Buton Tıklama Olayı
        binding.yoneticiKayitBttonn.setOnClickListener {
            Log.d("yoneticiKayit", "Butona basıldı!") // Logcat'te bunu görmen lazım

            val adSoyad = binding.yonetininIsmiSoyIsmiEditText.text.toString().trim()
            val email = binding.yoneticiEmailEditText.text.toString().trim()
            val sifre = binding.yoneticiSifreEditText.text.toString().trim()
            val yetkiKodu = binding.yetkiKoduEditText.text.toString().trim()

            if (yetkiKodu == "123456") {
                if (adSoyad.isNotEmpty() && email.isNotEmpty() && sifre.isNotEmpty()) {
                    Log.d("yoneticiKayit", "Firebase işlemi başlıyor...")

                    auth.createUserWithEmailAndPassword(email, sifre)
                        .addOnSuccessListener {
                            Log.d("yoneticiKayit", "Auth başarılı, Firestore'a yazılıyor...")
                            val adminMap = hashMapOf(
                                "adSoyad" to adSoyad,
                                "email" to email,
                                "role" to "admin",
                                "uid" to auth.currentUser?.uid
                            )
                            db.collection("Yoneticiler").document(auth.currentUser!!.uid)
                                .set(adminMap)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Yönetici Hesabı Oluşturuldu!", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Log.e("yoneticiKayit", "Hata: ${e.message}")
                            Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(context, "Lütfen tüm alanları doldur!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Yetki kodu hatalı!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}