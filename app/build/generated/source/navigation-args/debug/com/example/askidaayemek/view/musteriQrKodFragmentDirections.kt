package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class musteriQrKodFragmentDirections private constructor() {
  public companion object {
    public fun actionMusteriQrKodFragmentToUrunAnaSayfa(): NavDirections =
        ActionOnlyNavDirections(R.id.action_musteriQrKodFragment_to_urunAnaSayfa)
  }
}
