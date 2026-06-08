package com.example.askidaayemek.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentKayitOlBinding
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

class kayitOlFragment : Fragment(R.layout.fragment_kayit_ol) {

    private var _binding: FragmentKayitOlBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "GOOGLE_KONTROL"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKayitOlBinding.bind(view)
        binding.kayitOlToolBar.setNavigationIcon(R.drawable.outline_arrow_back_24)
        binding.kayitOlToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        auth = Firebase.auth
        db = Firebase.firestore

        binding.googleLinearLayout.isEnabled = false
        binding.googleLinearLayout.alpha = 0.5f

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val popupAcici = View.OnClickListener {
            val popup = PopupMenu(requireContext(), binding.editTextViewProfilTipi)
            popup.menu.add("Müşteri")
            popup.menu.add("Yönetici")
            popup.setOnMenuItemClickListener { item ->
                binding.editTextViewProfilTipi.setText(item.title)
                binding.googleLinearLayout.isEnabled = true
                binding.googleLinearLayout.alpha = 1.0f
                true
            }
            popup.show()
        }

        binding.editTextViewProfilTipi.setOnClickListener(popupAcici)

        binding.kaydolButton.setOnClickListener {
            normalKayitYap()
        }

        binding.googleLinearLayout.setOnClickListener {
            (activity as? MainActivity)?.gosterLoading(true)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }
    }

    private fun normalKayitYap() {
        val adSoyad = binding.adSoyadEditTextView.text.toString().trim()
        val email = binding.ePostaEditText.text.toString().trim()
        val sifre = binding.parolalarEditText.text.toString().trim()
        val profilTipi = binding.editTextViewProfilTipi.text.toString().trim()

        if (email.isNotEmpty() && sifre.isNotEmpty() && adSoyad.isNotEmpty() && profilTipi.isNotEmpty()) {
            (activity as? MainActivity)?.gosterLoading(true)

            auth.createUserWithEmailAndPassword(email, sifre).addOnSuccessListener {
                val uid = auth.currentUser?.uid
                val koleksiyonAdi =
                    if (profilTipi.equals("yönetici", true)) "Yoneticiler" else "Kullanicilar"
                kaydetVeYonlendir(uid, adSoyad, email, profilTipi, koleksiyonAdi)
            }.addOnFailureListener { e ->
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Tüm alanları doldur", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                (activity as? MainActivity)?.gosterLoading(false)
                Log.e(TAG, "Google Hatası: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                val adSoyad = auth.currentUser?.displayName ?: "Google Kullanıcısı"
                val email = auth.currentUser?.email ?: ""
                val secilenTip = binding.editTextViewProfilTipi.text.toString()
                val koleksiyonAdi =
                    if (secilenTip.equals("yönetici", true)) "Yoneticiler" else "Kullanicilar"

                kaydetVeYonlendir(uid, adSoyad, email, secilenTip, koleksiyonAdi)
            } else {
                (activity as? MainActivity)?.gosterLoading(false)
            }
        }
    }

    private fun kaydetVeYonlendir(
        uid: String?,
        ad: String,
        mail: String,
        tip: String,
        koleksiyon: String
    ) {
        if (uid != null) {
            val userMap =
                hashMapOf("adSoyad" to ad, "email" to mail, "profilTipi" to tip, "uid" to uid)
            db.collection(koleksiyon).document(uid).set(userMap).addOnSuccessListener {
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(context, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                findNavController().navigate(kayitOlFragmentDirections.actionKayitOlFragmentToGirisLoginFragment())
            }.addOnFailureListener {
                (activity as? MainActivity)?.gosterLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}