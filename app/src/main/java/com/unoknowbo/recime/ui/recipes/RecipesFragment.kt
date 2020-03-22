package com.unoknowbo.recime.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentRecipesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipes, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the recipesViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = RecimeDatabase.getInstance(application).recipeDao
        val viewModelFactory = RecipesViewModelFactory(dataSource, application)
        val recipesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel::class.java)
        binding.recipesViewModel = recipesViewModel

        // Set the adapter of the RecyclerView
        val adapter = RecipesAdapter(
            // TODO: Launch recipe info page
            RecipeListener { recipeId ->
                Toast.makeText(activity?.baseContext, "$recipeId", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recipesRecyclerView.adapter = adapter

        // When changes have been made to the recipes database, submit the updated list of recipes
        recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}