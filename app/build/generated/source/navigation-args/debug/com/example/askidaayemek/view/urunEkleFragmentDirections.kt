package com.example.askidaayemek.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.askidaayemek.R

public class urunEkleFragmentDirections private constructor() {
  public companion object {
    public fun actionUrunEkleFragmentToUrunPaylasanFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_urunEkleFragment_to_urunPaylasanFragment)
  }
}
