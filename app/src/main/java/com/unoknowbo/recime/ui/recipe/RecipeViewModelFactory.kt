/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * Provides the RecipeDao and recipeId to the ViewModel.
 */
class RecipeViewModelFactory(private val dataSource: RecipeDao,
                                        private val recipeId: Long)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(dataSource, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
