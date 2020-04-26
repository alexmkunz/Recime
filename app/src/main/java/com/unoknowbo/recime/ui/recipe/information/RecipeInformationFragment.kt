package com.unoknowbo.recime.ui.recipe.information

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
import com.unoknowbo.recime.databinding.FragmentRecipeInformationBinding

class RecipeInformationFragment : Fragment() {

    // Static method to pass the recipeId into the fragment upon creation
    companion object {
        fun newInstance(recipeId: Long): RecipeInformationFragment {
            val frag = RecipeInformationFragment()
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
        val binding: FragmentRecipeInformationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe_information, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the recipeInformationViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = RecimeDatabase.getInstance(application).recipeDao
        val viewModelFactory = RecipeInformationViewModelFactory(dataSource, recipeId)
        val recipeInformationViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(
                RecipeInformationViewModel::class.java
            )

        recipeInformationViewModel.recipe.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.recipe = it
            }
        })

        return binding.root
    }

}