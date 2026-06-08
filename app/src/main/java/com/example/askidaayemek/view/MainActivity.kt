package com.example.askidaayemek.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.NavHostFragment
import com.example.askidaayemek.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var mevcutMenu: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentKonteynrView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.butonNavigasyon)
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.urunAnaSayfa -> {
                    if (navController.currentDestination?.id != R.id.urunAnaSayfa) {
                        navController.navigate(R.id.urunAnaSayfa)
                    }
                    true
                }

                R.id.taleplerFragment -> {
                    if (navController.currentDestination?.id != R.id.taleplerFragment) {
                        navController.navigate(R.id.taleplerFragment)
                    }
                    true
                }

                R.id.urunEkleFragment -> {
                    if (navController.currentDestination?.id != R.id.urunPaylasanFragment) {
                        navController.navigate(R.id.urunPaylasanFragment)
                    }
                    true
                }

                R.id.yonetenListeFragment -> {
                    if (navController.currentDestination?.id != R.id.yonetenListeFragment) {
                        navController.navigate(R.id.yonetenListeFragment)
                    }
                    true
                }

                R.id.profilFragment -> {
                    if (navController.currentDestination?.id != R.id.profilFragment) {
                        navController.navigate(R.id.profilFragment)
                    }
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val sharedPref = getSharedPreferences("AskidaYemekPref", Context.MODE_PRIVATE)
            val rol = sharedPref.getString("kullanici_rolu", "MUSTERI") ?: "MUSTERI"
            val hedefMenu = if (rol == "YONETICI") R.menu.yonetici_menuler else R.menu.musteri_menu

            val gizle = destination.id == R.id.girisLoginFragment ||
                    destination.id == R.id.kayitOlFragment ||
                    destination.id == R.id.musteriQrKodFragment ||
                    destination.id == R.id.parolaFragment ||
                    destination.id == R.id.yoneticiQrKodFragment ||
                    destination.id == R.id.yoneticiKayitOl

            bottomNav.visibility = if (gizle) View.GONE else View.VISIBLE

            val constraintSet = ConstraintSet()
            constraintSet.clone(mainLayout)
            if (gizle) {
                constraintSet.connect(
                    R.id.fragmentKonteynrView,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
            } else {
                constraintSet.connect(
                    R.id.fragmentKonteynrView,
                    ConstraintSet.BOTTOM,
                    R.id.butonNavigasyon,
                    ConstraintSet.TOP
                )
            }
            constraintSet.applyTo(mainLayout)

            if (!gizle && mevcutMenu != hedefMenu) {
                bottomNav.menu.clear()
                bottomNav.inflateMenu(hedefMenu)
                mevcutMenu = hedefMenu
            }

            if (!gizle) {
                val menuId = when (destination.id) {
                    R.id.urunAnaSayfa -> R.id.urunAnaSayfa
                    R.id.taleplerFragment -> R.id.taleplerFragment
                    R.id.urunPaylasanFragment -> R.id.urunEkleFragment
                    R.id.yonetenListeFragment -> R.id.yonetenListeFragment
                    R.id.profilFragment -> R.id.profilFragment
                    else -> -1
                }
                if (menuId != -1) bottomNav.menu.findItem(menuId)?.isChecked = true
            }
        }
    }
    fun gosterLoading(goster: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.loadingBar)
        progressBar?.visibility = if (goster) View.VISIBLE else View.GONE
    }
}