package com.unoknowbo.recime

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unoknowbo.recime.util.dayNightPrefListener
import com.unoknowbo.recime.util.setDayNightMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the day/night listener and set the day/night mode to what was previously saved.
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs?.registerOnSharedPreferenceChangeListener(dayNightPrefListener)
        setDayNightMode(sharedPrefs)

        setContentView(R.layout.activity_main)
    }
}
