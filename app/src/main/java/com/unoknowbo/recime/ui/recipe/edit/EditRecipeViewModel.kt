package com.unoknowbo.recime.ui.recipe.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.ingredient.Ingredient
import com.unoknowbo.recime.database.ingredient.IngredientDao
import com.unoknowbo.recime.database.instruction.Instruction
import com.unoknowbo.recime.database.instruction.InstructionDao
import com.unoknowbo.recime.database.recipe.Recipe
import com.unoknowbo.recime.database.recipe.RecipeDao
import kotlinx.coroutines.*

class EditRecipeViewModel (
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val instructionDao: InstructionDao,
    var recipeId: Long
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
                val prepTimeEstimateInMinutes =
                    if (prepTimeHoursString == "" && prepTimeMinutesString == "") -1 else
                        (if (prepTimeHoursString == "") 0 else prepTimeHoursString.toInt() * 60) +
                                (if (prepTimeMinutesString == "") 0 else prepTimeMinutesString.toInt())
                val cookTimeEstimateInMinutes =
                    if (cookTimeHoursString == "" && cookTimeMinutesString == "") -1 else
                        (if (cookTimeHoursString == "") 0 else cookTimeHoursString.toInt() * 60) +
                                (if (cookTimeMinutesString == "") 0 else cookTimeMinutesString.toInt())
                val servings = if (servingsString == "") -1 else servingsString.toInt()
                val calories = if (caloriesString == "") -1 else caloriesString.toInt()

                if (recipeId == (-1).toLong()){
                    recipeId = recipeDao.insert(
                        Recipe(
                            nameString,
                            cookTimeEstimateInMinutes,
                            prepTimeEstimateInMinutes,
                            descriptionString,
                            servings,
                            calories
                        )
                    )
                } else {
                    recipeDao.updateName(nameString, recipeId)
                    recipeDao.updatePrepTime(prepTimeEstimateInMinutes, recipeId)
                    recipeDao.updateCookTime(cookTimeEstimateInMinutes, recipeId)
                    recipeDao.updateServings(servings, recipeId)
                    recipeDao.updateCalories(calories, recipeId)
                    recipeDao.updateDescription(descriptionString, recipeId)
                }

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
            _navigateBackToRecipe.value = true
        }
    }

    private fun getIngredientsFromString(ingredientsString: String): List<Ingredient> {
        val ingredientEntries = ingredientsString
            .split("\n").map {s: String -> s.trim()}.filterNot {s: String -> s == ""}
        val ingredients = mutableListOf<Ingredient>()
        for ((order, entry) in ingredientEntries.withIndex()) {
            ingredients.add(Ingredient(recipeId, entry, order))
        }
        return ingredients
    }

    private fun getInstructionsFromString(instructionsString: String): List<Instruction> {
        val instructionEntries = instructionsString
            .split("\n").map {s: String -> s.trim()}.filterNot {s: String -> s == ""}
        val instructions = mutableListOf<Instruction>()
        for ((order, entry) in instructionEntries.withIndex()) {
            instructions.add(Instruction(recipeId, entry, order)
            )
        }
        return instructions
    }
}