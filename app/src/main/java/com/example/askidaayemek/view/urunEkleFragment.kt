package com.example.askidaayemek.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentUrunEkleBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class urunEkleFragment : Fragment(R.layout.fragment_urun_ekle) {

    private var _binding: FragmentUrunEkleBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var secilenGorselUri: Uri? = null
    private var guncellenecekUrun: urun? = null
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUrunEkleBinding.bind(view)

        binding.urunEkleToolbar.post {
            val params = binding.urunEkleToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.urunEkleToolbar.layoutParams = params
        }
        registerLaunchers()
        binding.urunEkleToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        @Suppress("DEPRECATION")
        guncellenecekUrun = arguments?.getParcelable<urun>("duzenlenecekUrun")

        guncellenecekUrun?.let { urun ->
            binding.urunEkleToolbar.title = "Ürünü Güncelle"
            binding.yayNlaButton.text = "GÜNCELLE"
            binding.urunlerEditText.setText(urun.urunKategori)
            binding.urunAdiEditText.setText(urun.urunAdi)
            binding.miktarEditText.setText(urun.miktar)
            binding.ekNotEditText.setText(urun.ekNot)
            binding.mevcutKonumTextView.text = urun.konum
            resimYukle(urun.gorselUrl)
        } ?: run {
            binding.urunEkleToolbar.title = "Ürün Ekle"
            binding.yayNlaButton.text = "YAYINLA"
        }

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
            if (guncellenecekUrun != null) {
                guncellemeIslemi()
            } else {
                yayinlaIslemi()
            }
        }
    }

    private fun resimYukle(gorselData: String?) {
        if (!gorselData.isNullOrEmpty()) {
            if (gorselData.startsWith("http")) {
                Glide.with(this).load(gorselData).into(binding.urunFotografiImageView)
            } else {
                try {
                    val imageBytes = Base64.decode(gorselData, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.urunFotografiImageView.setImageBitmap(decodedImage)
                } catch (e: Exception) {
                    binding.urunFotografiImageView.setImageResource(R.drawable.ic_launcher_background)
                }
            }
        }
    }

    private fun guncellemeIslemi() {
        val guncelMap = hashMapOf<String, Any>(
            "urunKategori" to binding.urunlerEditText.text.toString(),
            "urunAdi" to binding.urunAdiEditText.text.toString(),
            "miktar" to binding.miktarEditText.text.toString(),
            "ekNot" to binding.ekNotEditText.text.toString(),
            "konum" to binding.mevcutKonumTextView.text.toString()
        )
        secilenGorselUri?.let { uri -> uriToBase64(uri)?.let { guncelMap["gorselUrl"] = it } }

        db.collection("Urunler")
            .whereEqualTo("urunAdi", guncellenecekUrun?.urunAdi)
            .whereEqualTo("yukleyenUid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot) {
                    db.collection("Urunler").document(doc.id).update(guncelMap)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Başarıyla Güncellendi", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                }
            }
    }

    private fun yayinlaIslemi() {
        val user = auth.currentUser ?: return
        val urunKategori = binding.urunlerEditText.text.toString().trim()
        val urunAdi = binding.urunAdiEditText.text.toString().trim()
        val ekNot = binding.ekNotEditText.text.toString().trim()
        val konum = binding.mevcutKonumTextView.text.toString().trim()
        val miktar = binding.miktarEditText.text.toString().trim()

        if (urunKategori.isEmpty() || urunAdi.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen kategori ve ürün adını doldur", Toast.LENGTH_SHORT).show()
            return
        }

        binding.yayNlaButton.isEnabled = false
        val gorselVerisi = secilenGorselUri?.let { uriToBase64(it) } ?: ""
        veritabaninaKaydet(urunKategori, urunAdi, ekNot, konum, miktar, user.uid, gorselVerisi)
    }

    private fun veritabaninaKaydet(kategori: String, ad: String, not: String, konum: String, miktar: String, uid: String, gorselPath: String) {
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
                Toast.makeText(requireContext(), "Başarıyla Paylaşıldı", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Hata: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                binding.yayNlaButton.isEnabled = true
            }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) { null }
    }

    private fun gorselSec() {
        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(requireContext(), storagePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(storagePermission)
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK)
            intentToGallery.type = "image/*"
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLaunchers() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                secilenGorselUri = result.data?.data
                binding.urunFotografiImageView.setImageURI(secilenGorselUri)
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intentToGallery = Intent(Intent.ACTION_PICK)
                intentToGallery.type = "image/*"
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}