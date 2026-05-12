package com.example.askidaayemek.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.databinding.FragmentUrunEkleBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class urunEkleFragment : Fragment(R.layout.fragment_urun_ekle) {
    private var _binding: FragmentUrunEkleBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var secilenGorselUri: Uri? = null
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunEkleBinding.bind(view)
        registerLaunchers()
        binding.secImageButton.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.urunler_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                binding.urunlerEditText.setText(item.title)
                true
            }
            popup.show()
        }
        binding.urunFotografiImageView.setOnClickListener { gorselSec() }
        binding.vazgecButton.setOnClickListener { findNavController().popBackStack() }
        binding.yayNlaButton.setOnClickListener {
            yayinlaIslemi()
        }
    }

    private fun yayinlaIslemi() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(requireContext(), "Hata: Önce giriş yapmalısınız!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val urunKategori = binding.urunlerEditText.text.toString().trim()
        val urunAdi = binding.urunAdiEditText.text.toString().trim()
        val ekNot = binding.ekNotEditText.text.toString().trim()
        val konum = binding.mevcutKonumTextView.text.toString().trim()
        val miktar = binding.miktarEditText.text.toString().trim()
        if (urunKategori.isEmpty() || urunAdi.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Lütfen kategori ve ürün adını doldurun!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        binding.yayNlaButton.isEnabled = false
        val gorselYolu = secilenGorselUri?.toString() ?: ""

        veritabaninaKaydet(urunKategori, urunAdi, ekNot, konum, miktar, user.uid, gorselYolu)
    }

    private fun veritabaninaKaydet(
        kategori: String,
        ad: String,
        not: String,
        konum: String,
        miktar: String,
        uid: String,
        gorselPath: String
    ) {
        val sdfSaat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val suAnkiSaat = sdfSaat.format(Date())

        val urunMap = hashMapOf(
            "urunKategori" to kategori,
            "urunAdi" to ad,
            "miktar" to if (miktar.isEmpty()) "Belirtilmedi" else miktar,
            "ekNot" to not,
            "konum" to konum,
            "yukleyenUid" to uid,
            "tarih" to Timestamp.now(),
            "saat" to suAnkiSaat,
            "durum" to "Askıda",
            "gorselUrl" to gorselPath
        )

        db.collection("Urunler").add(urunMap)
            .addOnSuccessListener {
                if (_binding != null) {
                    Toast.makeText(requireContext(), "Başarıyla Paylaşıldı!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
            }
            .addOnFailureListener { e ->
                if (_binding != null) {
                    Toast.makeText(
                        requireContext(),
                        "Firestore Hatası: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.yayNlaButton.isEnabled = true
                }
            }
    }

    private fun gorselSec() {
        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                storagePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(storagePermission)
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK)
            intentToGallery.type = "image/*"
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    secilenGorselUri = result.data?.data
                    binding.urunFotografiImageView.setImageURI(secilenGorselUri)
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    val intentToGallery = Intent(Intent.ACTION_PICK)
                    intentToGallery.type = "image/*"
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(requireContext(), "Galeri izni reddedildi!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}