package com.example.askidaayemek.view

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.askidaayemek.R
import kotlin.Int
import kotlin.String

public class haritaFragmentDirections private constructor() {
  private data class ActionHaritaFragmentToUrunEkleFragment(
    public val urunId: String? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_haritaFragment_to_urunEkleFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("urunId", this.urunId)
        return result
      }
  }

  public companion object {
    public fun actionHaritaFragmentToUrunEkleFragment(urunId: String? = null): NavDirections =
        ActionHaritaFragmentToUrunEkleFragment(urunId)
  }
}
