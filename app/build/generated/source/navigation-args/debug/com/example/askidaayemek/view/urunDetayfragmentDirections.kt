package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class urunDetayfragmentDirections private constructor() {
  private data class ActionUrunDetayfragmentToTarihVeZamanFragment(
    public val urunId: String?,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunDetayfragment_to_tarihVeZamanFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  private data class ActionUrunDetayfragmentToUrunEkleFragment(
    public val urunId: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunDetayfragment_to_urunEkleFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  public companion object {
    public fun actionUrunDetayfragmentToTarihVeZamanFragment(urunId: String?): NavDirections =
        ActionUrunDetayfragmentToTarihVeZamanFragment(urunId)

    public fun actionUrunDetayfragmentToTaleplerFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunDetayfragment_to_taleplerFragment)

    public fun actionUrunDetayfragmentToYoneticiQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunDetayfragment_to_yoneticiQrKodFragment)

    public fun actionUrunDetayfragmentToMusteriQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunDetayfragment_to_musteriQrKodFragment)

    public fun actionUrunDetayfragmentToUrunEkleFragment(urunId: String? = null): NavDirections =
        ActionUrunDetayfragmentToUrunEkleFragment(urunId)

    public fun actionUrunDetayfragmentToHaritaFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunDetayfragment_to_haritaFragment)
  }
}
