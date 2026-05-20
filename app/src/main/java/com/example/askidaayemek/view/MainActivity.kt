package com.example.askidaayemek.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.askidaayemek.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentKonteynrView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.butonNavigasyon)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val sharedPref = getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
            val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")

            when (destination.id) {
                R.id.girisLoginFragment,
                R.id.kayitOlFragment,
                R.id.musteriQrKodFragment,
                R.id.parolaFragment,
                R.id.yoneticiQrKodFragment -> {
                    bottomNav.visibility = View.GONE
                }

                else -> {
                    bottomNav.visibility = View.VISIBLE
                    bottomNav.menu.clear()
                    if (rol == "YONETICI") {
                        bottomNav.inflateMenu(R.menu.yonetici_menuler)
                    } else {
                        bottomNav.inflateMenu(R.menu.musteri_menu)
                    }
                    bottomNav.setupWithNavController(navController)
                }
            }
        }
    }
}