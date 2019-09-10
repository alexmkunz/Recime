package com.unoknowbo.recime

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    companion object {
        private const val isDay = "isDay"
    }

    // Change from day/night mode to night/day when the isDay preference changes.
    private var dayNightPrefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
            when (key) {
                isDay -> {
                    setDayNightMode(sharedPrefs)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the day/night listener and set the day/night mode to what was previously saved.
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs.registerOnSharedPreferenceChangeListener(dayNightPrefListener)
        setDayNightMode(sharedPrefs)

        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Toggle the isDay preference when this menu item is selected.
            R.id.day_night_mode_toggle -> {
                val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putBoolean(isDay, !sharedPref.getBoolean(isDay, true))
                    apply()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Set the app to day mode if isDay is true, or night mode if isDay is false.
     */
    private fun setDayNightMode(sharedPrefs: SharedPreferences) {
        AppCompatDelegate.setDefaultNightMode(
            if (sharedPrefs.getBoolean(isDay, true)) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
}
