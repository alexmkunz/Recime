package com.unoknowbo.recime.ui.recipe

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unoknowbo.recime.R
import com.unoknowbo.recime.database.RecimeDatabase
import com.unoknowbo.recime.databinding.FragmentRecipeBinding
import kotlinx.android.synthetic.main.fragment_recipe.view.*

class RecipeFragment : Fragment() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var viewPager: ViewPager2

    private var recipeViewModel: RecipeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // This fragment has an options menu
        setHasOptionsMenu(true)

        // Retrieve the recipeId from the arguments bundle
        val args = this.arguments
        val recipeId = requireNotNull(args?.getLong("recipeId"))

        // Get a reference to the binding object and inflate the fragment views
        val binding: FragmentRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe, container, false)

        // Set the lifecycle of the LiveData to this fragment's lifecycle
        binding.lifecycleOwner = this

        // Set the recipeViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = RecimeDatabase.getInstance(application).recipeDao
        val viewModelFactory = RecipeViewModelFactory(dataSource, recipeId)
        recipeViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(
                RecipeViewModel::class.java
            )

        recipeViewModel?.navigateBackToRecipes?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().popBackStack()
                recipeViewModel?.doneDeleting()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = RecipeFragmentArgs.fromBundle(arguments!!)
        recipeAdapter = RecipeAdapter(this, args.recipeId)
        viewPager = view.recipe_pager
        viewPager.adapter = recipeAdapter
        val tabLayout = view.recipe_tab_layout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.information)
                1 -> getString(R.string.ingredients)
                2 -> getString(R.string.instructions)
                else -> error("Recipe tab position > 2")
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_recipe, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_recipe_edit -> {
                val args = RecipeFragmentArgs.fromBundle(arguments!!)
                this.findNavController().navigate(RecipeFragmentDirections
                    .actionRecipeFragmentToEditRecipeFragment(args.recipeId))
            }
            R.id.fragment_recipe_delete -> {
                getConfirmDeleteAlertDialog()?.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getConfirmDeleteAlertDialog(): AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.delete){ _, _ ->
                    recipeViewModel?.delete()
                }
                setNegativeButton(R.string.cancel, null)
                setTitle(R.string.delete_recipe)
                setMessage(R.string.delete_recipe_message)
                setIcon(R.drawable.ic_delete_on_surface_high_emphasis)
            }
            builder.create()
        }
    }
}
