package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class girisLoginFragmentDirections private constructor() {
  public companion object {
    public fun actionGirisLoginFragmentToKayitOlFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_girisLoginFragment_to_kayitOlFragment)

    public fun actionGirisLoginFragmentToParolaFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_girisLoginFragment_to_parolaFragment)

    public fun actionGirisLoginFragmentToYoneticiKayitOl(): NavDirections =
        ActionOnlyNavDirections(R.id.action_girisLoginFragment_to_yoneticiKayitOl)

    public fun actionGirisLoginFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_girisLoginFragment_to_urunAnaSayfa)
  }
}
