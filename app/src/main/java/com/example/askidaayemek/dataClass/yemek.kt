package com.example.askidaayemek.dataClass

data class yemek(
    val yemekIsmi: String,
    val kategoriAdi: String,
    val gorselUrl: String = ""
)