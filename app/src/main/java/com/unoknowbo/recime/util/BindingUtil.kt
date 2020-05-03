package com.unoknowbo.recime.util

import android.content.res.Resources
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.recipe.Recipe

@BindingAdapter("formatTime")
fun TextView.formatTime(recipe: Recipe?) {
    recipe?.let {
        text = formatTime(
            (recipe.cookTimeEstimateInMinutes + recipe.prepTimeEstimateInMinutes),
            context.resources
        )
    }
}

@BindingAdapter("formatPrepTime")
fun TextView.formatPrepTime(recipe: Recipe?) {
    recipe?.let {
        text = formatTime(
            (recipe.prepTimeEstimateInMinutes),
            context.resources
        )
    }
}

@BindingAdapter("formatCookTime")
fun TextView.formatCookTime(recipe: Recipe?) {
    recipe?.let {
        text = formatTime(
            (recipe.cookTimeEstimateInMinutes),
            context.resources
        )
    }
}


private fun formatTime(minutes: Int, res: Resources): String {
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