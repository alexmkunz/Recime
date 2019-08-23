package com.unoknowbo.recime.util

import android.content.res.Resources
import com.unoknowbo.recime.R

fun formatTime(minutes: Int, res: Resources): String {
    var minutesString = ""
    val remainderMinutes = minutes % 60
    val displayMinutes = remainderMinutes > 0
    if (displayMinutes) {
        minutesString = "$remainderMinutes" + res.getString(R.string.minutes_abbreviation)
    }
    return if (minutes > 59) {
        val hoursString = "${minutes / 60}" + res.getString(R.string.hours_abbreviation)
        if (displayMinutes) {
            "$hoursString $minutesString"
        } else {
            hoursString
        }
    } else {
        minutesString
    }
}