package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class yoneticiKayitOlDirections private constructor() {
  public companion object {
    public fun actionYoneticiKayitOlToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yoneticiKayitOl_to_urunAnaSayfa)

    public fun actionYoneticiKayitOlToGirisLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_yoneticiKayitOl_to_girisLoginFragment)
  }
}
