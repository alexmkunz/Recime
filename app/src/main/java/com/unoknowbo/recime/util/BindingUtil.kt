package com.unoknowbo.recime.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
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