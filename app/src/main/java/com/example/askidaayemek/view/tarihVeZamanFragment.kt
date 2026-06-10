package com.example.askidaayemek.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.askidaayemek.R
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class tarihVeZamanFragment : Fragment() {

    private var _binding: FragmentTarihVeZamanBinding? = null
    private val binding get() = _binding!!
    private var secilenTarih = ""
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var mevcutUrun: urun? = null
    private var talepEdilenMiktar = 1
    private val BILDIRIM_KANAL_ID = "rezervasyon_kanali"

    private val izinIsteyici = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { izinVerildi ->
        if (izinVerildi) {
            bildirimGoster("Askıda Yemek", "Rezervasyonunuz oluşturuldu")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTarihVeZamanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bildirimKanaliOlustur()
        binding.tamSaatTimePicker.setIs24HourView(true)

        val urunId = arguments?.getString("urunId")
        val miktarStr = arguments?.getString("girilenMiktar") ?: "1"
        talepEdilenMiktar = miktarStr.toIntOrNull() ?: 1

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd.M.yyyy", Locale.getDefault())
        secilenTarih = sdf.format(calendar.time)

        if (!urunId.isNullOrEmpty()) {
            (activity as? MainActivity)?.gosterLoading(true)
            db.collection("Urunler").document(urunId).get().addOnSuccessListener { doc ->
                (activity as? MainActivity)?.gosterLoading(false)
                if (doc.exists()) {
                    mevcutUrun = doc.toObject(urun::class.java)?.apply { this.urunId = doc.id }
                }
            }.addOnFailureListener {
                (activity as? MainActivity)?.gosterLoading(false)
            }
        }

        binding.tarihZamanToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.takvimView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            secilenTarih = "$dayOfMonth.${month + 1}.$year"
        }

        binding.secimiKaydetButton.setOnClickListener {
            val currentMusteriUid = auth.currentUser?.uid
            if (currentMusteriUid == null) {
                Toast.makeText(context, "Giriş yapın", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mevcutUrun == null) {
                Toast.makeText(context, "Ürün yükleniyor bekleyin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ekBilgi = binding.bilgiEditText.text.toString().trim()
            if (ekBilgi.isEmpty()) {
                Toast.makeText(context, "Açıklama giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hour = binding.tamSaatTimePicker.hour
            val minute = binding.tamSaatTimePicker.minute
            val secilenSaat = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

            val gecerlilikTakvimi = Calendar.getInstance()
            try {
                val dateParts = secilenTarih.split(".")
                gecerlilikTakvimi.set(Calendar.YEAR, dateParts[2].toInt())
                gecerlilikTakvimi.set(Calendar.MONTH, dateParts[1].toInt() - 1)
                gecerlilikTakvimi.set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
                gecerlilikTakvimi.set(Calendar.HOUR_OF_DAY, hour)
                gecerlilikTakvimi.set(Calendar.MINUTE, minute)
                gecerlilikTakvimi.set(Calendar.SECOND, 0)
                gecerlilikTakvimi.add(Calendar.MINUTE, 30)
            } catch (e: Exception) {
                gecerlilikTakvimi.time = Calendar.getInstance().time
                gecerlilikTakvimi.add(Calendar.MINUTE, 30)
            }
            val gecerlilikTimestamp = Timestamp(gecerlilikTakvimi.time)

            (activity as? MainActivity)?.gosterLoading(true)
            binding.secimiKaydetButton.isEnabled = false
            val urunRef = db.collection("Urunler").document(mevcutUrun!!.urunId!!)

            urunRef.get().addOnSuccessListener { urunDoc ->
                if (urunDoc.exists()) {
                    val guncelStokStr = urunDoc.get("miktar")?.toString() ?: "0"
                    val guncelStok = guncelStokStr.toIntOrNull() ?: 0

                    if (guncelStok < talepEdilenMiktar) {
                        (activity as? MainActivity)?.gosterLoading(false)
                        binding.secimiKaydetButton.isEnabled = true
                        Toast.makeText(
                            context,
                            " Ürünümüzün Stoğu kalmamıştır Stok: $guncelStok",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnSuccessListener
                    }

                    val yeniTalepMap = hashMapOf(
                        "asilIlanId" to (mevcutUrun!!.urunId ?: ""),
                        "urunId" to (mevcutUrun!!.urunId ?: ""),
                        "urunAdi" to (mevcutUrun!!.urunAdi ?: ""),
                        "yukleyenUid" to (mevcutUrun!!.yukleyenUid ?: ""),
                        "musteriUid" to currentMusteriUid,
                        "miktar" to talepEdilenMiktar.toString(),
                        "gorselUrl" to (mevcutUrun!!.gorselUrl ?: ""),
                        "konum" to (mevcutUrun!!.konum ?: ""),
                        "saat" to secilenSaat,
                        "secilenTarih" to secilenTarih,
                        "acıklama" to ekBilgi,
                        "tarih" to Timestamp.now(),
                        "gecerlilikTarihi" to gecerlilikTimestamp,
                        "durum" to "Beklemede",
                        "onaylayacakAdminUid" to ""
                    )

                    val batch = db.batch()
                    val yeniTalepRef = db.collection("Talepler").document()
                    batch.set(yeniTalepRef, yeniTalepMap)

                    val kalanStok = guncelStok - talepEdilenMiktar
                    if (kalanStok <= 0) {
                        batch.delete(urunRef)
                    } else {
                        batch.update(urunRef, "miktar", kalanStok.toString())
                    }

                    batch.commit().addOnSuccessListener {
                        (activity as? MainActivity)?.gosterLoading(false)
                        Toast.makeText(
                            context,
                            "Rezervasyon başarıyla oluşturuldu",
                            Toast.LENGTH_LONG
                        ).show()
                        bildirimIzinKontrolEtVeGoster(
                            "Rezervasyon Alındı!",
                            "${mevcutUrun!!.urunAdi} için saat ${secilenSaat} rezervasyonunuz hazır."
                        )

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.tarihVeZamanFragment, true)
                            .build()
                        findNavController().navigate(R.id.urunAnaSayfa, null, navOptions)
                    }.addOnFailureListener { e ->
                        (activity as? MainActivity)?.gosterLoading(false)
                        binding.secimiKaydetButton.isEnabled = true
                        Toast.makeText(context, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }.addOnFailureListener {
                (activity as? MainActivity)?.gosterLoading(false)
                binding.secimiKaydetButton.isEnabled = true
            }
        }
    }

    private fun bildirimKanaliOlustur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rezervasyon Bildirimleri"
            val descriptionText = "Rezervasyon durumları hakkında bilgilendirme yapar"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(BILDIRIM_KANAL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun bildirimIzinKontrolEtVeGoster(baslik: String, mesaj: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bildirimGoster(baslik, mesaj)
            } else {
                izinIsteyici.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            bildirimGoster(baslik, mesaj)
        }
    }

    private fun bildirimGoster(baslik: String, mesaj: String) {
        val builder = NotificationCompat.Builder(requireContext(), BILDIRIM_KANAL_ID)
            .setSmallIcon(R.drawable.outline_circle_notifications_24)
            .setContentTitle(baslik)
            .setContentText(mesaj)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.gosterLoading(false)
        super.onDestroyView()
        _binding = null
    }
}