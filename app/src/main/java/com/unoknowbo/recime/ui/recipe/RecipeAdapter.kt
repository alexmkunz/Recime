package com.unoknowbo.recime.ui.recipe

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.unoknowbo.recime.ui.recipe.information.RecipeInformationFragment
import com.unoknowbo.recime.ui.recipe.ingredients.RecipeIngredientsFragment
import com.unoknowbo.recime.ui.recipe.instructions.RecipeInstructionsFragment

class RecipeAdapter(fragment: Fragment, private val recipeId: Long) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecipeInformationFragment.newInstance(recipeId)
            1 -> RecipeIngredientsFragment.newInstance(recipeId)
            2 -> RecipeInstructionsFragment.newInstance(recipeId)
            else -> error("Recipe fragment position >2")
        }
    }
}