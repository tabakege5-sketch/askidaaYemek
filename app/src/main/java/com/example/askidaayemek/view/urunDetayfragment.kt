package com.example.askidaayemek.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentUrunDetayfragmentBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class urunDetayfragment : Fragment(R.layout.fragment_urun_detayfragment) {

    private var _binding: FragmentUrunDetayfragmentBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var mevcutUrun: urun? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUrunDetayfragmentBinding.bind(view)
        val gelenUrunId = arguments?.getString("urunId")

        binding.urunDetayToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val sharedPref = requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")

        if (rol == "YONETICI") {
            binding.duzenleSilPaylasImageButton.visibility = View.VISIBLE
        } else {
            binding.duzenleSilPaylasImageButton.visibility = View.GONE
        }
        if (!gelenUrunId.isNullOrEmpty()) {
            db.collection("Urunler").document(gelenUrunId).get()
                .addOnSuccessListener { doc ->
                    if (_binding != null && doc.exists()) {
                        mevcutUrun = doc.toObject(urun::class.java)?.apply { urunId = doc.id }
                        mevcutUrun?.let { urun ->
                            binding.apply {
                                urunDetayToolbar.title = urun.urunAdi ?: "Ürün Detayı"
                                miktarTextView.text = "Miktar: ${urun.miktar ?: "Belirtilmedi"}"
                                aciklamaTextView.text = "Açıklama: ${urun.ekNot ?: "Açıklama yok"}"
                                konumTextView.text = urun.konum ?: "Konum belirtilmedi"
                            }
                            resimYukle(urun.gorselUrl)
                        }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Ürün ID'si bulunamadı!", Toast.LENGTH_SHORT).show()
        }

        binding.konumTextView.setOnClickListener {
            findNavController().navigate(R.id.action_urunDetayfragment_to_haritaFragment)
        }

        binding.duzenleSilPaylasImageButton.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.detay_menuler, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_paylas -> {
                        paylas(mevcutUrun)
                        true
                    }
                    R.id.menu_sil -> {
                        true
                    }
                    R.id.menu_duzenle -> {
                        if (mevcutUrun != null) {
                            val bundle = Bundle().apply {
                                putParcelable("duzenlenecekUrun", mevcutUrun)
                            }
                            findNavController().navigate(R.id.action_urunDetayfragment_to_urunEkleFragment, bundle)
                        } else {
                            Toast.makeText(context, "Ürün verisi henüz yüklenmedi!", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.tarihZamanRadioButton.setOnClickListener {
            if (mevcutUrun != null) {
                val bundle = Bundle().apply {
                    putParcelable("secilenUrun", mevcutUrun)
                }
                findNavController().navigate(R.id.action_urunDetayfragment_to_tarihVeZamanFragment, bundle)
            } else {
                Toast.makeText(context, "Ürün yükleniyor, lütfen bekleyin...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.askDanAllButton.setOnClickListener {
            val currentUserUid = auth.currentUser?.uid

            if (currentUserUid == null) {
                Toast.makeText(context, "Önce giriş yapın", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mevcutUrun == null) {
                Toast.makeText(context, "Ürün bilgisi alınamadı", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.hemenRadioButton.isChecked) {
                binding.askDanAllButton.isEnabled = false

                val yeniTalepMap = hashMapOf(
                    "urunId" to mevcutUrun!!.urunId,
                    "urunAdi" to mevcutUrun!!.urunAdi,
                    "miktar" to mevcutUrun!!.miktar,
                    "konum" to mevcutUrun!!.konum,
                    "gorselUrl" to mevcutUrun!!.gorselUrl,
                    "tarih" to Timestamp.now(),
                    "durum" to "Beklemede",
                    "talepTipi" to "Hemen Al",
                    "yukleyenUid" to currentUserUid,
                    "ekNot" to mevcutUrun!!.urunId,
                    "urunKategori" to mevcutUrun!!.urunKategori
                )

                db.collection("Talepler")
                    .add(yeniTalepMap)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Başarıyla alındı", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_urunDetayfragment_to_taleplerFragment)
                    }
                    .addOnFailureListener { e ->
                        binding.askDanAllButton.isEnabled = true
                        Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Tarih Seçin", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resimYukle(gorselData: String?) {
        if (!gorselData.isNullOrEmpty()) {
            if (gorselData.startsWith("http")) {
                Glide.with(this).load(gorselData).into(binding.detayImageView)
            } else {
                try {
                    val imageBytes = Base64.decode(gorselData, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.detayImageView.setImageBitmap(decodedImage)
                } catch (e: Exception) {
                    binding.detayImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        }
    }

    private fun paylas(urun: urun?) {
        val mesaj = "${urun?.urunAdi ?: "Ürün"} Hemen bitmeden al: ${urun?.konum ?: ""}"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, mesaj)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Paylaş"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}