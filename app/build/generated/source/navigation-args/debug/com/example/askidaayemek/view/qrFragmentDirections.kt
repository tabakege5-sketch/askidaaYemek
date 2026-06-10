package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class qrFragmentDirections private constructor() {
  private data class ActionQrFragment2ToYoneticiQrKodFragment(
    public val qrVerisi: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_qrFragment2_to_yoneticiQrKodFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("qr_verisi", this.qrVerisi)
        return result
      }
  }

  public companion object {
    public fun actionQrFragment2ToYoneticiQrKodFragment(qrVerisi: String? = null): NavDirections =
        ActionQrFragment2ToYoneticiQrKodFragment(qrVerisi)
  }
}
