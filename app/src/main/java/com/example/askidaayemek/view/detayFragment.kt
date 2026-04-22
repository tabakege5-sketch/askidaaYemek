package com.example.askidaayemek.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.askidaayemek.adapter.kategoriAdapter
import com.example.askidaayemek.dataClass.kategori
import com.example.askidaayemek.databinding.FragmentDetayBinding

class detayFragment : Fragment() {
    private var _binding: FragmentDetayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kategoriler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val kategoriListesi = arrayListOf(
            kategori("Ana yemekler"),
            kategori("Çorbalar"),
            kategori("Tatlılar"),
            kategori("Hamur İşleri"),
            kategori("Ekmekler"),
            kategori("Sıcak -Soğuk İçecekler")
        )
        val adapter = kategoriAdapter(kategoriListesi) { secilen ->
        }
        binding.kategoriler.adapter = adapter
        binding.aramaTextEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            //Önce değiştirildi
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val aranan = s.toString().uppercase()
                val filtrelenmis = kategoriListesi.filter {
                    it.isim.uppercase().contains(aranan)
                }
                adapter.listeyiGuncelle(filtrelenmis)
            }
            override fun afterTextChanged(s: Editable?) {}
            //Sonrasında Değiştirili
        })

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.kategoriler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}