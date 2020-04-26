package com.unoknowbo.recime.ui.recipe.instructions

import androidx.lifecycle.ViewModel
import com.unoknowbo.recime.database.instruction.InstructionDao

class RecipeInstructionsViewModel(val database: InstructionDao, val recipeId: Long) : ViewModel() {
    val instructions = database.getRecipeInstructions(recipeId)
}