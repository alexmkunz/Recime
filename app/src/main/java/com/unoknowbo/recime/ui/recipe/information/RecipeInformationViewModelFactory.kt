/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.ui.recipe.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * Provides the RecipeDao and recipeId to the ViewModel.
 */
class RecipeInformationViewModelFactory(private val dataSource: RecipeDao,
                                        private val recipeId: Long)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeInformationViewModel::class.java)) {
            return RecipeInformationViewModel(dataSource, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

