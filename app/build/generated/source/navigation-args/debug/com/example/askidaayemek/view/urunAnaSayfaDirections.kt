package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class urunAnaSayfaDirections private constructor() {
  private data class ActionUrunAnaSayfaToUrunDetayfragment(
    public val urunId: String?,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunAnaSayfa_to_urunDetayfragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  private data class ActionUrunAnaSayfaToYoneticiQrKodFragment(
    public val qrVerisi: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunAnaSayfa_to_yoneticiQrKodFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("qr_verisi", this.qrVerisi)
        return result
      }
  }

  public companion object {
    public fun actionUrunAnaSayfaToUrunDetayfragment(urunId: String?): NavDirections =
        ActionUrunAnaSayfaToUrunDetayfragment(urunId)

    public fun actionUrunAnaSayfaToTaleplerFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunAnaSayfa_to_taleplerFragment)

    public fun actionUrunAnaSayfaToUrunPaylasanFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunAnaSayfa_to_urunPaylasanFragment)

    public fun actionUrunAnaSayfaToMusteriQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunAnaSayfa_to_musteriQrKodFragment)

    public fun actionUrunAnaSayfaToYoneticiQrKodFragment(qrVerisi: String? = null): NavDirections =
        ActionUrunAnaSayfaToYoneticiQrKodFragment(qrVerisi)

    public fun actionUrunAnaSayfaToQrFragment2(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunAnaSayfa_to_qrFragment2)
  }
}
