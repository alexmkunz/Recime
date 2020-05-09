/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
package com.unoknowbo.recime.ui.recipe.instructions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.instruction.InstructionDao

/**
 * Provides the InstructionDao and recipeId to the ViewModel.
 */
class RecipeInstructionsViewModelFactory(private val dataSource: InstructionDao,
                                        private val recipeId: Long)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeInstructionsViewModel::class.java)) {
            return RecipeInstructionsViewModel(dataSource, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}