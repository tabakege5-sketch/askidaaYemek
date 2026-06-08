package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class yoneticiQrKodFragmentDirections private constructor() {
  public companion object {
    public fun actionYoneticiQrKodFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yoneticiQrKodFragment_to_urunAnaSayfa)
  }
}
