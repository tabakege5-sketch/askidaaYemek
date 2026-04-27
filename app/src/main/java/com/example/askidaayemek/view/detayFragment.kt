package com.example.askidaayemek.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.adapter.kategoriAdapter
import com.example.askidaayemek.adapter.yemekAdapter
import com.example.askidaayemek.dataClass.kategori
import com.example.askidaayemek.dataClass.yemek
import com.example.askidaayemek.databinding.FragmentDetayBinding
class detayFragment : Fragment() {
    private var _binding: FragmentDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var tumYemekler: ArrayList<yemek>
    private lateinit var altListeAdapter: yemekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verileriHazirla()

        // 2. Ana Yemek Listesi Setup
        altListeAdapter = yemekAdapter(tumYemekler)
        binding.listeRecyclerWiew.layoutManager = LinearLayoutManager(requireContext())
        binding.listeRecyclerWiew.adapter = altListeAdapter
        val kategoriListesi = arrayListOf(
            kategori("Tümü"),
            kategori("Ana yemekler"),
            kategori("Çorbalar"),
            kategori("Tatlılar"),
            kategori("Hamur İşleri"),
            kategori("Ekmekler"),
            kategori("Sıcak-Soğuk İçecekler")
        )

        val katAdapter = kategoriAdapter(kategoriListesi) { secilenKategori ->
            if (secilenKategori.isim == "Tümü") {
                altListeAdapter.listeyiGuncelle(tumYemekler)
            } else {
                val filtrelenmis = tumYemekler.filter {
                    it.kategoriAdi.equals(secilenKategori.isim, ignoreCase = true)
                }
                altListeAdapter.listeyiGuncelle(ArrayList(filtrelenmis))
            }
        }

        binding.kategoriler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.kategoriler.adapter = katAdapter
        binding.aramaTextEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val aranan = s.toString().lowercase()
                val filtrelenmis = tumYemekler.filter {
                    it.yemekIsmi.lowercase().contains(aranan)
                }
                altListeAdapter.listeyiGuncelle(ArrayList(filtrelenmis))
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun verileriHazirla() {
        tumYemekler = ArrayList()

        val kategoriler = listOf(
            "Ana yemekler",
            "Çorbalar",
            "Tatlılar",
            "Hamur İşleri",
            "Ekmekler",
            "Sıcak-Soğuk İçecekler"
        )
        for (katIsmi in kategoriler) {
            for (i in 1..20) {
                tumYemekler.add(yemek("$katIsmi  $i", katIsmi))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}