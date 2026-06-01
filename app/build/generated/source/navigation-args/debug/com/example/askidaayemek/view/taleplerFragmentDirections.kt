package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class taleplerFragmentDirections private constructor() {
  public companion object {
    public fun actionTaleplerFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_urunAnaSayfa)

    public fun actionTaleplerFragmentToMusteriQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_musteriQrKodFragment)

    public fun actionTaleplerFragmentToYoneticiQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_taleplerFragment_to_yoneticiQrKodFragment)
  }
}
