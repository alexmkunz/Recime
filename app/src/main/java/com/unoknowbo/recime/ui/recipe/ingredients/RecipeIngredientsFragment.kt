package com.unoknowbo.recime.ui.recipe.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentRecipeIngredientsBinding

class RecipeIngredientsFragment : Fragment() {

    // Static method to pass the recipeId into the fragment upon creation
    companion object {
        fun newInstance(recipeId: Long): RecipeIngredientsFragment {
            val frag = RecipeIngredientsFragment()
            val args = Bundle()
            args.putLong("recipeId", recipeId)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Retrieve the recipeId from the arguments bundle
        val args = this.arguments
        val recipeId = requireNotNull(args?.getLong("recipeId"))

        // Get a reference to the binding object and inflate the fragment views
        val binding: FragmentRecipeIngredientsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe_ingredients, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the recipesViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = RecimeDatabase.getInstance(application).ingredientDao
        val viewModelFactory = RecipeIngredientsViewModelFactory(dataSource, recipeId)
        val recipeIngredientsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(
                RecipeIngredientsViewModel::class.java
            )
        binding.recipeIngredientsViewModel = recipeIngredientsViewModel

        // Set the adapter of the RecyclerView
        val adapter = RecipeIngredientsAdapter()
        binding.recipeIngredientsRecyclerView.adapter = adapter

        // When changes have been made to the recipes database, submit the updated list of recipes
        recipeIngredientsViewModel.ingredients.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
