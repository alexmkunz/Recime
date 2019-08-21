package com.unoknowbo.recime.database.instruction

import androidx.room.Entity
import androidx.room.ForeignKey
import com.unoknowbo.recime.database.recipe.Recipe

@Entity(
    tableName = "instructions",
    primaryKeys = ["recipeId", "orderOfInstruction"],
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Instruction(
    var recipeId: Long,
    var description: String,
    var orderOfInstruction: Int
)