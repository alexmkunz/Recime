package com.unoknowbo.recime.ui.recipe.instructions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unoknowbo.recime.database.instruction.Instruction
import com.unoknowbo.recime.databinding.ListItemRecipeInstructionBinding

class RecipeInstructionsAdapter : ListAdapter<Instruction, RecipeInstructionsAdapter.ViewHolder>
    (InstructionDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemRecipeInstructionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Instruction) {
            binding.instruction = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecipeInstructionBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }
}

class InstructionDiffCallback : DiffUtil.ItemCallback<Instruction>() {
    override fun areItemsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
        return ((oldItem.recipeId == newItem.recipeId) &&
                (oldItem.orderOfInstruction == newItem.orderOfInstruction))
    }
    override fun areContentsTheSame(oldItem: Instruction, newItem: Instruction): Boolean {
        return oldItem == newItem
    }
}