package com.example.askidaayemek.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
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
import com.google.firebase.auth.*
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
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.girisButton.setOnClickListener {
            val email = binding.eMailText.text.toString().trim()
            val sifre = binding.editTextSifre.text.toString().trim()

            if (email.isEmpty() || sifre.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Lütfen e-mail ve şifreyi girin",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!isInternetAvailable()) {
                hataGoster("Bağlantı Hatası", "İnternet bağlantınızı kontrol edin.")
                return@setOnClickListener
            }

            binding.girisButton.isEnabled = false
            (activity as? MainActivity)?.gosterLoading(true)

            auth.signInWithEmailAndPassword(email, sifre)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        kullaniciTipiniKontrolEtVeYonlendir(uid)
                    } else {
                        girisTamamlandi()
                    }
                }
                .addOnFailureListener { e ->
                    girisTamamlandi()
                    handleLoginError(e)
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

    private fun handleLoginError(e: Exception) {
        val mesaj = when {
            e is FirebaseAuthInvalidUserException ->
                "Bu e-posta adresiyle kayıtlı bir hesap bulunamadı"

            e is FirebaseAuthInvalidCredentialsException -> {
                val errorMsg = e.message?.lowercase() ?: ""
                when {
                    errorMsg.contains("password") -> "Şifreniz hatalı Lütfen tekrar deneyin"
                    errorMsg.contains("user") || errorMsg.contains("email") -> "E-posta adresi hatalı veya kayıtlı değil"
                    else -> "e-mail veya şifre hatalı"
                }
            }

            e is FirebaseAuthEmailException ->
                "yanlış e-mail"

            e is FirebaseAuthWeakPasswordException ->
                "Yanlış Şifre"

            else -> {
                if (e.message?.contains("network", ignoreCase = true) == true) {
                    "İnternet bağlantınızı kontrol edin"
                } else {
                    "Giriş yapılamadı Bilgilerinizi kontrol edin"
                }
            }
        }

        hataGoster("Giriş Hatası", mesaj)
    }

    private fun girisTamamlandi() {
        (activity as? MainActivity)?.gosterLoading(false)
        binding.girisButton.isEnabled = true
    }

    private fun hataGoster(baslik: String, mesaj: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(baslik)
            .setMessage(mesaj)
            .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun kullaniciTipiniKontrolEtVeYonlendir(uid: String) {
        db.collection("Yoneticiler").document(uid).get()
            .addOnSuccessListener { adminDoc ->
                if (adminDoc.exists()) {
                    requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
                        .edit().putString("kullanici_rolu", "YONETICI").apply()
                    findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
                } else {
                    db.collection("Kullanicilar").document(uid).get()
                        .addOnSuccessListener { userDoc ->
                            if (userDoc.exists()) {
                                requireActivity().getSharedPreferences(
                                    "AskidaYemekPref",
                                    Context.MODE_PRIVATE
                                )
                                    .edit().putString("kullanici_rolu", "MUSTERI").apply()
                                findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
                            } else {
                                varsayilanKullaniciOlustur(uid)
                            }
                        }
                        .addOnFailureListener { varsayilanKullaniciOlustur(uid) }
                }
            }
            .addOnFailureListener { varsayilanKullaniciOlustur(uid) }
    }

    private fun varsayilanKullaniciOlustur(uid: String) {
        val yeniKullanici = hashMapOf(
            "e-posta" to auth.currentUser?.email,
            "kullaniciTipi" to "Müşteri",
            "uid" to uid
        )
        db.collection("Kullanicilar").document(uid).set(yeniKullanici)
            .addOnCompleteListener {
                requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
                    .edit().putString("kullanici_rolu", "MUSTERI").apply()
                findNavController().navigate(R.id.action_girisLoginFragment_to_urunAnaSayfa)
            }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        kullaniciTipiniKontrolEtVeYonlendir(auth.currentUser!!.uid)
                    }
                    .addOnFailureListener { e ->
                        hataGoster("Google Giriş Hatası", "Google ile giriş yapılamadı")
                    }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Google girişi iptal edildi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}