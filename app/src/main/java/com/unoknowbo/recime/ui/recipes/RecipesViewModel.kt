package com.unoknowbo.recime.ui.recipes

import android.database.Cursor
import android.database.MergeCursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.SearchDao
import com.unoknowbo.recime.database.recipe.RecipeDao
import kotlinx.coroutines.*

class RecipesViewModel (recipeDao: RecipeDao, private val searchDao: SearchDao)
    : ViewModel() {
    val recipes = recipeDao.getRecipes()
    var searchSuggestions: Cursor? = null

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

    /** Handle Searching */

    // The ID to the recipe that we are navigating to
    private val _doneRetrievingSearchSuggestions = MutableLiveData<Boolean>()
    val doneRetrievingSearchSuggestions
        get() = _doneRetrievingSearchSuggestions


    // Signify that navigation is complete
    fun doneSearching() {
        _doneRetrievingSearchSuggestions.value = null
    }

    fun search(searchInput: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val recipeSS = searchDao.getRecipeSearchSuggestions(searchInput)
                val ingredientSS = searchDao.getIngredientSearchSuggestions(searchInput)
                val instructionSS = searchDao.getInstructionSearchSuggestions(searchInput)
                searchSuggestions = MergeCursor(arrayOf(recipeSS, ingredientSS, instructionSS))
            }
            _doneRetrievingSearchSuggestions.value = true
        }
    }

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
