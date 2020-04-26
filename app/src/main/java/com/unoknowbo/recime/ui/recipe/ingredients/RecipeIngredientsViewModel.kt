package com.unoknowbo.recime.ui.recipe.ingredients

import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.ingredient.IngredientDao

class RecipeIngredientsViewModel(val database: IngredientDao, val recipeId: Long) : ViewModel() {
    val ingredients = database.getRecipeIngredients(recipeId)
}