package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class urunPaylasanFragmentDirections private constructor() {
  private data class ActionUrunPaylasanToUrunDetay(
    public val urunId: String?,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunPaylasan_to_urunDetay

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  private data class ActionUrunPaylasanFragmentToUrunEkleFragment(
    public val urunId: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_urunPaylasanFragment_to_urunEkleFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  public companion object {
    public fun actionUrunPaylasanToUrunDetay(urunId: String?): NavDirections =
        ActionUrunPaylasanToUrunDetay(urunId)

    public fun actionUrunPaylasanFragmentToUrunEkleFragment(urunId: String? = null): NavDirections =
        ActionUrunPaylasanFragmentToUrunEkleFragment(urunId)

    public fun actionUrunPaylasanFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunPaylasanFragment_to_urunAnaSayfa)
  }
}
