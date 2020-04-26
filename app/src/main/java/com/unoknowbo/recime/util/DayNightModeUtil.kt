package com.unoknowbo.recime.util

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val isDay = "isDay"

// Change from day/night mode to night/day when the isDay preference changes.
var dayNightPrefListener =
    SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
        when (key) {
            isDay -> {
                setDayNightMode(sharedPrefs)
            }
        }
    }

// Set the app to day mode if isDay is true, or night mode if isDay is false.
fun setDayNightMode(sharedPrefs: SharedPreferences?) {
    if (sharedPrefs != null) {
        AppCompatDelegate.setDefaultNightMode(
            if (sharedPrefs.getBoolean(isDay, true)) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
}

// Toggle the isDay variable from true -> false or false -> true
fun toggleIsDaySharedPref(sharedPrefs: SharedPreferences?) {
    if (sharedPrefs != null) {
        with (sharedPrefs.edit()) {
            putBoolean(isDay, !sharedPrefs.getBoolean(isDay, true))
            apply()
        }
    }
}
