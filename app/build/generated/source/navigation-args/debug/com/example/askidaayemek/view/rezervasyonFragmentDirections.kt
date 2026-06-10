package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class rezervasyonFragmentDirections private constructor() {
  public companion object {
    public fun actionRezervasyonFragmentToMusteriQrKodFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_rezervasyonFragment_to_musteriQrKodFragment)
  }
}
