package com.unoknowbo.recime.ui.edit
/**
 * Based on the implementation provided in Udacity's "Developing Android Apps with Kotlin"
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unoknowbo.recime.database.ingredient.IngredientDao
import com.unoknowbo.recime.database.instruction.InstructionDao
import com.unoknowbo.recime.database.recipe.RecipeDao

/**
 * Provides the RecipeDao and recipeId to the ViewModel.
 */
class EditRecipeViewModelFactory(private val recipeDao: RecipeDao,
                                 private val ingredientDao: IngredientDao,
                                 private val instructionDao: InstructionDao,
                                 private val recipeId: Long)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditRecipeViewModel::class.java)) {
            return EditRecipeViewModel(
                recipeDao,
                ingredientDao,
                instructionDao,
                recipeId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}