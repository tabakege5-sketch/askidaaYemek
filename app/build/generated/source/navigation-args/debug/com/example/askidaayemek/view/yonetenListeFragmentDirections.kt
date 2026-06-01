package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class yonetenListeFragmentDirections private constructor() {
  public companion object {
    public fun actionYonetenListeFragmentToTaleplerFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yonetenListeFragment_to_taleplerFragment)

    public fun actionYonetenListeFragmentToYoneticiQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yonetenListeFragment_to_yoneticiQrKodFragment)
  }
}
