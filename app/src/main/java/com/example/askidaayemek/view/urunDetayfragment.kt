package com.example.askidaayemek.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
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

        binding.urunDetayToolbar.post {
            val params = binding.urunDetayToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.urunDetayToolbar.layoutParams = params
        }

        val gelenUrunId = arguments?.getString("urunId")

        binding.urunDetayToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")
        binding.duzenleSilPaylasImageButton.visibility =
            if (rol == "YONETICI") View.VISIBLE else View.GONE
        if (!gelenUrunId.isNullOrEmpty()) {
            db.collection("Urunler").document(gelenUrunId).get().addOnSuccessListener { doc ->
                if (isAdded && _binding != null && doc.exists()) {
                    mevcutUrun = doc.toObject(urun::class.java)?.apply { urunId = doc.id }
                    mevcutUrun?.let { urun ->
                        binding.urunDetayToolbar.title = urun.urunAdi ?: "Ürün Detayı"
                        binding.miktarTextView.text = "Stok: ${urun.miktar ?: "0"}"
                        binding.aciklamaTextView.text = "Açıklama: ${urun.ekNot ?: "Bilgi yok"}"
                        binding.konumTextView.text = urun.konum ?: "Konum belirtilmemiş"
                        resimYukle(urun.gorselUrl)
                    }
                }
            }
        }
        binding.duzenleSilPaylasImageButton.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.detay_menuler, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_paylas -> {
                        paylas(mevcutUrun); true
                    }

                    R.id.menu_duzenle -> {
                        mevcutUrun?.let {
                            val bundle = Bundle().apply { putString("urunId", it.urunId) }
                            findNavController().navigate(
                                R.id.action_urunDetayfragment_to_urunEkleFragment,
                                bundle
                            )
                        }
                        true
                    }

                    R.id.menu_sil -> {
                        mevcutUrun?.urunId?.let { id ->
                            db.collection("Urunler").document(id).delete().addOnSuccessListener {
                                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
        binding.askDanAllButton.setOnClickListener {
            val uid = auth.currentUser?.uid ?: return@setOnClickListener Toast.makeText(
                context,
                "Giriş yapın!",
                Toast.LENGTH_SHORT
            ).show()


            val miktarStr = binding.miktarTextInput.text.toString()
            val talepEdilenMiktar = miktarStr.toIntOrNull() ?: 0

            if (talepEdilenMiktar <= 0) {
                Toast.makeText(context, "Lütfen geçerli bir miktar girin", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            mevcutUrun?.let { urun ->
                val stok = urun.miktar?.toIntOrNull() ?: 0
                if (talepEdilenMiktar > stok) {
                    Toast.makeText(context, "Stok yetersiz! (Mevcut: $stok)", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (binding.hemenRadioButton.isChecked) {
                    binding.askDanAllButton.isEnabled = false
                    val talep = hashMapOf(
                        "urunId" to urun.urunId,
                        "urunAdi" to urun.urunAdi,
                        "miktar" to talepEdilenMiktar.toString(),
                        "konum" to urun.konum,
                        "gorselUrl" to urun.gorselUrl,
                        "tarih" to Timestamp.now(),
                        "durum" to "Beklemede",
                        "talepTipi" to "Hemen Al",
                        "yukleyenUid" to uid
                    )
                    db.collection("Talepler").add(talep).addOnSuccessListener {
                        Toast.makeText(context, "Talep oluşturuldu", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_urunDetayfragment_to_taleplerFragment)
                    }
                } else {
                    val bundle = Bundle().apply { putString("urunId", urun.urunId) }
                    findNavController().navigate(
                        R.id.action_urunDetayfragment_to_tarihVeZamanFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun resimYukle(gorselData: String?) {
        if (!gorselData.isNullOrEmpty()) {
            if (gorselData.startsWith("http")) {
                Glide.with(this).load(gorselData).error(R.drawable.ic_launcher_background)
                    .into(binding.detayImageView)
            } else {
                try {
                    val bytes = Base64.decode(gorselData, Base64.DEFAULT)
                    binding.detayImageView.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            bytes,
                            0,
                            bytes.size
                        )
                    )
                } catch (e: Exception) {
                    binding.detayImageView.setImageResource(R.drawable.ic_launcher_background)
                }
            }
        }
    }

    private fun paylas(urun: urun?) {
        val msg = "${urun?.urunAdi ?: "Ürün"} - Hemen al: ${urun?.konum ?: ""}"
        val i = Intent().apply {
            action = Intent.ACTION_SEND; putExtra(Intent.EXTRA_TEXT, msg); type = "text/plain"
        }
        startActivity(Intent.createChooser(i, "Paylaş"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}