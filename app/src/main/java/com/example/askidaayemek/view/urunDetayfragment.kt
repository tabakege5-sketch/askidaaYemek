package com.example.askidaayemek.view

import android.app.AlertDialog
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

        binding.urunDetayToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.konumTextView.setOnClickListener {
            mevcutUrun?.konum?.let { konumString ->
                val bundle = Bundle().apply {
                    putString("hedefKonum", konumString)
                }
                findNavController().navigate(
                    R.id.action_urunDetayfragment_to_haritaFragment,
                    bundle
                )
            }
        }

        val sharedPref =
            requireActivity().getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
        val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")

        binding.duzenleSilPaylasImageButton.visibility =
            if (rol == "YONETICI") View.VISIBLE else View.GONE

        val gelenUrunId = arguments?.getString("urunId")

        if (!gelenUrunId.isNullOrEmpty()) {
            (activity as? MainActivity)?.gosterLoading(true)
            db.collection("Urunler").document(gelenUrunId).get()
                .addOnSuccessListener { doc ->
                    (activity as? MainActivity)?.gosterLoading(false)

                    if (isAdded && _binding != null && doc.exists()) {
                        mevcutUrun = doc.toObject(urun::class.java)?.apply {
                            urunId = doc.id
                        }

                        mevcutUrun?.let { urun ->
                            binding.urunDetayToolbar.title = urun.urunAdi ?: "Ürün Detayı"
                            binding.miktarTextView.text = "Stok: ${urun.miktar ?: "0"}"
                            binding.aciklamaTextView.text = "Açıklama: ${urun.ekNot ?: "Bilgi yok"}"
                            binding.konumTextView.text = urun.konum ?: "Konum: Belirtilmemiş"
                            resimYukle(urun.gorselUrl)
                        }
                    }
                }
                .addOnFailureListener {
                    (activity as? MainActivity)?.gosterLoading(false)
                    Toast.makeText(context, "Ürün yüklenemedi", Toast.LENGTH_SHORT).show()
                }
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

                    R.id.menu_duzenle -> {
                        mevcutUrun?.let { urun ->
                            val bundle = Bundle().apply {
                                putParcelable("duzenlenecekUrun", urun)
                            }
                            findNavController().navigate(
                                R.id.action_urunDetayfragment_to_urunEkleFragment,
                                bundle
                            )
                        }
                        true
                    }

                    R.id.menu_sil -> {
                        silmeOnayIletisimiGoster()
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }

        binding.askDanAllButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(context, "Önce giriş yapmalısın!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val talepEdilenMiktar = binding.miktarTextInput.text.toString().toIntOrNull() ?: 0
            if (talepEdilenMiktar <= 0) {
                Toast.makeText(context, "Geçerli bir miktar girin", Toast.LENGTH_SHORT).show()
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
                    (activity as? MainActivity)?.gosterLoading(true)

                    val talep = hashMapOf(
                        "asilIlanId" to urun.urunId,
                        "urunAdi" to urun.urunAdi,
                        "miktar" to talepEdilenMiktar.toString(),
                        "konum" to urun.konum,
                        "gorselUrl" to urun.gorselUrl,
                        "tarih" to Timestamp.now(),
                        "durum" to "Beklemede",
                        "talepTipi" to "Hemen Al",
                        "musteriUid" to currentUser.uid,
                        "yukleyenUid" to (urun.yukleyenUid ?: "")
                    )

                    db.collection("Talepler").add(talep)
                        .addOnSuccessListener { docReference ->
                            (activity as? MainActivity)?.gosterLoading(false)
                            Toast.makeText(
                                requireContext(),
                                "Talep oluşturuldu QR kod açılıyor",
                                Toast.LENGTH_SHORT
                            ).show()
                            val bundle = Bundle().apply {
                                putString(
                                    "urunId",
                                    docReference.id
                                )
                                putString("urunAdi", urun.urunAdi)
                            }
                            findNavController().navigate(
                                R.id.action_urunDetayfragment_to_musteriQrKodFragment,
                                bundle
                            )
                        }
                        .addOnFailureListener {
                            (activity as? MainActivity)?.gosterLoading(false)
                            Toast.makeText(context, "Talep oluşturulamadı", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    val bundle = Bundle().apply {
                        putString("urunId", urun.urunId)
                    }
                    findNavController().navigate(
                        R.id.action_urunDetayfragment_to_tarihVeZamanFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun silmeOnayIletisimiGoster() {
        val urunAdi = mevcutUrun?.urunAdi ?: "Bu ürünü"
        AlertDialog.Builder(requireContext())
            .setTitle("Ürünü Sil")
            .setMessage("$urunAdi silmek istediğinize emin misiniz?")
            .setPositiveButton("Sil") { _, _ ->
                mevcutUrun?.urunId?.let { id ->
                    (activity as? MainActivity)?.gosterLoading(true)
                    db.collection("Urunler").document(id).delete()
                        .addOnSuccessListener {
                            (activity as? MainActivity)?.gosterLoading(false)
                            Toast.makeText(context, "Ürün silindi", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        .addOnFailureListener {
                            (activity as? MainActivity)?.gosterLoading(false)
                            Toast.makeText(context, "Ürün silinemedi", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Vazgeç", null)
            .create()
            .show()
    }

    private fun resimYukle(gorselData: String?) {
        if (!gorselData.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(gorselData, Base64.DEFAULT)
                binding.detayImageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                )
            } catch (e: Exception) {
                binding.detayImageView.setImageResource(android.R.drawable.stat_notify_error)
            }
        }
    }

    private fun paylas(urun: urun?) {
        val msg = "${urun?.urunAdi ?: "Ürün"} - Hemen al: ${urun?.konum ?: ""}"
        val i = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, msg)
        }
        startActivity(Intent.createChooser(i, "Paylaşım Yapın"))
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}