package com.example.askidaayemek.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.askidaayemek.adapter.talepAdapter
import com.example.askidaayemek.dataClass.urun
import com.example.askidaayemek.databinding.FragmentTaleplerBinding

class taleplerFragment : Fragment() {

    private var _binding: FragmentTaleplerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: talepAdapter
    private var talepListesi = ArrayList<urun>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaleplerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.geriButtons.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.taleplerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = talepAdapter(talepListesi)
        binding.taleplerRecyclerView.adapter = adapter
        verileriGetir()
    }

    private fun verileriGetir() {
        talepListesi.clear()
        talepListesi.add(urun("", "", "", null, "", ""))
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}