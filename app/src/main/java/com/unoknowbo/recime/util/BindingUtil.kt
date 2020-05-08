package com.unoknowbo.recime.util

import android.content.res.Resources
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.ingredient.Ingredient
import com.unoknowbo.recime.database.instruction.Instruction
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

@BindingAdapter("formatPrepHours")
fun TextView.formatPrepHours(recipe: Recipe?) {
    recipe?.let {
        text = getHoursStringFromTime(recipe.prepTimeEstimateInMinutes)
    }
}

@BindingAdapter("formatPrepMinutes")
fun TextView.formatPrepMinutes(recipe: Recipe?) {
    recipe?.let {
        text = getMinutesStringFromTime(recipe.prepTimeEstimateInMinutes)
    }
}

@BindingAdapter("formatCookHours")
fun TextView.formatCookHours(recipe: Recipe?) {
    recipe?.let {
        text = getHoursStringFromTime(recipe.cookTimeEstimateInMinutes)
    }
}

@BindingAdapter("formatCookMinutes")
fun TextView.formatCookMinutes(recipe: Recipe?) {
    recipe?.let {
        text = getMinutesStringFromTime(recipe.cookTimeEstimateInMinutes)
    }
}

@BindingAdapter("formatServings")
fun TextView.formatServings(recipe: Recipe?) {
    recipe?.let {
        text = if (recipe.servings <= 0) "" else recipe.servings.toString()
    }
}

@BindingAdapter("formatCalories")
fun TextView.formatCalories(recipe: Recipe?) {
    recipe?.let {
        text = if (recipe.calories <= 0) "" else recipe.calories.toString()
    }
}

@BindingAdapter("formatIngredients")
fun TextView.formatIngredients(ingredients: List<Ingredient>?) {
    ingredients?.let {
        text = formatIngredientsString(ingredients)
    }
}

@BindingAdapter("formatInstructions")
fun TextView.formatInstructions(instructions: List<Instruction>?) {
    instructions?.let {
        text = formatInstructionsString(instructions)
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

private fun getHoursStringFromTime(minutes: Int): String {
    return if (minutes > 0) "${minutes / 60}" else ""
}

private fun getMinutesStringFromTime(minutes: Int): String {
    return if (minutes > 0) "${minutes % 60}" else ""
}

private fun formatIngredientsString(ingredients: List<Ingredient>): String {
    var ingredientsString = ""
    for (ingredient in ingredients) {
        ingredientsString += "${ingredient.quantity}, ${ingredient.unitOfMeasurement}, ${ingredient.description}\n\n"
    }
    return ingredientsString
}

private fun formatInstructionsString(instructions: List<Instruction>): String {
    var instructionsString = ""
    for (instruction in instructions) {
        instructionsString += "${instruction.description}\n\n"
    }
    return instructionsString
}