package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class profilFragmentDirections private constructor() {
  public companion object {
    public fun actionProfilFragmentToGirisLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_profilFragment_to_girisLoginFragment)
  }
}
