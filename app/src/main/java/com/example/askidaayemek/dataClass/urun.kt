package com.example.askidaayemek.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class urun(
    val urunAdi: String? = null,
    val miktar: String? = null,
    val konum: String? = null,
    val gorselUrl: String? = null,
    val tarih: @RawValue Any? = null,
    val saat: String? = null,
    val urunKategori: String? = null,
    val durum: String? = null,
    val yukleyenUid: String? = null,
    val ekNot: String? = null
) : Parcelable