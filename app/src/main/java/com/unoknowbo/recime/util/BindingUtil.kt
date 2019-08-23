package com.unoknowbo.recime.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.unoknowbo.recime.database.recipe.Recipe

@BindingAdapter("formatTime")
fun TextView.formatTime(recipe: Recipe?) {
    recipe?.let {
        text = formatTime(
            recipe.timeEstimateInMinutes,
            context.resources
        )
    }
}