/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.recipes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * Provides the RecipeDao and context to the ViewModel.
 */
class RecipesViewModelFactory(private val dataSource: RecipeDao, private val application: Application)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            return RecipesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}