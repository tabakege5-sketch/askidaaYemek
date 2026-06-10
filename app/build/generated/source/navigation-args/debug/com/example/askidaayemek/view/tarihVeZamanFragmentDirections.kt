package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class tarihVeZamanFragmentDirections private constructor() {
  public companion object {
    public fun actionTarihVeZamanFragmentToTaleplerFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_tarihVeZamanFragment_to_taleplerFragment)

    public fun actionTarihVeZamanFragmentToRezervasyonFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_tarihVeZamanFragment_to_rezervasyonFragment)
  }
}
