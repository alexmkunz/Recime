package com.unoknowbo.recime.ui.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.unoknowbo.recime.database.recipe.RecipeDao

class RecipesViewModel (val database: RecipeDao, application: Application) : AndroidViewModel(application) {
    val recipes = database.getRecipes()
}
