/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.SearchDao
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * Provides the RecipeDao to the ViewModel.
 */
class RecipesViewModelFactory(private val recipeDao: RecipeDao, private val searchDao: SearchDao)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            return RecipesViewModel(recipeDao, searchDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}