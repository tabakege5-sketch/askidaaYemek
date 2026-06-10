package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class yonetenListeFragmentDirections private constructor() {
  private data class ActionYonetenListeFragmentToYoneticiQrKodFragment(
    public val qrVerisi: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_yonetenListeFragment_to_yoneticiQrKodFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("qr_verisi", this.qrVerisi)
        return result
      }
  }

  public companion object {
    public fun actionYonetenListeFragmentToTaleplerFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yonetenListeFragment_to_taleplerFragment)

    public fun actionYonetenListeFragmentToYoneticiQrKodFragment(qrVerisi: String? = null):
        NavDirections = ActionYonetenListeFragmentToYoneticiQrKodFragment(qrVerisi)
  }
}
