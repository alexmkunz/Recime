package com.unoknowbo.recime.ui.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.unoknowbo.recime.MyOnBackPressed
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentEditRecipeBinding
import com.unoknowbo.recime.ui.recipe.RecipeFragmentArgs

class EditRecipeFragment : Fragment(), MyOnBackPressed {

    var recipeId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeId = RecipeFragmentArgs.fromBundle(arguments!!).recipeId

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
        val viewModelFactory =
            EditRecipeViewModelFactory(
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
        editRecipeViewModel.navigateBackToRecipeAfterSave.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navigateBack(editRecipeViewModel.recipeId)
                editRecipeViewModel.doneNavigating()
            }
        })

        binding.editRecipeCancelButton.setOnClickListener {
            getCancelAlertDialog(recipeId)?.show()
        }

        // Execute view model's save when save button is clicked
        binding.editRecipeSaveButton.setOnClickListener {
            editRecipeViewModel.save(
                binding.editRecipeNameEditText.text.toString(),
                binding.editRecipePrepTimeHoursEditText.text.toString(),
                binding.editRecipePrepTimeMinutesEditText.text.toString(),
                binding.editRecipeCookTimeHoursEditText.text.toString(),
                binding.editRecipeCookTimeMinutesEditText.text.toString(),
                binding.editRecipeServingsEditText.text.toString(),
                binding.editRecipeCaloriesEditText.text.toString(),
                binding.editRecipeDescriptionEditText.text.toString(),
                binding.editRecipeIngredientsEditText.text.toString(),
                binding.editRecipeInstructionsEditText.text.toString()
            )
        }

        // Add click listeners to each add/remove button.
        binding.editRecipePrepTimeAddRemove.setOnClickListener {
            addRemoveOnClickListener(
                binding.editRecipePrepTimeAddRemove,
                listOf(
                    binding.editRecipePrepTimeHoursEditText,
                    binding.editRecipePrepTimeMinutesEditText
                ),
                binding.editRecipeConstraintLayout,
                activity
            )
        }
        binding.editRecipeCookTimeAddRemove.setOnClickListener {
            addRemoveOnClickListener(
                binding.editRecipeCookTimeAddRemove,
                listOf(
                    binding.editRecipeCookTimeHoursEditText,
                    binding.editRecipeCookTimeMinutesEditText
                ),
                binding.editRecipeConstraintLayout,
                activity
            )
        }
        binding.editRecipeServingsAddRemove.setOnClickListener {
            addRemoveOnClickListener(
                binding.editRecipeServingsAddRemove,
                listOf(binding.editRecipeServingsEditText),
                binding.editRecipeConstraintLayout,
                activity
            )
        }
        binding.editRecipeCaloriesAddRemove.setOnClickListener {
            addRemoveOnClickListener(
                binding.editRecipeCaloriesAddRemove,
                listOf(binding.editRecipeCaloriesEditText),
                binding.editRecipeConstraintLayout,
                activity
            )
        }

        // Add on focus change listeners to each specific (i.e. prep time, cook time, servings,
        // calories).
        binding.editRecipePrepTimeMinutesEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipePrepTimeAddRemove,
                listOf(
                    binding.editRecipePrepTimeHoursEditText,
                    binding.editRecipePrepTimeMinutesEditText
                ),
                hasFocus
            )
        }
        binding.editRecipePrepTimeHoursEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipePrepTimeAddRemove,
                listOf(
                    binding.editRecipePrepTimeHoursEditText,
                    binding.editRecipePrepTimeMinutesEditText
                ),
                hasFocus
            )
        }
        binding.editRecipeCookTimeMinutesEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipeCookTimeAddRemove,
                listOf(
                    binding.editRecipeCookTimeHoursEditText,
                    binding.editRecipeCookTimeMinutesEditText
                ),
                hasFocus
            )
        }
        binding.editRecipeCookTimeHoursEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipeCookTimeAddRemove,
                listOf(
                    binding.editRecipeCookTimeHoursEditText,
                    binding.editRecipeCookTimeMinutesEditText
                ),
                hasFocus
            )
        }
        binding.editRecipeServingsEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipeServingsAddRemove,
                listOf(binding.editRecipeServingsEditText),
                hasFocus
            )
        }
        binding.editRecipeCaloriesEditText.setOnFocusChangeListener { _, hasFocus ->
            specificOnFocusChange(
                binding.editRecipeCaloriesAddRemove,
                listOf(binding.editRecipeCaloriesEditText),
                hasFocus
            )
        }

        return binding.root
    }

    override fun onBackPressed() {
        getCancelAlertDialog(recipeId)?.show()
    }

    private fun isAdd(imageView: ImageView): Boolean {
        return imageView.tag == R.drawable.ic_add
    }

    // Handle pushing an add/remove button.
    private fun addRemoveOnClickListener(
        addRemoveImageView: ImageView,
        listOfEditTexts: List<TextInputEditText>,
        layout: ConstraintLayout,
        activity: Activity?
    ) {
        if (isAdd(addRemoveImageView)) {
            addRemoveImageView.setImageResource(R.drawable.ic_remove)
            addRemoveImageView.tag = R.drawable.ic_remove
            showKeyboard(activity, listOfEditTexts.first())

        } else {
            addRemoveImageView.setImageResource(R.drawable.ic_add)
            addRemoveImageView.tag = R.drawable.ic_add
            listOfEditTexts.map { it.setText("") }

            // If the focus is on one of the TextViews that just got cleared by the user clicking
            // the remove button, then hide the keyboard and clear the focus.
            val currentFocusedView = activity?.currentFocus
            currentFocusedView?.let {
                for (editText in listOfEditTexts) {
                    if (it == editText) {
                        layout.requestFocus()
                        hideKeyboard(activity)
                        break
                    }
                }
            }
        }
    }

    // Handle the focus change of an EditText that is associated with an add/remove button.
    private fun specificOnFocusChange(
        addRemoveImageView: ImageView,
        listOfEditTexts: List<TextInputEditText>,
        hasFocus: Boolean

    ) {
        if (isAdd(addRemoveImageView) && hasFocus) {
            addRemoveImageView.setImageResource(R.drawable.ic_remove)
            addRemoveImageView.tag = R.drawable.ic_remove
        } else if (
            !isAdd(addRemoveImageView) &&
            !hasFocus &&
            listOfEditTexts.all { it.text.toString() == "" }
        ) {
            addRemoveImageView.setImageResource(R.drawable.ic_add)
            addRemoveImageView.tag = R.drawable.ic_add
        }
    }

    private fun getCancelAlertDialog(recipeId: Long): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.discard) { _, _ ->
                    navigateBack(recipeId)
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

    // Navigate back to RecipeFragment if a recipe was created or one was updated; navigate back to
    // RecipesFragment if a recipe under creation is discarded. Hide the keyboard if it is visible.
    private fun navigateBack(recipeId: Long) {
        val navController = this.findNavController()
        if (recipeId == (-1).toLong()) {
            navController.popBackStack()
        } else {
            navController.navigate(
                EditRecipeFragmentDirections.actionEditRecipeFragmentToRecipeFragment(
                    recipeId
                )
            )
        }
        hideKeyboard(activity)
    }

    private fun showKeyboard(activity: Activity?, editText: TextInputEditText) {
        activity?.let {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            editText.requestFocus()
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(activity: Activity?) {
        activity?.let {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // Check if a view has focus
            val currentFocusedView = activity.currentFocus
            currentFocusedView?.let {
                inputMethodManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}


