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
    private var mevcutMenu: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentKonteynrView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.butonNavigasyon)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val sharedPref = getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
            val rol = sharedPref.getString("kullanici_rolu", "MUSTERI")
            val hedefMenu = if (rol == "YONETICI") R.menu.yonetici_menuler else R.menu.musteri_menu
            val gizle = destination.id == R.id.girisLoginFragment ||
                    destination.id == R.id.kayitOlFragment ||
                    destination.id == R.id.musteriQrKodFragment ||
                    destination.id == R.id.parolaFragment ||
                    destination.id == R.id.yoneticiQrKodFragment

            bottomNav.visibility = if (gizle) View.GONE else View.VISIBLE
            if (!gizle && mevcutMenu != hedefMenu) {
                bottomNav.menu.clear()
                bottomNav.inflateMenu(hedefMenu)
                mevcutMenu = hedefMenu
            }
        }
    }
}