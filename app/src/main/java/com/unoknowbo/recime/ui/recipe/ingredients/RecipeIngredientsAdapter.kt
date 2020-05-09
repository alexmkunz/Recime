package com.unoknowbo.recime.ui.recipe.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unoknowbo.recime.database.ingredient.Ingredient
import com.unoknowbo.recime.databinding.ListItemRecipeIngredientBinding

class RecipeIngredientsAdapter : ListAdapter<Ingredient, RecipeIngredientsAdapter.ViewHolder>
    (IngredientDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemRecipeIngredientBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Ingredient) {
            binding.ingredient = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecipeIngredientBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }
}

class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return ((oldItem.recipeId == newItem.recipeId) &&
                (oldItem.description == newItem.description))
    }
    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}