package com.unoknowbo.recime.ui.recipes

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentRecipesBinding
import com.unoknowbo.recime.util.toggleIsDaySharedPref

class RecipesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // This fragment has an options menu
        setHasOptionsMenu(true)

        // Get a reference to the binding object and inflate the fragment views
        val binding: FragmentRecipesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipes, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the recipesViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = RecimeDatabase.getInstance(application).recipeDao
        val viewModelFactory = RecipesViewModelFactory(dataSource)
        val recipesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel::class.java)
        binding.recipesViewModel = recipesViewModel

        // Set the adapter of the RecyclerView
        val adapter = RecipesAdapter(
            RecipeListener {
                recipeId -> recipesViewModel.onRecipeClicked(recipeId)
            }
        )
        binding.recipesRecyclerView.adapter = adapter

        recipesViewModel.navigateToRecipe.observe(viewLifecycleOwner, Observer {recipeId ->
            recipeId?.let {
                this.findNavController().navigate(RecipesFragmentDirections
                    .actionRecipesFragmentToRecipeFragment(recipeId))
                recipesViewModel.onRecipeNavigated()
            }
        })

        // When changes have been made to the recipes database, submit the updated list of recipes
        recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_recipes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Toggle the isDay preference when this menu item is selected.
            R.id.day_night_mode_toggle -> {
                val sharedPrefs = this.activity?.getPreferences(Context.MODE_PRIVATE)
                toggleIsDaySharedPref(sharedPrefs)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}