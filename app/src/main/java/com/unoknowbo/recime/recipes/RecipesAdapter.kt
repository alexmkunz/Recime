package com.unoknowbo.recime.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unoknowbo.recime.database.recipe.Recipe
import com.unoknowbo.recime.databinding.ListItemRecipeBinding

class RecipesAdapter(private val clickListener: RecipeListener) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>
    (RecipeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemRecipeBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe, clickListener: RecipeListener) {
            binding.recipe = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecipeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}

class RecipeListener(val clickListener: (recipeId: Long) -> Unit) {
    fun onClick(recipe: Recipe) = clickListener(recipe.id)
}