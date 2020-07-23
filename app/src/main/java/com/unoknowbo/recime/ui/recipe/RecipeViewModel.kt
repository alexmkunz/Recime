package com.unoknowbo.recime.ui.recipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.recipe.RecipeDao
import kotlinx.coroutines.*

class RecipeViewModel (val database: RecipeDao, val recipeId: Long) : ViewModel() {
    /** Coroutine setup from Udacity's "Developing Android Apps with Kotlin" */

    // viewModelJob allows us to cancel all coroutines started by this ViewModel.
    private val viewModelJob = Job()

    // A CoroutineScope keeps track of all coroutines started by this ViewModel.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Cancels all coroutines when the ViewModel is cleared.
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /** Navigation handling after delete */

    // The ID to the recipe that we are navigating to
    private val _navigateBackToRecipes = MutableLiveData<Boolean>()
    val navigateBackToRecipes
        get() = _navigateBackToRecipes


    // Signify that navigation is complete
    fun doneDeleting() {
        _navigateBackToRecipes.value = null
    }


    /** Delete recipe with the given recipeId */

    fun delete() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteRecipe(recipeId)
            }
            _navigateBackToRecipes.value = true
        }
    }
}