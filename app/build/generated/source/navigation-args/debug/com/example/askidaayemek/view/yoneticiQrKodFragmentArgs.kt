package com.example.askidaayemek.view

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import kotlin.String
import kotlin.jvm.JvmStatic

public data class yoneticiQrKodFragmentArgs(
  public val qrVerisi: String? = null,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("qr_verisi", this.qrVerisi)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("qr_verisi", this.qrVerisi)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): yoneticiQrKodFragmentArgs {
      bundle.setClassLoader(yoneticiQrKodFragmentArgs::class.java.classLoader)
      val __qrVerisi : String?
      if (bundle.containsKey("qr_verisi")) {
        __qrVerisi = bundle.getString("qr_verisi")
      } else {
        __qrVerisi = null
      }
      return yoneticiQrKodFragmentArgs(__qrVerisi)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): yoneticiQrKodFragmentArgs {
      val __qrVerisi : String?
      if (savedStateHandle.contains("qr_verisi")) {
        __qrVerisi = savedStateHandle["qr_verisi"]
      } else {
        __qrVerisi = null
      }
      return yoneticiQrKodFragmentArgs(__qrVerisi)
    }
  }
}
