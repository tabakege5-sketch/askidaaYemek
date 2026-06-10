package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class taleplerFragmentDirections private constructor() {
  private data class ActionTaleplerFragmentToYoneticiQrKodFragment(
    public val qrVerisi: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_taleplerFragment_to_yoneticiQrKodFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("qr_verisi", this.qrVerisi)
        return result
      }
  }

  public companion object {
    public fun actionTaleplerFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_urunAnaSayfa)

    public fun actionTaleplerFragmentToMusteriQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_musteriQrKodFragment)

    public fun actionTaleplerFragmentToYoneticiQrKodFragment(qrVerisi: String? = null):
        NavDirections = ActionTaleplerFragmentToYoneticiQrKodFragment(qrVerisi)

    public fun actionTaleplerFragmentToYonetenListeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_yonetenListeFragment)
  }
}
