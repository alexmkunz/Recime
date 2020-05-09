package com.unoknowbo.recime

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unoknowbo.recime.ui.recipe.edit.EditRecipeFragment
import com.unoknowbo.recime.util.dayNightPrefListener
import com.unoknowbo.recime.util.setDayNightMode

interface MyOnBackPressed {
    fun onBackPressed()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        // Register the day/night listener and set the day/night mode to what was previously saved.
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs?.registerOnSharedPreferenceChangeListener(dayNightPrefListener)
        setDayNightMode(sharedPrefs)

        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val navHostFragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val fragments = navHostFragment?.childFragmentManager?.fragments
        for (f in fragments ?: listOf()) {
            if (f is EditRecipeFragment) {
                return (f as MyOnBackPressed).onBackPressed()
            }
        }
        super.onBackPressed()
    }
}
