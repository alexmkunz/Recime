/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.ui.recipe.ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.ingredient.IngredientDao

/**
 * Provides the IngredientDao and recipeId to the ViewModel.
 */
class RecipeIngredientsViewModelFactory(private val dataSource: IngredientDao,
                                        private val recipeId: Long)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeIngredientsViewModel::class.java)) {
            return RecipeIngredientsViewModel(dataSource, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}