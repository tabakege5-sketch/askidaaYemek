package com.example.askidaayemek.view

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class urunDetayfragmentArgs(
  public val urunId: String?,
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
    public fun fromBundle(bundle: Bundle): urunDetayfragmentArgs {
      bundle.setClassLoader(urunDetayfragmentArgs::class.java.classLoader)
      val __urunId : String?
      if (bundle.containsKey("urunId")) {
        __urunId = bundle.getString("urunId")
      } else {
        throw IllegalArgumentException("Required argument \"urunId\" is missing and does not have an android:defaultValue")
      }
      return urunDetayfragmentArgs(__urunId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): urunDetayfragmentArgs {
      val __urunId : String?
      if (savedStateHandle.contains("urunId")) {
        __urunId = savedStateHandle["urunId"]
      } else {
        throw IllegalArgumentException("Required argument \"urunId\" is missing and does not have an android:defaultValue")
      }
      return urunDetayfragmentArgs(__urunId)
    }
  }
}
