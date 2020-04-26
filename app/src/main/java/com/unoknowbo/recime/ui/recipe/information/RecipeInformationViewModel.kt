package com.unoknowbo.recime.ui.recipe.information

import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.recipe.RecipeDao

class RecipeInformationViewModel (val database: RecipeDao, val recipeId: Long) : ViewModel() {
    val recipe = database.getRecipe(recipeId)
}