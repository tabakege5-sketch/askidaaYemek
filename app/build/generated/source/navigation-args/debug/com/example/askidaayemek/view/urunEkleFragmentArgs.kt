package com.example.askidaayemek.view

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import kotlin.String
import kotlin.jvm.JvmStatic

public data class urunEkleFragmentArgs(
  public val urunId: String? = null,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("urunId", this.urunId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("urunId", this.urunId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): urunEkleFragmentArgs {
      bundle.setClassLoader(urunEkleFragmentArgs::class.java.classLoader)
      val __urunId : String?
      if (bundle.containsKey("urunId")) {
        __urunId = bundle.getString("urunId")
      } else {
        __urunId = null
      }
      return urunEkleFragmentArgs(__urunId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): urunEkleFragmentArgs {
      val __urunId : String?
      if (savedStateHandle.contains("urunId")) {
        __urunId = savedStateHandle["urunId"]
      } else {
        __urunId = null
      }
      return urunEkleFragmentArgs(__urunId)
    }
  }
}
