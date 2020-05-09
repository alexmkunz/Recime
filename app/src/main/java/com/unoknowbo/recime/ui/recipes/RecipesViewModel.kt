package com.unoknowbo.recime.ui.recipes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.recipe.RecipeDao

class RecipesViewModel (val database: RecipeDao) : ViewModel() {
    val recipes = database.getRecipes()

    /** Navigating to RecipeFragment */

    // The ID to the recipe that we are navigating to
    private val _navigateToRecipe = MutableLiveData<Long>()
    val navigateToRecipe
        get() = _navigateToRecipe

    // Set navigateToRecipe to the clicked recipe id when it is clicked on
    fun onRecipeClicked(id: Long){
        _navigateToRecipe.value = id
    }

    // Signify that navigation is complete
    fun onRecipeNavigated() {
        _navigateToRecipe.value = null
    }


    /** Navigating to EditRecipeFragment to create a new recipe */

    // The boolean that tracks navigation to the create recipe page
    private val _navigateToCreateRecipe = MutableLiveData<Boolean>()
    val navigateToCreateRecipe
        get() = _navigateToCreateRecipe

    // Set navigateToCreateRecipe to the true to launch create recipe page
    fun onCreateRecipeClicked(){
        _navigateToCreateRecipe.value = true
    }

    // Signify that navigation to the create recipe page is complete
    fun onCreateRecipeNavigated() {
        _navigateToCreateRecipe.value = null
    }
}
