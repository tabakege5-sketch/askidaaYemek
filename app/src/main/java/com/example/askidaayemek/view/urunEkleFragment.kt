package com.example.askidaayemek.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import android.util.Base64
import java.io.ByteArrayOutputStream

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val views = listOf(
                binding.urunlerEditText,
                binding.urunAdiEditText,
                binding.miktarEditText,
                binding.ekNotEditText
            )
            views.forEach { it.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO }
        }

        binding.urunEkleToolbar.post {
            val params = binding.urunEkleToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 100
            binding.urunEkleToolbar.layoutParams = params
        }

        @Suppress("DEPRECATION")
        guncellenecekUrun = arguments?.getParcelable("duzenlenecekUrun")
        registerLaunchers()
        setupListeners()
        haritadanGelenKonumuDinle()

        if (guncellenecekUrun != null) {
            setupEditMode(guncellenecekUrun!!)
        } else {
            setupAddMode()
        }
    }

    private fun setupListeners() {
        binding.urunEkleToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.urunlerEditText.setOnClickListener { popupGoster() }
        binding.secImageButton.setOnClickListener { popupGoster() }

        binding.mevcutKonumTextView.setOnClickListener {
            if (binding.mevcutKonumTextView.isEnabled) {
                val bundle = Bundle().apply {
                    putBoolean("isYonetici", true)
                }
                findNavController().navigate(
                    R.id.action_urunEkleFragment_to_haritaFragment,
                    bundle
                )
            }
        }

        binding.urunFotografiImageView.setOnClickListener { gorselSec() }

        binding.vazgecButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.yayNlaButton.setOnClickListener {
            if (guncellenecekUrun != null) {
                guncellemeIslemi()
            } else {
                yayinlaIslemi()
            }
        }
    }

    private fun haritadanGelenKonumuDinle() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("secilenKonum")
            ?.observe(viewLifecycleOwner) { konumMetni ->
                if (!konumMetni.isNullOrEmpty()) {
                    binding.mevcutKonumTextView.text = konumMetni
                }
            }
    }

    private fun setupEditMode(urun: urun) {
        binding.kilitKatmani.visibility = View.GONE

        binding.textUrunEkle.visibility = View.GONE
        binding.secImageButton.visibility = View.GONE

        binding.urunEkleToolbar.title = "Ürünü Güncelle"
        binding.yayNlaButton.text = "GÜNCELLE"

        binding.urunlerEditText.setText(urun.urunKategori ?: "")
        binding.urunAdiEditText.setText(urun.urunAdi ?: "")
        binding.miktarEditText.setText(urun.miktar ?: "")
        binding.ekNotEditText.setText(urun.ekNot ?: "")
        binding.mevcutKonumTextView.text = urun.konum ?: "Konum Belirtilmedi"

        resimYukle(urun.gorselUrl)
    }

    private fun setupAddMode() {
        binding.kilitKatmani.visibility = View.GONE
        binding.urunEkleToolbar.title = "Ürün Ekle"
        binding.yayNlaButton.text = "YAYINLA"
    }

    private fun popupGoster() {
        val popup = PopupMenu(requireContext(), binding.urunlerEditText)
        popup.menuInflater.inflate(R.menu.urunler_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            binding.urunlerEditText.setText(item.title.toString())
            hideKeyboard()
            true
        }
        popup.show()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    secilenGorselUri = result.data?.data
                    binding.urunFotografiImageView.setImageURI(secilenGorselUri)
                    binding.textUrunEkle.visibility = View.GONE
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) gorselSec()
                else Toast.makeText(requireContext(), "Galeri izni gerekli", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun guncellemeIslemi() {
        val urunId = guncellenecekUrun?.urunId
        if (urunId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Ürün ID bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        (activity as? MainActivity)?.gosterLoading(true)

        val guncelMap = hashMapOf<String, Any>(
            "urunKategori" to binding.urunlerEditText.text.toString().trim(),
            "urunAdi" to binding.urunAdiEditText.text.toString().trim(),
            "miktar" to binding.miktarEditText.text.toString().trim(),
            "ekNot" to binding.ekNotEditText.text.toString().trim(),
            "konum" to binding.mevcutKonumTextView.text.toString().trim()
        )

        secilenGorselUri?.let { uri ->
            uriToBase64(uri)?.let { base64 ->
                guncelMap["gorselUrl"] = base64
            }
        }

        db.collection("Urunler").document(urunId)
            .update(guncelMap)
            .addOnSuccessListener {
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(context, "Başarıyla Güncellendi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                (activity as? MainActivity)?.gosterLoading(false)
                Toast.makeText(context, "Güncelleme başarısız: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun yayinlaIslemi() {
        val user = auth.currentUser ?: run {
            Toast.makeText(requireContext(), "Giriş yapmanız gerekiyor", Toast.LENGTH_SHORT).show()
            return
        }

        val kategori = binding.urunlerEditText.text.toString().trim()
        val ad = binding.urunAdiEditText.text.toString().trim()
        val miktar = binding.miktarEditText.text.toString().trim()
        val not = binding.ekNotEditText.text.toString().trim()
        val konum = binding.mevcutKonumTextView.text.toString().trim()

        if (kategori.isEmpty() || ad.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Lütfen kategori ve ürün adını doldurun",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        binding.yayNlaButton.isEnabled = false
        (activity as? MainActivity)?.gosterLoading(true)

        val gorsel = secilenGorselUri?.let { uriToBase64(it) } ?: ""

        veritabaninaKaydet(
            kategori = kategori,
            ad = ad,
            not = not,
            konum = konum,
            miktar = miktar,
            uid = user.uid,
            gorsel = gorsel
        )
    }

    private fun veritabaninaKaydet(
        kategori: String,
        ad: String,
        not: String,
        konum: String,
        miktar: String,
        uid: String,
        gorsel: String
    ) {
        val urunMap = hashMapOf(
            "urunKategori" to kategori,
            "urunAdi" to ad,
            "miktar" to (miktar.ifEmpty { "Belirtilmedi" }),
            "ekNot" to not,
            "konum" to konum,
            "yukleyenUid" to uid,
            "tarih" to Timestamp.now(),
            "durum" to "Askıda",
            "gorselUrl" to gorsel
        )

        db.collection("Urunler").add(urunMap)
            .addOnSuccessListener {
                (activity as? MainActivity)?.gosterLoading(false)
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                (activity as? MainActivity)?.gosterLoading(false)
                binding.yayNlaButton.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    "Kayıt başarısız: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun resimYukle(data: String?) {
        if (!data.isNullOrEmpty()) {
            if (data.startsWith("http")) {
                Glide.with(this).load(data).into(binding.urunFotografiImageView)
            } else {
                try {
                    val bytes = Base64.decode(data, Base64.DEFAULT)
                    binding.urunFotografiImageView.setImageBitmap(
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    )
                } catch (e: Exception) {
                    binding.urunFotografiImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val output = ByteArrayOutputStream()
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 40, output)
            Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    private fun gorselSec() {
        val perm =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                perm
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(perm)
        } else {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            activityResultLauncher.launch(intent)
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}