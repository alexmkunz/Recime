package com.unoknowbo.recime.ui.recipes

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.unoknowbo.recime.MainActivity
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentRecipesBinding
import com.unoknowbo.recime.util.toggleIsDaySharedPref
import kotlinx.android.synthetic.main.list_item_ingredient_search_suggestion.view.*
import kotlinx.android.synthetic.main.list_item_instruction_search_suggestion.view.*
import kotlinx.android.synthetic.main.list_item_recipe_search_suggestion.view.*


class RecipesFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var searchView: SearchView

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
        val recipeDao = RecimeDatabase.getInstance(application).recipeDao
        val searchDao = RecimeDatabase.getInstance(application).searchDao
        val viewModelFactory = RecipesViewModelFactory(recipeDao, searchDao)
        recipesViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipesViewModel::class.java)
        binding.recipesViewModel = recipesViewModel

        // Set the adapter of the RecyclerView
        val adapter = RecipesAdapter(
            RecipeListener {
                recipeId -> recipesViewModel.onRecipeClicked(recipeId)
            }
        )
        binding.recipesRecyclerView.adapter = adapter

        // Navigate to RecipeFragment
        recipesViewModel.navigateToRecipe.observe(viewLifecycleOwner, Observer {recipeId ->
            recipeId?.let {
                this.findNavController().navigate(RecipesFragmentDirections
                    .actionRecipesFragmentToRecipeFragment(recipeId))
                recipesViewModel.onRecipeNavigated()
            }
        })

        // Navigate to EditRecipeFragment to create a recipe
        recipesViewModel.navigateToCreateRecipe.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(RecipesFragmentDirections
                    .actionRecipesFragmentToEditRecipeFragment(-1))
                recipesViewModel.onCreateRecipeNavigated()
            }
        })

        // When changes have been made to the recipes database, submit the updated list of recipes
        recipesViewModel.recipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Update the suggestionAdapter with the new search suggestions
        recipesViewModel.doneRetrievingSearchSuggestions.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if (this::searchView.isInitialized) {
                    searchView.suggestionsAdapter = SearchCursorAdapter(
                        context,
                        recipesViewModel.searchSuggestions,
                        this.findNavController(),
                        recipesViewModel
                    )
                    searchView.suggestionsAdapter?.notifyDataSetChanged()
                }
                recipesViewModel.doneSearching()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_recipes, menu)

        // Set the SearchView
        searchView = SearchView((context as MainActivity)
            .supportActionBar?.themedContext ?: context)
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(searchInput: String): Boolean {
                if (this@RecipesFragment::recipesViewModel.isInitialized) {
                    recipesViewModel.search(searchInput)
                }
                return false
            }
        })
        searchView.setOnQueryTextFocusChangeListener { view, hasFocus ->
            // Show the keyboard when the query text has focus, hide it when it doesn't
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus) {
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            } else {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        // Make the searchView's text box show suggestions after one character is entered
        val autoCompleteTextViewID =
            resources.getIdentifier("android:id/search_src_text", null, null)
        val searchAutoCompleteTextView =
            searchView.findViewById(autoCompleteTextViewID) as AutoCompleteTextView
        searchAutoCompleteTextView.threshold = 0

        // Associate the search icon with the SearchView
        val searchItem = menu.findItem(R.id.fragment_recipes_menu_search)
        searchItem.apply {
            setShowAsAction(
                MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM
            )
            actionView = searchView
        }
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true // Return true to collapse
            }
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                item.actionView.requestFocus()
                return true // Return true to expand
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Toggle the isDay preference when this menu item is selected.
            R.id.fragment_recipes_menu_day_night_mode_toggle -> {
                val sharedPrefs = this.activity?.getPreferences(Context.MODE_PRIVATE)
                toggleIsDaySharedPref(sharedPrefs)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // CursorAdapter to populate the ListView of search suggestions.
    private class SearchCursorAdapter(
        context: Context?,
        cursor: Cursor?,
        val navController: NavController,
        val recipesViewModel: RecipesViewModel
    ) : CursorAdapter(context, cursor, 0) {
        private enum class SuggestionType(val value: Int) {
            Recipe(0), Ingredient(1), Instruction(2)
        }

        override fun getViewTypeCount(): Int {
            return 3
        }

        override fun getItemViewType(position: Int): Int {
            return getSuggestionType(getItem(position) as Cursor) ?: 0
        }

        override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
            val layout = when (getSuggestionType(cursor)) {
                SuggestionType.Ingredient.value -> R.layout.list_item_ingredient_search_suggestion
                SuggestionType.Instruction.value -> R.layout.list_item_instruction_search_suggestion
                else -> R.layout.list_item_recipe_search_suggestion // if null or Recipe
            }
            return LayoutInflater.from(context).inflate(layout, parent, false)
        }

        override fun bindView(view: View, context: Context?, cursor: Cursor) {
            val recipeName = cursor.getString(cursor.getColumnIndexOrThrow("recipeName"))
            val recipeId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
            view.setOnClickListener {
                navController.navigate(RecipesFragmentDirections
                    .actionRecipesFragmentToRecipeFragment(recipeId))
                recipesViewModel.onRecipeNavigated()
            }

            when (getSuggestionType(cursor)) {
                SuggestionType.Ingredient.value -> {
                    val ingredient = cursor.getString(cursor.getColumnIndexOrThrow("ingredient"))
                    val ingredientString =
                        "${context?.getString(R.string.has_ingredient_colon) ?: ""} $ingredient"
                    view.list_item_ingredient_search_suggestion_recipe_name.text = recipeName
                    view.list_item_ingredient_search_suggestion_ingredient.text = ingredientString
                }
                SuggestionType.Instruction.value -> {
                    val instruction = cursor.getString(cursor.getColumnIndexOrThrow("instruction"))
                    val instructionString =
                        "${context?.getString(R.string.has_instruction_colon) ?: ""} $instruction"
                    view.list_item_instruction_search_suggestion_recipe_name.text = recipeName
                    view.list_item_instruction_search_suggestion_instruction.text = instructionString
                }
                else -> view.list_item_recipe_search_suggestion_recipe_name.text = recipeName
            }

        }

        private fun getSuggestionType(cursor: Cursor?): Int? {
            return if (cursor != null) {
                if (cursor.columnCount > 2) {
                    if (cursor.getColumnIndex("ingredient") != -1) {
                        SuggestionType.Ingredient.value
                    } else {
                        SuggestionType.Instruction.value
                    }
                } else {
                    SuggestionType.Recipe.value
                }
            } else {
                null
            }
        }
    }
}