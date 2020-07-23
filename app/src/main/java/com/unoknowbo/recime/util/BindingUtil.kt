package com.unoknowbo.recime.util

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
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
            ((if (isValid(recipe.cookTimeEstimateInMinutes)) recipe.cookTimeEstimateInMinutes
            else 0) +
                    (if (isValid(recipe.prepTimeEstimateInMinutes)) recipe.prepTimeEstimateInMinutes
                    else 0)),
            context.resources
        )
    }
}

@BindingAdapter("formatSpecificsString")
fun TextView.formatSpecificsString(recipe: Recipe?) {
    recipe?.let {
        visibility = getSpecificVisibility(recipe, Specific.TopLeft)
    }
}

@BindingAdapter("formatSpecificsIcon")
fun ImageView.formatSpecificsIcon(recipe: Recipe?) {
    recipe?.let {
        visibility = getSpecificVisibility(recipe, Specific.TopLeft)
    }
}

@BindingAdapter("formatTopLeftSpecificString")
fun TextView.formatTopLeftSpecificString(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificString(recipe, Specific.TopLeft, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.TopLeft)
    }
}

@BindingAdapter("formatTopRightSpecificString")
fun TextView.formatTopRightSpecificString(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificString(recipe, Specific.TopRight, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.TopRight)
    }
}

@BindingAdapter("formatBottomLeftSpecificString")
fun TextView.formatBottomLeftSpecificString(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificString(recipe, Specific.BottomLeft, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.BottomLeft)
    }
}

@BindingAdapter("formatBottomRightSpecificString")
fun TextView.formatBottomRightSpecificString(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificString(recipe, Specific.BottomRight, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.BottomRight)
    }
}

@BindingAdapter("formatTopLeftSpecificValue")
fun TextView.formatTopLeftSpecificValue(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificValue(recipe, Specific.TopLeft, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.TopLeft)
    }
}

@BindingAdapter("formatTopRightSpecificValue")
fun TextView.formatTopRightSpecificValue(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificValue(recipe, Specific.TopRight, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.TopRight)

    }
}

@BindingAdapter("formatBottomLeftSpecificValue")
fun TextView.formatBottomLeftSpecificValue(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificValue(recipe, Specific.BottomLeft, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.BottomLeft)
    }
}

@BindingAdapter("formatBottomRightSpecificValue")
fun TextView.formatBottomRightSpecificValue(recipe: Recipe?) {
    recipe?.let {
        text = getSpecificValue(recipe, Specific.BottomRight, context.resources)
        visibility = getSpecificVisibility(recipe, Specific.BottomRight)
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
        text = if (isValid(recipe.servings)) recipe.servings.toString() else ""
    }
}

@BindingAdapter("formatCalories")
fun TextView.formatCalories(recipe: Recipe?) {
    recipe?.let {
        text = if (isValid(recipe.calories)) recipe.calories.toString() else ""
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

@BindingAdapter("addOrRemovePrepTime")
fun ImageView.addOrRemovePrepTime(recipe: Recipe?) {
    addOrRemove(this, recipe?.prepTimeEstimateInMinutes?: -1)
}

@BindingAdapter("addOrRemoveCookTime")
fun ImageView.addOrRemoveCookTime(recipe: Recipe?) {
    addOrRemove(this, recipe?.cookTimeEstimateInMinutes?: -1)
}

@BindingAdapter("addOrRemoveServings")
fun ImageView.addOrRemoveServings(recipe: Recipe?) {
    addOrRemove(this, recipe?.servings ?: -1)
}

@BindingAdapter("addOrRemoveCalories")
fun ImageView.addOrRemoveCalories(recipe: Recipe?) {
    addOrRemove(this, recipe?.calories ?: -1)
}


private var lastRecipe: Recipe? = null
private var specificsDisplayValues = mutableListOf<Pair<String, String>>()

private enum class Specific(val value: Int) {
    TopLeft(1), TopRight(2), BottomLeft(3), BottomRight(4)
}

private fun Boolean.toInt() = if (this) 1 else 0

// Return if the recipe should display the specific.
private fun hasSpecific(recipe: Recipe, specific: Specific): Boolean {
    return (
            isValid(recipe.prepTimeEstimateInMinutes).toInt() +
                    isValid(recipe.cookTimeEstimateInMinutes).toInt() +
                    isValid(recipe.servings).toInt() +
                    isValid(recipe.calories).toInt() >= specific.value
            )
}

private fun setSpecificsDisplayValues(recipe: Recipe, res: Resources):
        MutableList<Pair<String, String>>{
    val list = mutableListOf<Pair<String, String>>()
    if (isValid(recipe.prepTimeEstimateInMinutes)) {
        list.add(
            Pair(
                res.getString(R.string.prep_time_colon),
                formatTime(recipe.prepTimeEstimateInMinutes, res)
            )
        )
    }
    if (isValid(recipe.cookTimeEstimateInMinutes)) {
        list.add(
            Pair(
                res.getString(R.string.cook_time_colon),
                formatTime(recipe.cookTimeEstimateInMinutes, res)
            )
        )
    }
    if (isValid(recipe.servings)) {
        list.add(
            Pair(
                res.getString(R.string.servings_colon),
                recipe.servings.toString()
            )
        )
    }
    if (isValid(recipe.calories)) {
        list.add(
            Pair(
                res.getString(R.string.calories_colon),
                recipe.calories.toString()
            )
        )
    }

    // Make cook time bottom left (third in the list) if there is a prep time and a bottom left
    if(
        isValid(recipe.cookTimeEstimateInMinutes) &&
        isValid(recipe.prepTimeEstimateInMinutes) &&
        hasSpecific(recipe, Specific.BottomLeft)
    ) {
        val indexOfCookTime = list.indexOf(
            Pair(
                res.getString(R.string.cook_time_colon),
                formatTime(recipe.cookTimeEstimateInMinutes, res)
            )
        )
        list[2] = list[indexOfCookTime].also { list[indexOfCookTime] = list[2] }
    }

    return list
}

private fun getSpecificStringOrValue(
    recipe: Recipe,
    specific: Specific,
    res: Resources,
    isString: Boolean
): String {
    if (recipe != lastRecipe) {
        lastRecipe = recipe
        specificsDisplayValues = setSpecificsDisplayValues(recipe, res)
    }
    return if (hasSpecific(recipe, specific) && (specificsDisplayValues.size >= specific.value)) {
        if (isString) {
            specificsDisplayValues[specific.value - 1].first
        } else {
            specificsDisplayValues[specific.value - 1].second
        }
    } else {
        ""
    }
}

private fun getSpecificString(recipe: Recipe, specific: Specific, res: Resources): String {
    return getSpecificStringOrValue(recipe, specific, res, true)
}

private fun getSpecificValue(recipe: Recipe, specific: Specific, res: Resources): String {
    return getSpecificStringOrValue(recipe, specific, res, false)
}

private fun getSpecificVisibility(recipe: Recipe, specific: Specific): Int {
    return if (hasSpecific(recipe, specific)) View.VISIBLE else View.GONE
}

private fun addOrRemove(imageView: ImageView, value: Int) {
    imageView.tag = if (value == -1) {
        imageView.setImageResource(R.drawable.ic_add)
        R.drawable.ic_add
    } else {
        imageView.setImageResource(R.drawable.ic_remove)
        R.drawable.ic_remove
    }
}

private fun formatTime(minutes: Int, res: Resources): String {
    return when {
        minutes < 0 -> ""
        minutes == 0 -> res.getString(R.string.na)
        else -> {
            var minutesString = ""
            val remainderMinutes = minutes % 60
            val displayMinutes = remainderMinutes > 0
            if (displayMinutes) {
                minutesString = "$remainderMinutes" + res.getString(R.string.minutes_abbreviation)
            }
            if (minutes > 59) {
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
    }
}

private fun getHoursStringFromTime(minutes: Int): String {
    return if (minutes >= 0) "${minutes / 60}" else ""
}

private fun getMinutesStringFromTime(minutes: Int): String {
    return if (minutes >= 0) "${minutes % 60}" else ""
}

private fun formatIngredientsString(ingredients: List<Ingredient>): String {
    var ingredientsString = ""
    for (ingredient in ingredients) {
        ingredientsString += "${ingredient.description}\n\n"
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

private fun isValid(value: Int): Boolean {
    return value > -1
}