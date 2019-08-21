package com.unoknowbo.recime.util

import android.content.res.Resources
import com.unoknowbo.recime.R

fun formatRecipeTimes(cookTimeInMinutes: Int, prepTimeInMinutes: Int, res: Resources): String {
    return "${res.getString(R.string.prep_colon)} ${formatTime(
        prepTimeInMinutes,
        res
    )}, " +
            "${res.getString(R.string.cook_colon)} ${formatTime(
                cookTimeInMinutes,
                res
            )}"
}

fun formatTime(minutes: Int, res: Resources): String {
    var hoursString = ""
    if (minutes > 59) {
        val hours = minutes / 60
        hoursString += "$hours " + if (hours > 1) res.getString(R.string.hrs) else res.getString(
            R.string.hr
        )
    }
    val remainderMinutes = minutes % 60
    return if (remainderMinutes > 0) {
        "$hoursString $remainderMinutes " +
                if (remainderMinutes > 1) res.getString(R.string.mins) else res.getString(R.string.min)
    }
    else {
        hoursString
    }
}