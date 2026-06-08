package com.example.askidaayemek.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Parcelize
data class urun(
    var urunId: String? = null,
    var urunAdi: String? = null,
    var miktar: String? = null,
    var konum: String? = null,
    var gorselUrl: String? = null,
    var tarih: @RawValue Any? = null,
    var saat: String? = null,
    var urunKategori: String? = null,
    var durum: String? = null,
    var yukleyenUid: String? = null,
    var ekNot: String? = null
) : Serializable, Parcelable {

    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)
}