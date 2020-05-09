package com.unoknowbo.recime.ui.recipe.edit

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.unoknowbo.recime.MyOnBackPressed
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentEditRecipeBinding
import com.unoknowbo.recime.ui.recipe.RecipeFragmentArgs
import kotlinx.android.synthetic.main.fragment_edit_recipe.*

class EditRecipeFragment : Fragment(), MyOnBackPressed {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = RecipeFragmentArgs.fromBundle(arguments!!)
        val recipeId = args.recipeId

        // Get a reference to the binding object and inflate the fragment views
        val binding: FragmentEditRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_recipe, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the editRecipeViewModel
        val application = requireNotNull(this.activity).application
        val recipeDao = RecimeDatabase.getInstance(application).recipeDao
        val ingredientDao = RecimeDatabase.getInstance(application).ingredientDao
        val instructionDao = RecimeDatabase.getInstance(application).instructionDao
        val viewModelFactory = EditRecipeViewModelFactory(
            recipeDao,
            ingredientDao,
            instructionDao,
            recipeId
        )
        val editRecipeViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(
                EditRecipeViewModel::class.java
            )

        // Data binding variables: recipe, ingredients, instructions from database
        editRecipeViewModel.recipe.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.recipe = it
            }
        })
        editRecipeViewModel.ingredients.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.ingredients = it
            }
        })
        editRecipeViewModel.instructions.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.instructions = it
            }
        })

        // Navigate back to the recipe fragment on save
        editRecipeViewModel.navigateBackToRecipe.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navControllerPopBackStack()
                editRecipeViewModel.doneNavigating()
            }
        })

        binding.editRecipeCancelButton.setOnClickListener {
            getCancelAlertDialog()?.show()
        }

        // Execute view model's save when save button is clicked
        binding.editRecipeSaveButton.setOnClickListener {
            var canSave = true
            // Ensure the ingredients input adheres to the required format
            val ingredientsRegex = Regex("(([^,\n]*[,]){2}[^,\n]*([\n]+|$))*")
            if (!edit_recipe_ingredients_edit_text.text.toString().matches(ingredientsRegex)) {
                edit_recipe_ingredients_layout.error =
                    resources.getString(R.string.invalid_input_period_space) +
                            resources.getString(R.string.ingredients_input_example)
                canSave = false
                getInvalidIngredientsInputAlertDialog()?.show()
            } else {
                edit_recipe_ingredients_layout.isErrorEnabled = false
            }

            // Ensure the instructions input adheres to the required format
            val instructionsRegex = Regex("(.*([\n]+|$))*")
            if (!edit_recipe_instructions_edit_text.text.toString().matches(instructionsRegex)) {
                edit_recipe_instructions_layout.error =
                    resources.getString(R.string.invalid_input_period_space) +
                            resources.getString(R.string.instructions_input_example)
                canSave = false
                getInvalidInstructionsInputAlertDialog()?.show()
            } else {
                edit_recipe_instructions_layout.isErrorEnabled = false
            }

            if (canSave) {
                editRecipeViewModel.save(
                    edit_recipe_name_edit_text.text.toString(),
                    edit_recipe_prep_time_hours_edit_text.text.toString(),
                    edit_recipe_prep_time_minutes_edit_text.text.toString(),
                    edit_recipe_cook_time_hours_edit_text.text.toString(),
                    edit_recipe_cook_time_minutes_edit_text.text.toString(),
                    edit_recipe_servings_edit_text.text.toString(),
                    edit_recipe_calories_edit_text.text.toString(),
                    edit_recipe_description_edit_text.text.toString(),
                    edit_recipe_ingredients_edit_text.text.toString(),
                    edit_recipe_instructions_edit_text.text.toString()
                )
            }
        }

        return binding.root
    }

    override fun onBackPressed() {
        getCancelAlertDialog()?.show()
    }

    private fun getCancelAlertDialog(): AlertDialog? {
        val args = RecipeFragmentArgs.fromBundle(arguments!!)
        val recipeId = args.recipeId
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.discard) { _, _ ->
                    navControllerPopBackStack()
                }
                setNegativeButton(R.string.cancel, null)
                setTitle(
                    if (recipeId == (-1).toLong()) R.string.discard_recipe
                    else R.string.discard_changes
                )
                setMessage(
                    if (recipeId == (-1).toLong()) R.string.discard_recipe_message
                    else R.string.discard_changes_message
                )
                setIcon(R.drawable.ic_discard)
            }
            builder.create()
        }
    }

    private fun getInvalidIngredientsInputAlertDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok, null)
                setTitle(R.string.invalid_ingredients_input)
                setMessage(R.string.ingredients_input_example)
                setIcon(R.drawable.ic_ingredients_on_surface_high_emphasis)
            }
            builder.create()
        }
    }

    private fun getInvalidInstructionsInputAlertDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok, null)
                setTitle(R.string.invalid_instructions_input)
                setMessage(R.string.instructions_input_example)
                setIcon(R.drawable.ic_instructions_on_surface_high_emphasis)
            }
            builder.create()
        }
    }

    // Make this a function so it can be called within the alert dialog
    private fun navControllerPopBackStack() {
        this.findNavController().popBackStack()
    }
}


