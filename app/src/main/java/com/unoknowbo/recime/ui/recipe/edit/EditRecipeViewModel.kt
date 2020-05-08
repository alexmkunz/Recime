package com.unoknowbo.recime.ui.recipe.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.ingredient.Ingredient
import com.unoknowbo.recime.database.ingredient.IngredientDao
import com.unoknowbo.recime.database.instruction.Instruction
import com.unoknowbo.recime.database.instruction.InstructionDao
import com.unoknowbo.recime.database.recipe.RecipeDao
import kotlinx.coroutines.*

class EditRecipeViewModel (
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val instructionDao: InstructionDao,
    val recipeId: Long
) : ViewModel() {

    /** Data binding values */
    val recipe = recipeDao.getRecipe(recipeId)
    val ingredients = ingredientDao.getRecipeIngredients(recipeId)
    val instructions = instructionDao.getRecipeInstructions(recipeId)


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


    /** Navigation handling after save or cancel */

    // The ID to the recipe that we are navigating to
    private val _navigateBackToRecipe = MutableLiveData<Boolean>()
    val navigateBackToRecipe
        get() = _navigateBackToRecipe


    // Signify that navigation is complete
    fun doneNavigating() {
        _navigateBackToRecipe.value = null
    }


    /** Save changes */

    fun save(
        nameString: String,
        prepTimeHoursString: String,
        prepTimeMinutesString: String,
        cookTimeHoursString: String,
        cookTimeMinutesString: String,
        servingsString: String,
        caloriesString: String,
        descriptionString: String,
        ingredientsString: String,
        instructionsString: String
    ) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                recipeDao.updateName(nameString, recipeId)
                recipeDao.updatePrepTime(
                    (if (prepTimeHoursString == "") 0 else prepTimeHoursString.toInt() * 60) +
                            (if (prepTimeMinutesString == "") 0 else prepTimeMinutesString.toInt()),
                    recipeId
                )
                recipeDao.updateCookTime(
                    (if (cookTimeHoursString == "") 0 else cookTimeHoursString.toInt() * 60) +
                            (if (cookTimeMinutesString == "") 0 else cookTimeMinutesString.toInt()),
                    recipeId
                )
                recipeDao.updateServings(
                    (if (servingsString == "") 0 else servingsString.toInt()),
                    recipeId
                )
                recipeDao.updateCalories(
                    (if (caloriesString == "") 0 else caloriesString.toInt()),
                    recipeId
                )
                recipeDao.updateDescription(descriptionString, recipeId)

                val ingredients = getIngredientsFromString(ingredientsString)
                // Remove old ingredients
                ingredientDao.deleteIngredients(recipeId)
                for (ingredient in ingredients) {
                    ingredientDao.insert(ingredient)
                }

                val instructions = getInstructionsFromString(instructionsString)
                // Remove old instructions
                instructionDao.deleteInstructions(recipeId)
                for (instruction in instructions) {
                    instructionDao.insert(instruction)
                }
            }
        }
        _navigateBackToRecipe.value = true
    }

    private fun getIngredientsFromString(ingredientsString: String): List<Ingredient> {
        val ingredientEntries = ingredientsString
            .split("\n").filterNot {s: String -> s == ""}
        val ingredients = mutableListOf<Ingredient>()
        for ((order, entry) in ingredientEntries.withIndex()) {
            val ingredientEntryComponents = entry.split(",").map { s: String -> s.trim() }
            Log.d("pepe", ingredientEntryComponents.toString())
            ingredients.add(
                Ingredient(
                    recipeId,
                    ingredientEntryComponents[2],
                    ingredientEntryComponents[0],
                    ingredientEntryComponents[1],
                    order
                )
            )
        }
        Log.d("pepe", ingredients.toString())
        return ingredients
    }

    private fun getInstructionsFromString(instructionsString: String): List<Instruction> {
        val instructionEntries = instructionsString
            .split("\n").filterNot {s: String -> s == ""}.map { s: String -> s.trim() }
        Log.d("pepe", instructionEntries.toString())
        val instructions = mutableListOf<Instruction>()
        for ((order, entry) in instructionEntries.withIndex()) {
            instructions.add(
                Instruction(
                    recipeId,
                    entry,
                    order
                )
            )
        }
        Log.d("pepe", instructions.toString())
        return instructions
    }
}