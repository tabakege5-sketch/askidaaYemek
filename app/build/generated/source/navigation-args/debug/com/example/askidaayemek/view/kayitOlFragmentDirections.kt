package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class kayitOlFragmentDirections private constructor() {
  public companion object {
    public fun actionKayitOlFragmentToGirisLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_kayitOlFragment_to_girisLoginFragment)
  }
}
