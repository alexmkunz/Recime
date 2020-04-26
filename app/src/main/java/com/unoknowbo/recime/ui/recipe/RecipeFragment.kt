package com.unoknowbo.recime.ui.recipe

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unoknowbo.recime.R
import kotlinx.android.synthetic.main.fragment_recipe.view.*

class RecipeFragment : Fragment() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // This fragment has an options menu
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_recipe, container, false)
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
                // TODO, launch create/edit page
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
