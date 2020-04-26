package com.unoknowbo.recime.ui.recipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.recipe.RecipeDao

class RecipesViewModel (val database: RecipeDao) : ViewModel() {
    val recipes = database.getRecipes()

    /**
     * The ID to the recipe that we are navigating to
     */
    private val _navigateToRecipe = MutableLiveData<Long>()
    val navigateToRecipe
        get() = _navigateToRecipe

    /**
     * Set navigateToRecipe to the clicked recipe id when it is clicked on
     */
    fun onRecipeClicked(id: Long){
        _navigateToRecipe.value = id
    }

    /**
     * Signify that navigation is complete
     */
    fun onRecipeNavigated() {
        _navigateToRecipe.value = null
    }
}
