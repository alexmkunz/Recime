package com.unoknowbo.recime.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.unoknowbo.recime.database.recipe.Recipe

@BindingAdapter("formattedRecipeTimes")
fun TextView.formattedRecipeTimes(recipe: Recipe?) {
    recipe?.let {
        text = formatRecipeTimes(
            recipe.cookTimeInMinutes,
            recipe.prepTimeInMinutes,
            context.resources
        )
    }
}